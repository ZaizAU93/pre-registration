package com.example.controllers;

import com.example.DTO.AnchorDeleteTicketOverListDTO;
import com.example.model.*;
import com.example.service.ComputerService;
import com.example.service.ProblemService;
import com.example.service.TicketService;
import com.example.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.handler.annotation.SendTo;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.net.UnknownHostException;
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

    @Autowired
    private ProblemService problemService;

    @Autowired
    private ComputerService computerService;

    @GetMapping
    public String getTickets(Model model) {
        List<Ticket> tickets = ticketService.getAllTickets();
        model.addAttribute("tickets", tickets);
        return "ticketList"; // имя шаблона Thymeleaf
    }

    @GetMapping("/new")
    public String createTicketForm(Model model) {
        model.addAttribute("ticket", new Ticket());

        List<Problem> problems = problemService.getAllProblems();

        model.addAttribute("problems", problems);


        return "ticketForm"; // имя шаблона Thymeleaf
    }


    @PostMapping("/create")
    public String createTicketForm(@ModelAttribute Ticket ticket) throws UnknownHostException {

        Computer computer = computerService.getSystemInfo();

        Long id = computerService.save(computer);


        Long idTicketNew = ticketService.createTicket(ticket, computerService.getComputerById(id) );

        messagingTemplate.convertAndSend("/topic/tickets", ticketService.getTiketById(idTicketNew));

        return "redirect:/tickets"; // перенаправление на список тикетов

    }



    @MessageMapping("/ticket")
    @SendTo("/topic/tickets")
    public Ticket sendTicket(Ticket ticket) {
        Ticket ticket1 = ticket;

        ticket1.setUser(userService.getCurrentUser());

        return ticket1; // Возвращаем тикет, чтобы отправить его всем подписчикам
    }


    @MessageMapping("/delete/ticket")
    public void deleteTicketOverList(@Payload AnchorDeleteTicketOverListDTO dto) {
        messagingTemplate.convertAndSend("/topic/delete/ticket", dto);
    }



    @PostMapping("/hire")
    @ResponseBody
    public ResponseEntity<Long> hireInProgressTicket(@RequestParam("id") Long id) {
        Status status = Status.IN_PROGRESS;
        ticketService.jobs(status, id);
        messagingTemplate.convertAndSend("/topic/tickets", ticketService.getTiketById(id) );
        User user = ticketService.getUserByTicketId(id);
        return ResponseEntity.ok(user.getId());
    }



    @MessageMapping("/tickets/update")
    @SendTo("/topic/tickets")
    public Ticket sendTicketInProgress(Ticket ticket){
        return ticket;
    }


    @PostMapping("/close")
    @ResponseBody
    public  ResponseEntity<String> closeTiket(@RequestParam("id") Long id){
        Status status = Status.RESOLVED;
        ticketService.jobs(status, id);
        return ResponseEntity.ok("Задача завершена");
    }
}
