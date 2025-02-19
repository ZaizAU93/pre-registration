package com.example.controllers;

import com.example.model.Messag;
import com.example.service.TicketService;
import com.example.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;


@Controller
public class ChatMessageController {

    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    @Autowired
    private UserService  userService;

    @Autowired
    private TicketService ticketService;


    @GetMapping("/chat/user")
    public String chatPage(@RequestParam("adminId") Long adminId, @RequestParam("sender") Long sender, Model model) {
        model.addAttribute("adminId", adminId);
        model.addAttribute("sender", sender);
        return "chat";
    }



    @MessageMapping("/chat")
    public void processMessage(@Payload Messag message) {


        // Отправка сообщения конкретному получателю
        String destination = "/queue/messages/" + message.getRecipientId();
        messagingTemplate.convertAndSendToUser(
                message.getRecipientId().toString(),
                destination,
                message
        );
    }







}
