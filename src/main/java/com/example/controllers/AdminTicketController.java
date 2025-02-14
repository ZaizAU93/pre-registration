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
import java.util.ArrayList;
import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;


@Controller
@RequestMapping("/admin/tickets")
public class AdminTicketController {

    @Autowired
    private TicketService ticketService;

    @Autowired
    private UserService userService;

    @GetMapping()
    public String getAdminTicketsInProgress(Model model) throws UnknownHostException {
   //     List<Ticket> tickets = ticketService.getAllTickets();

        List<Ticket> ticketsNew = ticketService.getTikecetStatusNew(Status.NEW);
        for (Ticket tik: ticketsNew) {
            System.out.println("новые  " + tik.getDescription());
        }
        List<Ticket> ticketsInProgress = ticketService.getTikecetStatusProgResol(Status.IN_PROGRESS);
        for (Ticket tik: ticketsInProgress) {
            System.out.println("в работе  " + tik.getDescription());
        }
        List<Ticket> ticketResolved = ticketService.getTikecetStatusProgResol(Status.RESOLVED);

        for (Ticket tik: ticketResolved) {
            System.out.println("закрытые  " + tik.getDescription());
        }



        model.addAttribute("ticketsNew", ticketsNew);
        model.addAttribute("ticketsInProgress", ticketsInProgress);
        model.addAttribute("ticketsResolved", ticketResolved);
        model.addAttribute("user", userService.getCurrentUser());
        model.addAttribute("admin", userService.getCurrentUser());
        return "profAdmin"; // имя шаблона Thymeleaf для админов
    }


    // взять в работу тикет
    @GetMapping("/api/tickets")
    @ResponseBody
    public List<Ticket> getTickets() {
        List<Ticket> ticketsNew = ticketService.getTikecetStatusNew(Status.NEW);
        List<Ticket> ticketsInProgress = ticketService.getTikecetStatusProgResol(Status.IN_PROGRESS);
        List<Ticket> ticketsResolved = ticketService.getTikecetStatusProgResol(Status.RESOLVED);

        // Здесь можно объединить все тикеты в один список для упрощения обработки на стороне клиента
        List<Ticket> allTickets = new ArrayList<>();
        allTickets.addAll(ticketsNew);
        allTickets.addAll(ticketsInProgress);
        allTickets.addAll(ticketsResolved);

        return allTickets;
    }

}
