package com.bishop.forexplatform.repository;

import com.bishop.forexplatform.entity.CurrencyRate;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;
import java.util.List;

public interface CurrencyRateRepository extends JpaRepository<CurrencyRate, Long> {
    List<CurrencyRate> findByPairOrderByLastUpdatedDesc(String pair);
    Optional<CurrencyRate> findFirstByPairOrderByLastUpdatedDesc(String pair);
}
