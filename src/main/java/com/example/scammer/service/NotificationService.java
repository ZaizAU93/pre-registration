package com.example.scammer.service;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Service;

@Service
public class NotificationService {

    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    public void notifyAvailabilityChange() {
        messagingTemplate.convertAndSend("/topic/availability", "update");
    }

    public void notifyBooking(Long registrarId) {
        messagingTemplate.convertAndSend("/topic/registrar/" + registrarId, "booked");
    }
}