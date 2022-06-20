package com.example.abcase.client;

import com.example.abcase.Entity.CurrencyRates;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "openexchangerates", url = "${exchangeClient.url}")
public interface OpenExchangeRatesClient {
    @GetMapping("latest.json")
    CurrencyRates getCurrentRates(@RequestParam("app_id") String appId, @RequestParam("base") String baseCode);

    @GetMapping("historical/{date}.json")
    CurrencyRates getHistoricalRates(@RequestParam("app_id") String appId, @PathVariable String date, @RequestParam("base") String baseCode);

    @GetMapping("currencies.json")
    String getCurrenciesCodes(@RequestParam("app_id") String appId);

}
