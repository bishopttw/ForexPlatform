package com.bishop.forexplatform.service;

import com.bishop.forexplatform.entity.User;
import com.bishop.forexplatform.entity.VerificationToken;
import com.bishop.forexplatform.repository.UserRepository;
import com.bishop.forexplatform.repository.VerificationTokenRepository;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.time.LocalDateTime;
import java.util.UUID;
import java.util.regex.Pattern;

@Service
public class UserService {

    private static final Pattern PASSWORD_PATTERN =
            Pattern.compile("^(?=.*[A-Z])(?=.*[!@#$%^*(),.?\":{}|<>]).{8,}$");

    private final UserRepository userRepository;
    private final VerificationTokenRepository tokenRepository;
    private final EmailService emailService;
    private final PasswordEncoder passwordEncoder;

    public UserService(UserRepository userRepository,VerificationTokenRepository tokenRepository,
                       PasswordEncoder passwordEncoder, EmailService emailService) {
        this.userRepository = userRepository;
        this.tokenRepository = tokenRepository;
        this.passwordEncoder = passwordEncoder;
        this.emailService = emailService;
    }

    public boolean emailExists(String email) {
        return userRepository.existsByEmail(email);
    }

    public boolean isPasswordValid(String password) {
        return PASSWORD_PATTERN.matcher(password).matches();
    }

    public void registerUser(String email, String username, String rawPassword) {
        String hashedPassword = passwordEncoder.encode(rawPassword);
        User newUser = new User(email, username, hashedPassword, new BigDecimal("10000"));
        userRepository.save(newUser);

        String token = UUID.randomUUID().toString();
        LocalDateTime expiry = LocalDateTime.now().plusMinutes(5);
        VerificationToken verificationToken = new VerificationToken(token, newUser, expiry);
        tokenRepository.save(verificationToken);

        emailService.sendVerificationEmail(newUser.getEmail(), token);
    }
}
