package com.example.controllers;

import com.example.model.Status;
import com.example.model.Ticket;
import com.example.service.TicketService;
import com.example.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.net.UnknownHostException;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/admin/tickets")
public class AdminTicketController {

    @Autowired
    private TicketService ticketService;

    @Autowired
    private UserService userService;

    @GetMapping()
    public String getAdminTicketsInProgress(Model model) throws UnknownHostException {
        List<Ticket> tickets = ticketService.getAllTickets();
        model.addAttribute("tickets", tickets);
        model.addAttribute("user", userService.getCurrentUser());
        model.addAttribute("admin", userService.getCurrentUser());
        return "profAdmin"; // имя шаблона Thymeleaf для админов
    }


    // взять в работу тикет


    @GetMapping("/api/tickets")
    @ResponseBody
    public List<Ticket> getTickets() {
        return ticketService.getAllTickets();
    }


}
