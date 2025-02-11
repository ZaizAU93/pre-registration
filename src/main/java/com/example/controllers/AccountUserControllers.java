package com.example.controllers;

import com.example.model.Status;
import com.example.model.Ticket;
import com.example.model.User;
import com.example.service.TicketService;
import com.example.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import java.util.List;

@Controller("/account")
public class AccountUserControllers {

    @Autowired
    private UserService userService;

    @Autowired
    private TicketService ticketService;


    @GetMapping()
    public String getAccountUser(Model model){

        User currentUser = userService.getCurrentUser();

        List<Ticket> tickets = ticketService.findByUser(currentUser);

        ticketService.searchAdminAllTicketInStatusInprogress(currentUser.getId(), Status.IN_PROGRESS);

        model.addAttribute("user", currentUser);
        model.addAttribute("tickets", tickets);
        model.addAttribute("");

        return "profUsers";
    }


}
