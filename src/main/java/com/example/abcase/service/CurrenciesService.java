package com.example.abcase.service;

import com.example.abcase.Entity.CurrencyRates;
import com.example.abcase.util.DataNotFoundException;
import org.springframework.web.bind.annotation.RequestParam;

import java.time.Clock;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

public interface CurrenciesService {
    public String getCurrenciesCodes();

    public CurrencyRates getCurrentRates(String appId, String baseCode);

    public CurrencyRates getHistoricalRates(String appId, String date, String baseCode);

    public int compareCurrencies(String currencyCode);

}
