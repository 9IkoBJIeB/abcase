package com.example.abcase.client;

import com.example.abcase.Entity.GiphyResponse;
import org.springframework.cloud.openfeign.FeignClient;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

@FeignClient(name = "giphy", url = "${giphyClient.url}")
public interface GiphyClient {
    @GetMapping("random")
    GiphyResponse getGifJson(@RequestParam(name = "api_key") String apiKey, @RequestParam(name = "tag") String tag);
}
