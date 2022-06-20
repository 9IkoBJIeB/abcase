package com.example.abcase.service;

import com.example.abcase.Entity.DataDTO;
import com.example.abcase.Entity.GiphyResponse;
import com.example.abcase.client.GifClient;
import com.example.abcase.client.GiphyClient;
import com.example.abcase.util.DataNotFoundException;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.ResponseEntity;

import javax.naming.ldap.HasControls;

import java.net.URI;
import java.util.HashMap;
import java.util.Map;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;
@SpringBootTest
@ExtendWith(MockitoExtension.class)
class GifServiceImplTest {
    @MockBean
    CurrenciesService currenciesService;

    @MockBean
    GiphyClient giphyClient;

    @MockBean
    GifClient gifClient;

    @Autowired
    GifService gifService;

    @Value("${goodTag}")
    private String goodTag;

    @Value("${badTag}")
    private String badTag;

    @Value("${neutralTag}")
    private String neutralTag;

    @Value("${giphyClient.apiKey}")
    private String giphyClientKey;


    @Test
    void getGifJson() {
        GiphyResponse expected= mock(GiphyResponse.class);
        when(giphyClient.getGifJson(eq(giphyClientKey),anyString())).thenReturn(expected);

        GiphyResponse actual =gifService.getGifJson("");

        assertEquals(actual,expected);
        verify(giphyClient).getGifJson(eq(giphyClientKey),anyString());

    }

    // getGifByteArray Tests

    @Test
    void whenCurrenciesServiceReturnGoodTag_thenReturnGoodGif() {
        String niceUrl = "niceURL";
        GiphyResponse expectedResponse = initTestGiphyResponse(niceUrl);
        ResponseEntity<byte[]> expected = mock(ResponseEntity.class);
        when(currenciesService.compareCurrencies(anyString())).thenReturn(1);
        when(giphyClient.getGifJson(anyString(),eq(goodTag))).thenReturn(expectedResponse);
        when(gifClient.getGif(URI.create(niceUrl))).thenReturn(expected);

        ResponseEntity actual = gifService.getGifByteArray("");

        assertEquals(expected,actual);
        verify(currenciesService).compareCurrencies(anyString());
        verify(giphyClient).getGifJson(anyString(),eq(goodTag));
        verify(gifClient).getGif(URI.create(niceUrl));
    }

    @Test
    void whenCurrenciesServiceReturnBadTag_thenReturnBadGif() {
        String niceUrl = "niceURL";
        GiphyResponse expectedResponse = initTestGiphyResponse(niceUrl);
        ResponseEntity<byte[]> expected = mock(ResponseEntity.class);
        when(currenciesService.compareCurrencies(anyString())).thenReturn(-1);
        when(giphyClient.getGifJson(anyString(),eq(badTag))).thenReturn(expectedResponse);
        when(gifClient.getGif(URI.create(niceUrl))).thenReturn(expected);

        ResponseEntity actual = gifService.getGifByteArray("");

        assertEquals(expected,actual);
        verify(currenciesService).compareCurrencies(anyString());
        verify(giphyClient).getGifJson(anyString(),eq(badTag));
        verify(gifClient).getGif(URI.create(niceUrl));
    }

    @Test
    void whenCurrenciesServiceThrowException_thenThrowException() {

        ResponseEntity<byte[]> expected = mock(ResponseEntity.class);
        when(currenciesService.compareCurrencies(anyString())).thenThrow(new DataNotFoundException(""));

        assertThrows(DataNotFoundException.class,() -> {currenciesService.compareCurrencies(anyString());});

        verify(currenciesService).compareCurrencies(anyString());
        verify(giphyClient,never()).getGifJson(anyString(),anyString());
        verify(gifClient,never()).getGif(any());

    }

    @Test
    void whenGiphyClientReturnNull_thenThrowException() {
        String niceUrl = "niceURL";
        GiphyResponse expectedResponse = initTestGiphyResponse(niceUrl);
        ResponseEntity<byte[]> expected = mock(ResponseEntity.class);
        when(currenciesService.compareCurrencies(anyString())).thenReturn(-1);
        when(giphyClient.getGifJson(anyString(),eq(badTag))).thenReturn(null);
        when(gifClient.getGif(URI.create(niceUrl))).thenReturn(expected);

        assertThrows(DataNotFoundException.class,() -> {gifService.getGifByteArray("");});

        verify(currenciesService).compareCurrencies(anyString());
        verify(giphyClient).getGifJson(anyString(),eq(badTag));
        verify(gifClient,never()).getGif(any());
    }
    private GiphyResponse initTestGiphyResponse(String niceUrl){
        GiphyResponse expectedResponse = new GiphyResponse();
        DataDTO dataDTO = new DataDTO();
        Map<String,Map<String,String>> map = new HashMap<>();
        Map<String,String> map2 = new HashMap<>();
        map2.put("url",niceUrl);
        map.put("original",map2);
        dataDTO.setImages(map);
        expectedResponse.setData(dataDTO);
        return expectedResponse;
    }

}