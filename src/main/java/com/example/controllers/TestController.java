package com.example.controllers;

import com.example.model.Test;
import com.example.repo.TestRepo;
import com.example.service.TestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class TestController {

    @Autowired
    private TestService testService;

    @GetMapping("/index")
    public String getIndexPage(){
        return "index";
    }

    @PostMapping("/new/test")
    public String createTest(@ModelAttribute Test test){
        testService.createTest(test);
        return "redirect:tests";
    }

    @GetMapping("/test")
    public String getAllTest(Model model){
        model.addAttribute("listTests", testService.getTests());
        return "tests";
    }


}
