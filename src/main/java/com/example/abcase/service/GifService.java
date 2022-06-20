package com.example.abcase.service;

import com.example.abcase.Entity.GiphyResponse;
import org.springframework.http.ResponseEntity;

public interface GifService {
    ResponseEntity<byte[]> getGifByteArray(String baseCode);

    GiphyResponse getGifJson(String tag);

}
