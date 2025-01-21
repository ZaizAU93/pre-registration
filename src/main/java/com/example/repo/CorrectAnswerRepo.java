package com.example.repo;

import com.example.model.Question;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import com.example.model.Сanswer;
@Repository
public interface CorrectAnswerRepo extends JpaRepository<Сanswer, Long> {
    void deleteAllByQuestion(Question question);
}
