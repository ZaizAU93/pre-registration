package com.example.controllers;

import com.example.model.User;
import com.example.scammer.service.RegistratorService;
import com.example.service.UserService;
import org.springframework.core.io.Resource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;


import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.Map;
import java.util.stream.Collectors;

@Controller
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private RegistratorService registratorService;

    @GetMapping("/login")
    public String login() {
        return "login";
    }



    @GetMapping("/register")
    public String register(Model model) {
        model.addAttribute("user", new User());
        return "register";
    }

    @PostMapping("/register")
    public String registerUser(@ModelAttribute User user) {
        Long idUser = userService.createUser(user);
        registratorService.save(user,idUser );
        return "redirect:/login"; // Перенаправление на страницу входа после регистрации
    }


    @GetMapping("/admin/register")
    public String registerAdmin(Model model) {
        model.addAttribute("user", new User());
        return "registerAdmin";
    }

    @PostMapping("/admin/register")
    public String registerAdmin(@ModelAttribute User user) {
        userService.createAdmin(user);
        return "redirect:/login"; // Перенаправление на страницу входа после регистрации
    }

}
