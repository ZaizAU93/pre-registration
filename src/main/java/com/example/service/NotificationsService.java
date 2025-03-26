package com.example.service;

import com.example.model.Notification;
import com.example.repo.NotificationRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class NotificationsService {

    @Autowired
    private NotificationRepo repo;

    public List<Notification> getAllNotificationsRecipient(Long id){
        return repo.findByAdminId(id);
    }


    public void saveNotifications(Notification notification){
        Notification saveNotifications = Notification.builder()
                .adminId(notification.getAdminId())
                .sender(notification.getSender())
                .localDate(LocalDate.now())
                .content(notification.getContent())
                .build();
    }


}
