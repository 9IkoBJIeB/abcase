package com.example.abcase.controller;

import com.example.abcase.service.CurrenciesService;
import com.example.abcase.service.GifService;
import lombok.RequiredArgsConstructor;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

@RestController
@RequiredArgsConstructor
@RequestMapping("/api/v0")
public class CodeController {
    private final GifService codeService;
    private final CurrenciesService currenciesService;

    @GetMapping("/rates")
    public String getCurrenciesCodes() {
        return currenciesService.getCurrenciesCodes();
    }

    @GetMapping("/rates/{currencyCode}")
    public ResponseEntity<byte[]> getGif(@PathVariable String currencyCode) {
        return codeService.getGifByteArray(currencyCode);
    }

}
