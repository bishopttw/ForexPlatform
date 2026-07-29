package com.bishop.forexplatform.controller;

import com.bishop.forexplatform.entity.CurrencyRate;
import com.bishop.forexplatform.service.RateService;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import java.util.List;

@Controller
public class DashboardController {

    private final RateService rateService;

    public DashboardController(RateService rateService){
        this.rateService = rateService;
    }

    @GetMapping("/dashboard")
    public String dashboard(Model model){
        List<CurrencyRate> rates = rateService.getAllSavedRates();
        model.addAttribute("rates", rates);
        return "dashboard";
    }
}
