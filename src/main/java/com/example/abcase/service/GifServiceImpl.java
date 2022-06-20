package com.example.abcase.service;

import com.example.abcase.Entity.DataDTO;
import com.example.abcase.Entity.GiphyResponse;
import com.example.abcase.client.GifClient;
import com.example.abcase.client.GiphyClient;
import com.example.abcase.util.DataNotFoundException;
import lombok.RequiredArgsConstructor;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;

import java.net.URI;

@Slf4j
@Service
@RequiredArgsConstructor
public class GifServiceImpl implements GifService {
    private final CurrenciesService currenciesService;
    private final GiphyClient giphyClient;

    private final GifClient gifClient;
    @Value("${goodTag}")
    private String goodTag;

    @Value("${badTag}")
    private String badTag;

    @Value("${neutralTag}")
    private String neutralTag;

    @Value("${giphyClient.url}")
    private String giphyClientUrl;

    @Value("${giphyClient.apiKey}")
    private String giphyClientKey;

    @Override
    public GiphyResponse getGifJson(String tag) {
        return giphyClient.getGifJson(giphyClientKey, tag);
    }

    @Override
    public ResponseEntity<byte[]> getGifByteArray(String currencyCode) {
        //Получение тега для поиска гифки
        String gifTag;
        int currencyCompareResult = currenciesService.compareCurrencies(currencyCode);
        switch (currencyCompareResult) {
            case 1:
                gifTag = goodTag;
                break;
            case -1:
                gifTag = badTag;
                break;
            default:
                gifTag = neutralTag;
                break;
        }
        //Получение ссылки на оригинальную гифку
        String gifUrl = getGifUrl(gifTag);

        log.debug(String.format("Request for %s with %s tag", gifUrl, gifTag));
        return gifClient.getGif(URI.create(gifUrl)); //Обращение к giphy сервису с целью получения оригинальной гифки

    }
    private String getGifUrl(String tag) {
        log.debug(String.format("Request for %s/random with %s tag", giphyClientUrl, tag));
        GiphyResponse giphyResponse = getGifJson(tag);//Использование giphy api с целью получения json
        if (giphyResponse == null
                || giphyResponse.getData() == null
                || giphyResponse.getData().getImages() == null
                || giphyResponse.getData().getImages().get("original") == null
                || giphyResponse.getData().getImages().get("original").get("url") == null
        ) {
            log.warn("Giphy client response is not included required data");
            throw new DataNotFoundException("Received data from " + giphyClientUrl + " is null");
        }
        DataDTO dto = giphyResponse .getData();
        return dto.getImages().get("original").get("url");
    }

}
