package com.example.controllers;

import com.example.model.User;
import com.example.service.DepartmentService;
import com.example.service.UserService;
import org.springframework.core.io.Resource;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.core.io.support.PathMatchingResourcePatternResolver;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;

import java.io.IOException;
import java.util.Arrays;
import java.util.List;
import java.util.stream.Collectors;

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



    @PostMapping("/user/saveAvatar")
    public ResponseEntity<?> saveAvatar(@RequestParam String selectedAvatar) {

       User currentUser = userService.getCurrentUser();

       userService.updateAvatar(selectedAvatar, currentUser);

        return ResponseEntity.ok().build();
    }
    //@Value("${upload.avatar.dir}")
    //private String avatarUploadDir;

    @GetMapping("/api/avatars")
    @ResponseBody
    public List<String> getAvailableAvatars() throws IOException {
        Resource[] resources =  new PathMatchingResourcePatternResolver()
                .getResources("classpath:static/avatars/*");
        return Arrays.stream(resources)
                .map(resource -> resource.getFilename())
                .collect(Collectors.toList());
    }

    @GetMapping("/user/avatar")
    @ResponseBody
    public String getUserAvatar(){
        return userService.getCurrentUser().getAvatar();
    }

}
