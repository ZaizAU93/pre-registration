package com.example.controllers;

import com.example.model.Problem;
import com.example.model.Status;
import com.example.model.Ticket;
import com.example.model.User;
import com.example.service.ProblemService;
import com.example.service.TicketService;
import com.example.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpEntity;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/account")
public class AccountUserControllers {

    @Autowired
    private UserService userService;

    @Autowired
    private TicketService ticketService;

    @Autowired
    private ProblemService problemService;

    @GetMapping()
    public String getAccountUser(Model model){
        model.addAttribute("ticket", new Ticket());

        model.addAttribute("user", userService.getCurrentUser());

        List<Problem> problems = problemService.getAllProblems();

        model.addAttribute("problems", problems);

        model.addAttribute("tiket_users", ticketService.getTiketByUser(userService.getCurrentUser()));

        return "profUsers";
    }


    @GetMapping("/ticket/status")
    @ResponseBody
    public List<Ticket> getTicketUsersId(){
        return ticketService.getTiketByUser(userService.getCurrentUser());
    }

    @GetMapping("/admin")
    @ResponseBody
    public User getAdminIdChat(@RequestParam Long id){
        return userService.getUserById(id);
    }

}
