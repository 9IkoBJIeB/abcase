package com.example.abcase.service;

import com.example.abcase.Entity.CurrencyRates;
import com.example.abcase.client.OpenExchangeRatesClient;
import com.example.abcase.util.DataNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import java.time.Clock;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;

@Slf4j
@Service
@RequiredArgsConstructor
public class CurrenciesServiceImpl implements CurrenciesService {

    private final OpenExchangeRatesClient openExchangeRatesClient;

    @Value("${baseCode}")
    private String baseCode;

    @Value("${exchangeClient.url}")
    private String exchangeClientUrl;

    @Value("${exchangeClient.appId}")
    private String exchangeClientKey;

    @Override
    public String getCurrenciesCodes() {
        log.debug("Request for openExchangeRates client: /currencies.json");
        return openExchangeRatesClient.getCurrenciesCodes(exchangeClientKey);
    }

    @Override
    public CurrencyRates getCurrentRates(String appId, String baseCode) {
        return openExchangeRatesClient.getCurrentRates(appId, baseCode);
    }

    @Override
    public CurrencyRates getHistoricalRates(String appId, String date, String baseCode) {
        return openExchangeRatesClient.getHistoricalRates(appId, date, baseCode);
    }

    @Override
    public int compareCurrencies(String currencyCode) {
        log.debug("Request for " + exchangeClientUrl + "/latest.json");
        //Получение текущего курса
        CurrencyRates todayRates = openExchangeRatesClient.getCurrentRates(exchangeClientKey, baseCode);
        //Получение вчерашнего курса
        LocalDateTime currentDateTime = LocalDateTime.now(Clock.systemUTC());
        LocalDateTime yesterdayDateTime = currentDateTime.minusDays(1);
        String yesterdayDate = yesterdayDateTime.format(DateTimeFormatter.ofPattern("yyyy-MM-dd"));
        log.debug(String.format("Request for %s/historical/{%s}.json", exchangeClientUrl, yesterdayDate));
        CurrencyRates yesterdayRates = openExchangeRatesClient.getHistoricalRates(exchangeClientKey, yesterdayDate, baseCode);
        if (todayRates == null
                || yesterdayRates == null
                || todayRates.getRates() == null
                || yesterdayRates.getRates() == null
                || todayRates.getRates().get(currencyCode) == null
                || yesterdayRates.getRates().get(currencyCode) == null
        ) {
            log.warn("OpenExchangeRates client response is not included required data");
            throw new DataNotFoundException("Received data from " + exchangeClientUrl + " is null");
        }
        //Сравнение текущего курса валют со вчерашним
        return todayRates.getRates().get(currencyCode).compareTo(yesterdayRates.getRates().get(currencyCode));
    }

}
