package com.example.controllers;

import com.example.model.Question;
import com.example.model.Test;
import com.example.repo.QuestionRepo;
import com.example.repo.TestRepo;
import com.example.service.QuestionService;
import com.example.service.TestService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/test")
public class TestController {

    @Autowired
    private TestRepo testRepository;

    @Autowired
    private QuestionRepo questionRepo;

    @GetMapping
    public String listTests(Model model) {
        List<Test> tests = testRepository.findAll();
        model.addAttribute("tests", tests);
        return "list";
    }

    @GetMapping("/create")
    public String createTestForm(Model model) {
        model.addAttribute("test", new Test());
        return "create";
    }

    @PostMapping("/create")
    public String createTest(@ModelAttribute Test test) {
        testRepository.save(test);
        return "redirect:/test";
    }



    @GetMapping("/{id}/edit")
    public String editTest(@PathVariable Long id, Model model) {
        Optional<Test> test = testRepository.findById(id);
        model.addAttribute("test", test);
        model.addAttribute("questions", questionRepo.findByTest(test.get()));
        return "editTest";
    }

    @PostMapping("/{id}/addQuestion")
    public String addQuestion(@PathVariable Long id, @ModelAttribute Question question) {
        Optional<Test> test = testRepository.findById(id);
        Question quest = Question.builder().
                questionText(question.getQuestionText()).
                answers(question.getAnswers()).
                сorrectAnswer(question.getСorrectAnswer()).
                build();
        questionRepo.save(question);
        return "redirect:/test/" + id + "/edit"; // Перенаправление обратно на страницу редактирования
    }
}