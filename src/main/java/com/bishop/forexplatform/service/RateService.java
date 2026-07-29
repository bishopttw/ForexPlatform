package com.bishop.forexplatform.service;

import com.bishop.forexplatform.entity.CurrencyRate;
import com.bishop.forexplatform.repository.CurrencyRateRepository;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.util.Map;
import java.time.LocalDateTime;
import java.util.List;

@Service
public class RateService {

    private final RestClient restClient = RestClient.create();
    private final CurrencyRateRepository currencyRateRepository;

    private static final List<String> PAIRS = List.of("EUR/USD", "GBP/USD", "USD/JPY");

    public RateService(CurrencyRateRepository currencyRateRepository){
        this.currencyRateRepository = currencyRateRepository;
    }

    public Double fetchRate(String baseCurrency,String targetCurrency) {
        String url = "https://cdn.jsdelivr.net/npm/@fawazahmed0/currency-api@latest/v1/currencies/"
                + baseCurrency.toLowerCase() + ".json";

        Map<String, Object> response = restClient.get()
                .uri(url)
                .retrieve()
                .body(Map.class);

        Map<String, Object> rates = (Map<String, Object>) response.get(baseCurrency.toLowerCase());
        Object rateValue = rates.get(targetCurrency.toLowerCase());

        return ((Number) rateValue).doubleValue();
    }

    @Scheduled(fixedRate = 3600000)
    public void refreshAllRates(){
        for(String pair : PAIRS) {
            String[] parts = pair.split("/");
            String base = parts[0];
            String target = parts[1];

            Double rate = fetchRate(base, target);

            CurrencyRate currencyRate = currencyRateRepository.findByPair(pair)
                    .orElse(new CurrencyRate(pair, rate, LocalDateTime.now()));

            currencyRate.setRate(rate);
            currencyRate.setLastUpdated(LocalDateTime.now());

            currencyRateRepository.save(currencyRate);
        }
    }

    public List<CurrencyRate> getAllSavedRates(){
        return currencyRateRepository.findAll();
    }
}
