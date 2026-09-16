package com.bishop.forexplatform.service;

import com.bishop.forexplatform.entity.Trade;
import com.bishop.forexplatform.entity.TradeDirection;
import com.bishop.forexplatform.entity.User;
import com.bishop.forexplatform.repository.TradeRepository;
import com.bishop.forexplatform.repository.UserRepository;
import com.bishop.forexplatform.repository.VerificationTokenRepository;
import org.junit.jupiter.api.Test;
import org.springframework.security.crypto.password.PasswordEncoder;

import java.math.BigDecimal;

import static org.junit.jupiter.api.Assertions.assertEquals;

class TradeServiceTest {

    private final TradeService tradeService = new TradeService(
            null, null, null
    );

    @Test
    void buyTrade_profitsWhenRateGoesUp() {
        User user = new User();
        Trade trade = new Trade(user, "EUR/USD", TradeDirection.BUY, new BigDecimal("1000"), 1.1000);

        Double profit = tradeService.calculateProfit(trade, 1.1050);

        assertEquals(5.0, profit, 0.0001);
    }

    @Test
    void buyTrade_losesWhenRateGoesDown() {
        User user = new User();
        Trade trade = new Trade(user, "EUR/USD", TradeDirection.BUY, new BigDecimal("1000"), 1.1000);

        Double profit = tradeService.calculateProfit(trade, 1.0950);

        assertEquals(-5.0, profit, 0.0001);
    }

    @Test
    void sellTrade_profitsWhenRateGoesDown() {
        User user = new User();
        Trade trade = new Trade(user, "EUR/USD", TradeDirection.SELL, new BigDecimal("1000"), 1.1000);

        Double profit = tradeService.calculateProfit(trade, 1.0950);

        assertEquals(5.0, profit, 0.0001);
    }

    @Test
    void sellTrade_losesWhenRateGoesUp() {
        User user = new User();
        Trade trade = new Trade(user, "EUR/USD", TradeDirection.SELL, new BigDecimal("1000"), 1.1000);

        Double profit = tradeService.calculateProfit(trade, 1.1050);

        assertEquals(-5.0, profit, 0.0001);
    }
}