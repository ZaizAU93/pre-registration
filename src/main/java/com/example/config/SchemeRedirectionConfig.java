package com.example.config;

import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;

import java.util.*;
//@Configuration
public class SchemeRedirectionConfig {

  //  @Bean
    public static Map<String, List<Integer>> schemeRedirections(){
        return Map.of(
                "OBL", new ArrayList<>(Arrays.asList(1, 2, 3)),
                "MOLODECHNO", new ArrayList<>(Arrays.asList(4, 5, 6, 7, 8, 9)),
                "DERZINSK",new ArrayList<>( Arrays.asList(10, 11, 12, 13)),
                "SLUTSK", new ArrayList<>(Arrays.asList(14, 15, 16, 17)),
                "BORISOV", new ArrayList<>(Arrays.asList(18, 19, 20, 21, 22, 23))
        );
    }
}

