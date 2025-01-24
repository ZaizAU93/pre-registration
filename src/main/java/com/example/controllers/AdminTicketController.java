package com.example.controllers;

import com.example.model.Ticket;
import com.example.service.SystemInfoService;
import com.example.service.TicketService;
import com.example.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.net.UnknownHostException;
import java.util.List;

@Controller
@RequestMapping("/admin/tickets")
public class AdminTicketController {

    @Autowired
    private TicketService ticketService;
    @Autowired
    private SystemInfoService systemInfoService;

    @Autowired
    private UserService userService;

    @GetMapping
    public String getAdminTickets(Model model) throws UnknownHostException {
        List<Ticket> tickets = ticketService.getAllTickets();
        model.addAttribute("tickets", tickets);
        model.addAttribute("user", userService.getCurrentUser());

        return "1"; // имя шаблона Thymeleaf для админов
    }

    // Добавьте методы для управления тикетами (например, закрытие тикета)
}
