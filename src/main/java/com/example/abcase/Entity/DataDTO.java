package com.example.abcase.Entity;

import lombok.Data;
import java.util.Map;

@Data
public class DataDTO {

    private String type;

    private String id;

    private String url;

    private String title;

    private Map<String, Map<String,String>> images;
}
