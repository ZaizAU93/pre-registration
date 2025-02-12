package com.example.controllers;

import com.example.model.User;
import com.example.service.DepartmentService;
import com.example.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class UserController {

    @Autowired
    private UserService userService;

    @Autowired
    private DepartmentService departmentService;

    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @GetMapping("/register")
    public String register(Model model) {
        model.addAttribute("departaments", departmentService.getAllDepartaments());
        return "register";
    }

    @PostMapping("/register")
    public String registerUser(@ModelAttribute User user) {

        userService.createUser(user);

        return "redirect:/login"; // Перенаправление на страницу входа после регистрации
    }


    @GetMapping("/admin/register")
    public String registerAdmin(Model model) {
        model.addAttribute("departaments", departmentService.getAllDepartaments());
        return "registerAdmin";
    }

    @PostMapping("/admin/register")
    public String registerAdmin(@ModelAttribute User user) {
        userService.createAdmin(user);
        return "redirect:/login"; // Перенаправление на страницу входа после регистрации
    }


}
