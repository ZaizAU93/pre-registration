package com.example.controllers;

import com.example.model.Answer;
import com.example.model.Сanswer;
import com.example.model.Question;
import com.example.repo.AnswerRepo;
import com.example.repo.CorrectAnswerRepo;
import com.example.repo.QuestionRepo;
import com.example.repo.TestRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
@RequestMapping("/questions")
public class QuestionController {
/*
    @Autowired
    private QuestionRepo questionRepository;

    @Autowired
    private TestRepo testRepository;

    @Autowired
    private AnswerRepo answerRepository;

    @Autowired
    private CorrectAnswerRepo correctAnswerRepository;

    @GetMapping
    public String listQuestions(Model model) {
        List<Question> questions = questionRepository.findAll();
        model.addAttribute("questions", questions);
        return "question/list";
    }

    @GetMapping("/create")
    public String createQuestionForm(Model model) {
        model.addAttribute("question", new Question());
        model.addAttribute("tests", testRepository.findAll());
        return "question/create";
    }

    @PostMapping("/create")
    public String createQuestion(@ModelAttribute Question question,
                                 @RequestParam List<String> wrongAnswers,
                                 @RequestParam List<String> correctAnswers) {
        // Сохранение вопроса
        questionRepository.save(question);
        for (String content : wrongAnswers) {
            Answer answer = new Answer();
            answer.setContent(content);
            answer.setQuestion(question); // Связываем ответ с вопросом
            answerRepository.save(answer);
        }

        // Сохранение правильных ответов
        for (String content : correctAnswers) {
            Сanswer correctAnswer = new Сanswer();
            correctAnswer.setContent(content);
            correctAnswer.setQuestions(question); // Связываем правильный ответ с вопросом
            correctAnswerRepository.save(correctAnswer);
        }

        return "redirect:/questions";
    }

    @GetMapping("/edit/{id}")
    public String editQuestionForm(@PathVariable Long id, Model model) {
        Question question = questionRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Invalid question Id:" + id));
        model.addAttribute("question", question);
        model.addAttribute("tests", testRepository.findAll());
        return "question/edit"; // Создайте соответствующий шаблон edit.html
    }

    @PostMapping("/edit/{id}")
    public String updateQuestion(@PathVariable Long id,
                                 @ModelAttribute Question updatedQuestion,
                                 @RequestParam List<String> wrongAnswers,
                                 @RequestParam List<String> correctAnswers) {
        Question question = questionRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Invalid question Id:" + id));

        // Обновляем данные вопроса
        question.setQuestionText(updatedQuestion.getQuestionText());
        questionRepository.save(question);

        // Удаляем старые ответы, если необходимо (можно улучшить логику)
        answerRepository.deleteAllByQuestion(question);
        correctAnswerRepository.deleteAllByQuestion(question);

        // Сохраняем новые неправильные ответы
        for (String content : wrongAnswers) {
            Answer answer = new Answer();
            answer.setContent(content);
            answer.setQuestion(question);
            answerRepository.save(answer);
        }

        // Сохраняем новые правильные ответы
        for (String content : correctAnswers) {
            Сanswer correctAnswer = new Сanswer();
            correctAnswer.setContent(content);
            correctAnswer.setQuestions(question);
            correctAnswerRepository.save(correctAnswer);
        }

        return "redirect:/questions";
    }

    @GetMapping("/delete/{id}")
    public String deleteQuestion(@PathVariable Long id) {
        Question question = questionRepository.findById(id).orElseThrow(() -> new IllegalArgumentException("Invalid question Id:" + id));
        questionRepository.delete(question);
        return "redirect:/questions";
    }

 */
}
