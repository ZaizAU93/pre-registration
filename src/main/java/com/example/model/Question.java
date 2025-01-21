package com.example.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Question {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String questionText; // текст вопроса

    @ManyToOne
    @JoinColumn(name = "test_id")
    private Test test;

    @OneToMany
    @JoinColumn(name = "answer_id")
    private List<Answer> answers;  //  ответ

    @OneToMany
    @JoinColumn(name = "correct_answer_id")
    private List<Сanswer> сorrectAnswer;  // Правильный ответ
}
