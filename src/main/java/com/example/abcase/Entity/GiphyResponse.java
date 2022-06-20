package com.example.abcase.Entity;

import lombok.Data;

import java.util.Map;

@Data
public class GiphyResponse {

    private DataDTO data;

    private Map<String,Object> meta;
}
