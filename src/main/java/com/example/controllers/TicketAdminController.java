package com.example.controllers;

import com.example.DTO.TicketAdminDTO;
import com.example.model.Ticket;
import com.example.model.TicketAdmin;
import com.example.model.User;
import com.example.service.TicketAdminService;
import com.example.service.TicketService;
import com.example.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.util.ArrayList;
import java.util.List;

@Controller
@RequestMapping("/rup/admin/ticket")
public class TicketAdminController {

    @Autowired
    private TicketAdminService service;

    @Autowired
    private UserService userService;

    @Autowired
    private TicketService ticketService;


    @PostMapping("/save")
    @ResponseBody
    public void save(@RequestParam ("id") Long id,
                     @RequestParam ("reason") String reason
                     )
    {
        TicketAdmin newTicketAdmin = TicketAdmin.builder()
                .ticketId(id)
                .description(reason)
                .build();

        service.save(newTicketAdmin);
    }

    @PostMapping("/hire")
    @ResponseBody
    public ResponseEntity<String> hire(@RequestParam ("id") Long id){
        System.out.println("проверка взятия задачи администратора ------" + id);
       TicketAdmin ticketAdmin = service.findById(id);
       service.updateTcketAdminRup(ticketAdmin, userService.getCurrentUser().getId());
       return ResponseEntity.ok("Должно работать");
    }

    @PostMapping("/close")
    @ResponseBody
    public void close(@RequestParam Long id){
        TicketAdmin ticketAdmin = service.findById(id);
        service.delete(ticketAdmin);
    }

    @GetMapping("/all")
    @ResponseBody
    public List<TicketAdminDTO> getAllTicketAdmin(){

        List<TicketAdminDTO> ticketAdmins = new ArrayList<>();

        List<TicketAdmin> allTicketAdminlist = service.findAll();

        for (TicketAdmin ticketAdmin: allTicketAdminlist) {

            Ticket dtoTicket = ticketService.getTiketById(ticketAdmin.getTicketId()); // находим тикет

            User executorAdmin = userService.getUserById(ticketService.getTiketById(ticketAdmin.getTicketId()).getAdminId()); // админа по id

            User user = ticketService.getTiketById(ticketAdmin.getTicketId()).getUser(); // пользователя по id

            TicketAdminDTO ticketAdminDTO = TicketAdminDTO.builder()
                    .admin(executorAdmin)
                    .ticket(dtoTicket)
                    .user(user)
                    .description(ticketAdmin.getDescription())
                .build();

            ticketAdmins.add(ticketAdminDTO);
        }

        return ticketAdmins;
    }




}
