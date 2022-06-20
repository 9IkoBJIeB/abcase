package com.example.abcase.Entity;

import lombok.Data;
import java.math.BigDecimal;
import java.util.Map;

@Data
public class CurrencyRates {

    private String disclaimer;

    private String license;

    private long timestamp;

    private String base;

    private Map<String, BigDecimal> rates;
}
