package com.example.controllers;

import com.example.model.ChatMessage;
import com.example.model.Messag;
import com.example.model.SenderType;
import com.example.repo.ChatMessageRepository;
import com.example.service.ChatMessageService;
import com.example.service.TicketService;
import com.example.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;

import javax.management.Notification;
import java.util.List;

@Controller
public class ChatMessageController {

    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    @Autowired
    private  UserService userService;

    @Autowired
    private ChatMessageRepository chatMessageRepository;

    @MessageMapping("/chat")
    public void processMessage(@Payload ChatMessage message) {
       chatMessageRepository.save(message);

        // Отправляем сообщение получателю
        String destination = "/queue/messages/" + message.getRecipientId();
        messagingTemplate.convertAndSendToUser(
                message.getRecipientId().toString(),
                destination,
                message
        );

        // Отправляем уведомление отправителю (если нужно)
        if (message.getSenderType().equals("ADMIN")) {
            messagingTemplate.convertAndSendToUser(
                    message.getSenderId().toString(),
                    "/queue/notifications",
                    "Ваше сообщение доставлено пользователю."
            );
        } else {
            messagingTemplate.convertAndSendToUser(
                    message.getSenderId().toString(),
                    "/queue/notifications",
                    "Ваше сообщение доставлено администратору."
            );
        }
    }

    @GetMapping("/chat/admin")
    public String chatPageAdmin(@RequestParam("userId") Long userId, @RequestParam("adminId") Long adminId, Model model) {
        model.addAttribute("userId", userId);
        model.addAttribute("adminId", adminId);
        return "admin-chat";
    }

    @GetMapping("/chat/user")
    public String chatPageUser(@RequestParam("adminId") Long adminId, Model model) {
        model.addAttribute("adminId", adminId);
        model.addAttribute("sender", userService.getCurrentUser().getId() );
        return "chat";
    }



}
