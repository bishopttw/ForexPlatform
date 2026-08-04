package com.bishop.forexplatform.repository;

import com.bishop.forexplatform.entity.Trade;
import com.bishop.forexplatform.entity.TradeDirection;
import com.bishop.forexplatform.entity.TradeStatus;
import com.bishop.forexplatform.entity.User;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface TradeRepository extends JpaRepository<Trade, Long>{
    List<Trade> findByUserAndStatus(User user, TradeStatus status);
}
