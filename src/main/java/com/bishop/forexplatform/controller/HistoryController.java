package com.bishop.forexplatform.controller;

import com.bishop.forexplatform.entity.Trade;
import com.bishop.forexplatform.entity.TradeStatus;
import com.bishop.forexplatform.entity.User;
import com.bishop.forexplatform.repository.UserRepository;
import com.bishop.forexplatform.repository.TradeRepository;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
public class HistoryController {

    private final TradeRepository tradeRepository;
    private final UserRepository userRepository;

    public HistoryController(TradeRepository tradeRepository, UserRepository userRepository){
        this.tradeRepository = tradeRepository;
        this.userRepository = userRepository;
    }

    @GetMapping("/history")
    public String history(Authentication authentication, Model model){
        User user = userRepository.findByEmail(authentication.getName()).orElseThrow();
        List<Trade> closedTrades = tradeRepository.findByUserAndStatus(user , TradeStatus.CLOSED);

        model.addAttribute("user", user);
        model.addAttribute("trades",closedTrades);
        return "history";
    }
}
