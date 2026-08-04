package com.bishop.forexplatform.controller;

import com.bishop.forexplatform.entity.Trade;
import com.bishop.forexplatform.entity.TradeDirection;
import com.bishop.forexplatform.entity.User;
import com.bishop.forexplatform.repository.UserRepository;
import com.bishop.forexplatform.service.TradeService;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.math.BigDecimal;

@Controller
public class TradeController {
    private final TradeService tradeService;
    private final UserRepository userRepository;

    public TradeController(TradeService tradeService, UserRepository userRepository){
        this.tradeService = tradeService;
        this.userRepository = userRepository;
    }

    @PostMapping("/trade")
    public String placeTrade(@RequestParam String pair,
                             @RequestParam TradeDirection direction,
                             @RequestParam BigDecimal amount,
                             Authentication authentication,
                             Model model) {
        String email = authentication.getName();
        User user = userRepository.findByEmail(email).orElseThrow();

        String error = tradeService.placeTrade(user, pair, direction, amount);
        if (error != null){
            model.addAttribute("tradeError", error);
        }

        return "redirect:/dashboard";
    }
}
