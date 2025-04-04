package com.example.controllers;

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


    @GetMapping("/all")
    @ResponseBody
    public List<Notification> getNotificationsRecipient(){
        return service.getAllNotificationsRecipient(userService.getCurrentUser().getId());
    }

    @PostMapping("/delete")
    @ResponseBody
    public void deleteHistoryNotifications(@RequestParam Long id, @RequestParam Long user, @RequestParam Long ticket ){
        System.out.println("!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!!");
        System.out.println("id отправителя " + id);
        System.out.println("id получателя " + user);
        System.out.println("id тикета " + ticket);
        service.deleteHistoryNotifications(id, user, ticket);
    }



}
