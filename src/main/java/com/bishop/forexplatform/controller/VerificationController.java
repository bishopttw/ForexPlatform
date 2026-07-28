package com.bishop.forexplatform.controller;

import com.bishop.forexplatform.entity.User;
import com.bishop.forexplatform.entity.VerificationToken;
import com.bishop.forexplatform.repository.UserRepository;
import com.bishop.forexplatform.repository.VerificationTokenRepository;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.LocalDateTime;

@Controller
public class VerificationController {

    private final VerificationTokenRepository tokenRepository;
    private final UserRepository userRepository;

    public VerificationController(VerificationTokenRepository tokenRepository,
                                   UserRepository userRepository) {
        this.tokenRepository = tokenRepository;
        this.userRepository = userRepository;
    }

    @GetMapping("/verify")
    public String verifyAccount(@RequestParam String token) {
        VerificationToken verificationToken = tokenRepository.findByToken(token).orElse(null);

        if (verificationToken == null || verificationToken.getExpiryDate().isBefore(LocalDateTime.now())){
            return "redirect:/login?verifyError";
        }

        User user = verificationToken.getUser();
        user.setEnabled(true);
        userRepository.save(user);

        return "redirect:/login?verified";
    }
}
