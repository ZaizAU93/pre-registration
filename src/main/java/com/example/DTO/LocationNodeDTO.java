package com.example.DTO;

import lombok.Data;

import java.util.List;

@Data
public class LocationNodeDTO {
    private String id;
    private String text;
    private String type;
    private List<LocationNodeDTO> children;

}