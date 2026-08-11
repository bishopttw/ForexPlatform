package com.bishop.forexplatform.controller;

import com.bishop.forexplatform.entity.CurrencyRate;
import com.bishop.forexplatform.entity.User;
import com.bishop.forexplatform.repository.UserRepository;
import com.bishop.forexplatform.service.RateService;
import com.bishop.forexplatform.service.TwelveDataService;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;

import java.util.List;

@Controller
public class DashboardController {

    private final RateService rateService;
    private final UserRepository userRepository;
    private final TwelveDataService twelveDataService;

    public DashboardController(RateService rateService, UserRepository userRepository, TwelveDataService twelveDataService){
        this.rateService = rateService;
        this.userRepository = userRepository;
        this.twelveDataService = twelveDataService;
    }

    @GetMapping("/dashboard")
    public String dashboard(Authentication authentication, Model model){
        User user = userRepository.findByEmail(authentication.getName()).orElseThrow();
        List<CurrencyRate> rates = rateService.getLatestRates();

        model.addAttribute("user", user);
        model.addAttribute("rates", rates);
        return "dashboard";
    }

    @GetMapping("/api/history/{pair}")
    @ResponseBody
    public List<CurrencyRate> getHistory(@PathVariable String pair){
        String formattedPair = pair.toUpperCase().replace("-", "/");
        return rateService.getHistory(formattedPair, 50);
    }

    @GetMapping("/api/candles")
    @ResponseBody
    public List<TwelveDataService.Candle> getCandles(){
        return twelveDataService.getCachedCandles();
    }
}
