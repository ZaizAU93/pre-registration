package com.example.controllers;

import com.example.model.Messag;
import com.example.model.Status;
import com.example.model.Ticket;
import com.example.model.User;
import com.example.service.TicketService;
import com.example.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;

import java.util.List;
import java.util.Optional;

@Controller
public class ChatMessageController {

    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    @Autowired
    private UserService  userService;

    @Autowired
    private TicketService ticketService;


    @MessageMapping("/chat/{recipient}")
    @SendTo("/queue/messages")
    public void sendMessageToUser(User recipient, Messag message) {

      //  User currentUser = userService.getCurrentUser();

        //List<Optional<Ticket>> tickets = ticketService.getTiketByIdAndStatus(currentUser.getId(), Status.IN_PROGRESS);

        messagingTemplate.convertAndSendToUser(recipient.getUsername(), "/queue/messages", message);
    }
}
