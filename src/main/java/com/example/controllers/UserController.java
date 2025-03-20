package com.example.controllers;

import com.example.model.Ticket;
import com.example.model.User;
import com.example.service.DepartmentService;
import com.example.service.JobTitleService;
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
    private DepartmentService departmentService;

    @Autowired
    private JobTitleService jobTitleService;
    @GetMapping("/login")
    public String login() {
        return "login";
    }

    @GetMapping("/register")
    public String register(Model model) {
        model.addAttribute("user", new User());
        model.addAttribute("departaments", departmentService.getAllDepartaments());
        model.addAttribute("jobTitle", jobTitleService.getAllJobTitle());
        return "register";
    }

    @PostMapping("/register")
    public String registerUser(@ModelAttribute User user) {

        userService.createUser(user);

        return "redirect:/login"; // Перенаправление на страницу входа после регистрации
    }


    @GetMapping("/admin/register")
    public String registerAdmin(Model model) {
        model.addAttribute("user", new User());
        model.addAttribute("departaments", departmentService.getAllDepartaments());
        model.addAttribute("jobTitle", jobTitleService.getAllJobTitle());
        return "registerAdmin";
    }

    @PostMapping("/admin/register")
    public String registerAdmin(@ModelAttribute User user) {
        userService.createAdmin(user);
        return "redirect:/login"; // Перенаправление на страницу входа после регистрации
    }



    @PostMapping("/user/saveAvatar")
    public ResponseEntity<?> saveAvatar(@RequestBody Map<String, String> request) {
        User currentUser = userService.getCurrentUser();
        userService.updateAvatar(request.get("avatar"), currentUser);
        return ResponseEntity.ok().build();
    }


    @GetMapping("/api/avatars")
    @ResponseBody
    public List<String> getAvailableAvatars() throws IOException {
        Resource[] resources = new PathMatchingResourcePatternResolver()
                .getResources("classpath:/static/avatars/*.*"); // Добавлены расширения

        return Arrays.stream(resources)
                .map(resource -> {
                    try {
                        return resource.getURL().toString(); // Получаем полный URL
                    } catch (IOException e) {
                        throw new RuntimeException(e);
                    }
                })
                .map(url -> url.substring(url.lastIndexOf("/") + 1)) // Извлекаем имя файла
                .collect(Collectors.toList());
    }

    @GetMapping("/user/avatar")
    @ResponseBody
    public String getUserAvatar() {
        User user = userService.getCurrentUser();
        return user.getAvatar() != null ? user.getAvatar() : "";
    }
}
