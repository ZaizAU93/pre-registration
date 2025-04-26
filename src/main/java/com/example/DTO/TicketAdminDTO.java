package com.example.DTO;

import com.example.model.Ticket;
import com.example.model.User;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

@Data
@Component
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class TicketAdminDTO {

    private User admin;

    private Ticket ticket;

    private User user;

    private String description;

}
