package com.bishop.forexplatform.service;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.scheduling.annotation.Scheduled;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestClient;

import java.util.ArrayList;
import java.util.Collections;
import java.util.List;
import java.util.Map;

@Service
public class TwelveDataService {

    private final RestClient restClient = RestClient.create();

    @Value("${twelvedata.api.key}")
    private String apiKey;

    private volatile List<Candle> cachedCandles = new ArrayList<>();

    @Scheduled(fixedRate = 900000)
    public void refreshCandles() {
        String url = "https://api.twelvedata.com/time_series?symbol=EUR/USD&interval=1h&outputsize=30&apikey=" + apiKey;

        Map<String, Object> response = restClient.get()
                .uri(url)
                .retrieve()
                .body(Map.class);

        List<Map<String, String>> values = (List<Map<String, String>>) response.get("values");
        if (values == null){
            return;
        }

        List<Candle> candles = new ArrayList<>();
        for (Map<String, String> entry : values) {
            candles.add(new Candle(
                    entry.get("datetime"),
                    Double.parseDouble(entry.get("open")),
                    Double.parseDouble(entry.get("high")),
                    Double.parseDouble(entry.get("low")),
                    Double.parseDouble(entry.get("close"))
            ));
        }

        Collections.reverse(candles);
        cachedCandles = candles;
    }

    public List<Candle> getCachedCandles() {
        return cachedCandles;
    }

    public static class Candle {
        public String datetime;
        public double open;
        public double high;
        public double low;
        public double close;

        public Candle(String datetime, double open, double high, double low, double close) {
            this.datetime = datetime;
            this.open = open;
            this.high = high;
            this.low = low;
            this.close = close;
        }
    }
}
