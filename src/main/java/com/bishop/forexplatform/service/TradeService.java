package com.bishop.forexplatform.service;

import com.bishop.forexplatform.entity.*;
import com.bishop.forexplatform.repository.TradeRepository;
import com.bishop.forexplatform.repository.UserRepository;
import org.springframework.stereotype.Service;

import java.math.BigDecimal;
import java.util.List;

@Service
public class TradeService {

    private final TradeRepository tradeRepository;
    private final UserRepository userRepository;
    private final RateService rateService;

    public TradeService(TradeRepository tradeRepository, UserRepository userRepository, RateService rateService){
        this.tradeRepository =  tradeRepository;
        this.userRepository = userRepository;
        this.rateService = rateService;
    }

    public String placeTrade(User user, String pair, TradeDirection direction, BigDecimal amount){
        if (amount.compareTo(user.getBalance()) > 0){
            return "Insufficient balance.";
        }

        Double currentRate = rateService.getCurrentRate(pair);
        if (currentRate == null){
            return "Rate unavailable for that pair right now.";
        }

        Trade trade = new Trade(user, pair, direction, amount, currentRate);
        tradeRepository.save(trade);

        user.setBalance(user.getBalance().subtract(amount));
        userRepository.save(user);

        return null;
    }

    public List<Trade> getOpenTrades(User user){
        return tradeRepository.findByUserAndStatus(user, TradeStatus.OPEN);
    }

    public Double calculateProfit(Trade trade, Double currentRate){
        double diff = currentRate - trade.getOpenPrice();
        double directionMultiplier = trade.getDirection() == TradeDirection.BUY ? 1 : -1;
        return diff * directionMultiplier * trade.getAmount().doubleValue();
    }
}
