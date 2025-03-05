package com.example.model;

import lombok.Data;

@Data
public class Notification {
    private Long sender;
    private String content;
    private Long adminId;
}
