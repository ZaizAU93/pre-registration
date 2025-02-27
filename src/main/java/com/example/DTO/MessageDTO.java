package com.example.DTO;

import com.example.model.SenderType;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import lombok.Data;

import java.time.LocalDateTime;

@Data
public class MessageDTO {
    private Long senderId;
    private Long recipientId;

    @Enumerated(EnumType.STRING)
    private SenderType senderType;

    private String content;

}
