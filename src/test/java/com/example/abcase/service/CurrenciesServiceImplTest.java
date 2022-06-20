package com.example.abcase.service;

import com.example.abcase.Entity.CurrencyRates;
import com.example.abcase.client.OpenExchangeRatesClient;
import com.example.abcase.util.DataNotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;

import java.math.BigDecimal;
import java.util.HashMap;


import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

@SpringBootTest
@ExtendWith(MockitoExtension.class)
 class CurrenciesServiceImplTest {

    @MockBean
    OpenExchangeRatesClient openExchangeRatesClient;
    @Autowired
    CurrenciesService currenciesService;

    @Value("${exchangeClient.appId}")
    private String exchangeClientKey;

    @Value("${baseCode}")
    private String baseCode;

    @Test
    void getCurrenciesCodes(){
        String expected = "{ AED: United Arab Emirates Dirham, AFN: Afghan Afghani...";
        when(openExchangeRatesClient.getCurrenciesCodes(eq(exchangeClientKey))).thenReturn("{ AED: United Arab Emirates Dirham, AFN: Afghan Afghani...");

        String actual = currenciesService.getCurrenciesCodes();

        assertEquals(expected,actual);
        verify(openExchangeRatesClient).getCurrenciesCodes(eq(exchangeClientKey));
    }

    @Test
    void getCurrentRates() {
        CurrencyRates expected = mock(CurrencyRates.class);
        when(openExchangeRatesClient.getCurrentRates(eq(exchangeClientKey),eq(baseCode))).thenReturn(expected);

        CurrencyRates actual = currenciesService.getCurrentRates(exchangeClientKey,baseCode);

        assertNotNull(actual);
        assertEquals(expected,actual);
        verify(openExchangeRatesClient).getCurrentRates(eq(exchangeClientKey),eq(baseCode));
    }

    @Test
    void getHistoricalRates() {
        CurrencyRates expected = mock(CurrencyRates.class);
        when(openExchangeRatesClient.getHistoricalRates(eq(exchangeClientKey),any(),eq(baseCode))).thenReturn(expected);

        CurrencyRates actual = currenciesService.getHistoricalRates(exchangeClientKey,"",baseCode);

        assertNotNull(actual);
        assertEquals(expected,actual);
        verify(openExchangeRatesClient).getHistoricalRates(eq(exchangeClientKey),any(),eq(baseCode));
    }

    //compareCurrencies Tests

    @Test
    void whenTodayLargerYesterdayThenReturnPositive() {
        String currencyCode = "RUB";
        CurrencyRates today = new CurrencyRates();
        CurrencyRates yesterday = new CurrencyRates();
        today.setRates(new HashMap<String, BigDecimal>() {{
            put(currencyCode, new BigDecimal(2));
        }});
        yesterday.setRates(new HashMap<String, BigDecimal>() {{
            put(currencyCode, new BigDecimal(1));
        }});
        when(openExchangeRatesClient.getCurrentRates(eq(exchangeClientKey),eq(baseCode))).thenReturn(today);
        when(openExchangeRatesClient.getHistoricalRates(eq(exchangeClientKey),any(),eq(baseCode))).thenReturn(yesterday);

        int actual = currenciesService.compareCurrencies(currencyCode);

        assertTrue(actual==1);
        verify(openExchangeRatesClient).getCurrentRates(eq(exchangeClientKey),eq(baseCode));
        verify(openExchangeRatesClient).getHistoricalRates(eq(exchangeClientKey),any(),eq(baseCode));

    }

    @Test
    void whenTodayLesserYesterdayThenReturnNegative() {
        String currencyCode = "RUB";
        CurrencyRates today = new CurrencyRates();
        CurrencyRates yesterday = new CurrencyRates();
        today.setRates(new HashMap<String, BigDecimal>() {{
            put(currencyCode, new BigDecimal(2));
        }});
        yesterday.setRates(new HashMap<String, BigDecimal>() {{
            put(currencyCode, new BigDecimal(3));
        }});
        when(openExchangeRatesClient.getCurrentRates(eq(exchangeClientKey),eq(baseCode))).thenReturn(today);
        when(openExchangeRatesClient.getHistoricalRates(eq(exchangeClientKey),any(),eq(baseCode))).thenReturn(yesterday);

        int result = currenciesService.compareCurrencies(currencyCode);

        assertTrue(result==-1);
        verify(openExchangeRatesClient).getCurrentRates(eq(exchangeClientKey),eq(baseCode));
        verify(openExchangeRatesClient).getHistoricalRates(eq(exchangeClientKey),any(),eq(baseCode));

    }

    @Test
    void whenTodaySameYesterdayThenReturnZero() {
        String currencyCode = "RUB";
        CurrencyRates today = new CurrencyRates();
        CurrencyRates yesterday = new CurrencyRates();
        today.setRates(new HashMap<String, BigDecimal>() {{
            put(currencyCode, new BigDecimal(2));
        }});
        yesterday.setRates(new HashMap<String, BigDecimal>() {{
            put(currencyCode, new BigDecimal(2));
        }});
        when(openExchangeRatesClient.getCurrentRates(eq(exchangeClientKey),eq(baseCode))).thenReturn(today);
        when(openExchangeRatesClient.getHistoricalRates(eq(exchangeClientKey),any(),eq(baseCode))).thenReturn(yesterday);

        int result = currenciesService.compareCurrencies(currencyCode);

        assertTrue(result==0);
        verify(openExchangeRatesClient).getCurrentRates(eq(exchangeClientKey),eq(baseCode));
        verify(openExchangeRatesClient).getHistoricalRates(eq(exchangeClientKey),any(),eq(baseCode));

    }

    @Test
    void whenGetCurrentRatesReturnNullThenThrowException() {
        String currencyCode = "RUB";
        CurrencyRates yesterday = new CurrencyRates();
        yesterday.setRates(new HashMap<String, BigDecimal>() {{
            put(currencyCode, new BigDecimal(2));
        }});
        when(openExchangeRatesClient.getCurrentRates(eq(exchangeClientKey),eq(baseCode))).thenReturn(null);
        when(openExchangeRatesClient.getHistoricalRates(eq(exchangeClientKey),any(),eq(baseCode))).thenReturn(yesterday);

        assertThrows(DataNotFoundException.class,() -> {currenciesService.compareCurrencies(currencyCode);});

        verify(openExchangeRatesClient).getCurrentRates(eq(exchangeClientKey),eq(baseCode));
        verify(openExchangeRatesClient).getHistoricalRates(eq(exchangeClientKey),any(),eq(baseCode));

    }

    @Test
    void whenGetHistoricalRatesReturnNullThenThrowException() {
        String currencyCode = "RUB";
        CurrencyRates today = new CurrencyRates();
        today.setRates(new HashMap<String, BigDecimal>() {{
            put(currencyCode, new BigDecimal(2));
        }});
        when(openExchangeRatesClient.getCurrentRates(eq(exchangeClientKey),eq(baseCode))).thenReturn(null);
        when(openExchangeRatesClient.getHistoricalRates(eq(exchangeClientKey),any(),eq(baseCode))).thenReturn(null);

        assertThrows(DataNotFoundException.class,() -> {currenciesService.compareCurrencies(currencyCode);});

        verify(openExchangeRatesClient).getCurrentRates(eq(exchangeClientKey),eq(baseCode));
        verify(openExchangeRatesClient).getHistoricalRates(eq(exchangeClientKey),any(),eq(baseCode));

    }

}
