package com.example.DTO;

import com.example.model.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class NotificationDTO {
    private Long sender;
    private String content;
    private Long recipient;

    private Long ticketId;
}
