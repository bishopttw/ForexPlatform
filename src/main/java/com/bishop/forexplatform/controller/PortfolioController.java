package com.bishop.forexplatform.controller;

import com.bishop.forexplatform.entity.Trade;
import com.bishop.forexplatform.entity.User;
import com.bishop.forexplatform.repository.UserRepository;
import com.bishop.forexplatform.service.RateService;
import com.bishop.forexplatform.service.TradeService;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import java.util.LinkedHashMap;
import java.util.List;
import java.util.Map;

@Controller
public class PortfolioController {

    private final TradeService tradeService;
    private final UserRepository userRepository;
    private final RateService rateService;

    public PortfolioController(TradeService tradeService, UserRepository userRepository, RateService rateService){
        this.tradeService = tradeService;
        this.userRepository = userRepository;
        this.rateService = rateService;
    }

    @GetMapping("/portfolio")
    public String portfolio(Authentication authentication, Model model){
        User user = userRepository.findByEmail(authentication.getName()).orElseThrow();
        List<Trade> openTrades = tradeService.getOpenTrades(user);

        Map<Trade, Double> tradesWithProfit = new LinkedHashMap<>();
        for (Trade trade : openTrades){
            Double currentRate = rateService.getCurrentRate(trade.getPair());
            Double profit = (currentRate != null) ? tradeService.calculateProfit(trade, currentRate) : 0.0;
            tradesWithProfit.put(trade, profit);
        }

        model.addAttribute("user", user);
        model.addAttribute("tradesWithProfit", tradesWithProfit);
        return "portfolio";
    }

    @PostMapping("/portfolio/close")
    public String closeTrade(@RequestParam Long tradeId, Authentication authentication, Model model){
        User user = userRepository.findByEmail(authentication.getName()).orElseThrow();
        tradeService.closeTrade(user, tradeId);
        return "redirect:/portfolio";
    }
}
