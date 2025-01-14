package com.example.model;

import jakarta.persistence.*;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.util.ArrayList;
import java.util.List;

@Entity
@Data
public class Question {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String questionText; // текст вопроса

    @ManyToOne
    @JoinColumn(name = "test_id")
    private Test test;

    private String answer; // Правильный ответ

    private boolean multipleAnswers; // многоответность
}
