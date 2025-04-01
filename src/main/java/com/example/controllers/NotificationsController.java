package com.example.controllers;

import com.example.DTO.NotificationDTO;
import com.example.model.Notification;
import com.example.service.NotificationsService;
import com.example.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/notifications")
public class NotificationsController {

    @Autowired
    private NotificationsService service;

    @Autowired
    private UserService userService;

/*
    @PostMapping("/save")
    public void save(@ModelAttribute NotificationDTO dto){

        Notification notification = Notification.builder()
                .content(dto.getContent())
                .adminId(dto.getRecipient())
                .sender(dto.getSender())
                .build();

        service.saveNotifications(notification);
    }
*/
    @GetMapping("/all")
    @ResponseBody
    public List<Notification> getNotificationsRecipient(){
        return service.getAllNotificationsRecipient(userService.getCurrentUser().getId());
    }


}
