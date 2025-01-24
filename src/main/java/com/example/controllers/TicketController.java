package com.example.controllers;

import com.example.model.Ticket;
import com.example.service.TicketService;
import com.example.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/tickets")
public class TicketController {

    @Autowired
    private TicketService ticketService;

    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    @Autowired
    private UserService userService;

    @GetMapping
    public String getTickets(Model model) {
        List<Ticket> tickets = ticketService.getAllTickets();
        model.addAttribute("tickets", tickets);
        return "ticketList"; // имя шаблона Thymeleaf
    }

    @GetMapping("/new")
    public String createTicketForm(Model model) {
        model.addAttribute("ticket", new Ticket());
        return "ticketForm"; // имя шаблона Thymeleaf
    }

    @PostMapping
    public String createTicket(@ModelAttribute Ticket ticket) {
        ticketService.createTicket(ticket);
        messagingTemplate.convertAndSend("/topic/tickets", ticket);
        return "redirect:/tickets"; // перенаправление на список тикетов
    }

    @MessageMapping("/ticket")
    @SendTo("/topic/tickets")
    public Ticket sendTicket(Ticket ticket) {
        Ticket ticket1 = ticket;

        ticket1.setUser(userService.getCurrentUser());

        return ticket1; // Возвращаем тикет, чтобы отправить его всем подписчикам
    }
}
