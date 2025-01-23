package com.example.controllers;

import com.example.model.Ticket;
import com.example.service.TicketService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.List;

@Controller
@RequestMapping("/admin/tickets")
public class AdminTicketController {

    @Autowired
    private TicketService ticketService;

    @GetMapping
    public String getAdminTickets(Model model) {
        List<Ticket> tickets = ticketService.getAllTickets();
        model.addAttribute("tickets", tickets);
        return "adminTicketList"; // имя шаблона Thymeleaf для админов
    }

    // Добавьте методы для управления тикетами (например, закрытие тикета)
}
