package com.example.model;

import lombok.Data;

@Data
public class Messag {

    private String content;
    private Long senderId;
    private Long recipientId;
    private String senderType;

}
