package com.example.abcase.client;

import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;

import java.net.URI;

@FeignClient(name = "gif", url = "/")
public interface GifClient {
    @GetMapping
    ResponseEntity<byte[]> getGif(URI uri);
}
