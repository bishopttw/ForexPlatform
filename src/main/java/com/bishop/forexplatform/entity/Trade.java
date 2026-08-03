package com.bishop.forexplatform.entity;

import jakarta.persistence.*;
import java.math.BigDecimal;
import java.time.LocalDateTime;

@Entity
@Table(name = "trades")
public class Trade {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne
    @JoinColumn(nullable = false)
    private User user;

    @Column(nullable = false)
    private String pair;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TradeDirection direction;

    @Column(nullable = false)
    private BigDecimal amount;

    @Column(nullable = false)
    private Double openPrice;

    @Column(nullable = false)
    private LocalDateTime openTime;

    private Double closePrice;
    private LocalDateTime closeTime;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private TradeStatus status;

    private Double profit;

    public Trade(){}

    public Trade(User user, String pair, TradeDirection direction, BigDecimal amount, Double openPrice){
        this.user = user;
        this.pair = pair;
        this.direction = direction;
        this.amount = amount;
        this.openPrice = openPrice;
        this.openTime = LocalDateTime.now();
        this.status = TradeStatus.OPEN;
    }

    public Long getId() {
        return id;
    }

    public User getUser() {
        return user;
    }

    public String getPair() {
        return pair;
    }

    public TradeDirection getDirection() {
        return direction;
    }

    public BigDecimal getAmount() {
        return amount;
    }

    public Double getOpenPrice() {
        return openPrice;
    }

    public LocalDateTime getOpenTime() {
        return openTime;
    }

    public Double getClosePrice() {
        return closePrice;
    }

    public void setClosePrice(Double closePrice) {
        this.closePrice = closePrice;
    }

    public LocalDateTime getCloseTime() {
        return closeTime;
    }

    public void setCloseTime(LocalDateTime closeTime) {
        this.closeTime = closeTime;
    }

    public TradeStatus getStatus() {
        return status;
    }

    public void setStatus(TradeStatus status) {
        this.status = status;
    }

    public Double getProfit() {
        return profit;
    }

    public void setProfit(Double profit) {
        this.profit = profit;
    }
}
