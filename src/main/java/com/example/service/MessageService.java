package com.example.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

@Service
public class MessageService {

    private final SimpMessagingTemplate messagingTemplate;

    @Autowired
    public MessageService(SimpMessagingTemplate messagingTemplate) {
        this.messagingTemplate = messagingTemplate;
    }

    public void sendMessageToUser(String username, String message) {
        messagingTemplate.convertAndSendToUser(username, "/queue/messages", message);
    }
}
