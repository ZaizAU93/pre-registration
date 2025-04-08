package com.example.controllers;

import com.example.model.TicketAdmin;
import com.example.service.TicketAdminService;
import com.example.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;
import oshi.hardware.platform.unix.freebsd.FreeBsdPowerSource;

import java.util.List;

@Controller
@RequestMapping("/rup/admin/ticket")
public class TicketAdminController {

    @Autowired
    private TicketAdminService service;

    @Autowired
    private UserService userService;


    @PostMapping("/save")
    @ResponseBody
    public void save(TicketAdmin ticketAdmin){
        service.save(ticketAdmin);
    }

    @PostMapping("/hire")
    @ResponseBody
    public void hire(Long id){
       TicketAdmin ticketAdmin = service.findById(id);

       service.updateTcketAdminRup(ticketAdmin, userService.getCurrentUser().getId());

    }

    @PostMapping("/close")
    @ResponseBody
    public void close(Long id){
        TicketAdmin ticketAdmin = service.findById(id);
        service.delete(ticketAdmin);
    }

    @GetMapping("/all")
    @ResponseBody
    public List<TicketAdmin> getAllTicketAdmin(){
        return service.findAll();
    }

}
