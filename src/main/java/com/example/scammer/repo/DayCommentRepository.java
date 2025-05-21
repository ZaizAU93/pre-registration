package com.example.scammer.repo;

import com.example.scammer.DayComment;
import com.example.scammer.Registrar;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

@Repository
public interface DayCommentRepository extends JpaRepository<DayComment, Long> {
    Optional<DayComment> findByRegistrarAndDate(Registrar registrar, LocalDate date);

    List<DayComment> findByRegistrar(Registrar registrar);

    List<DayComment> findByRegistrarAndDateBetween(Registrar registrar, LocalDate startDate, LocalDate endDate);
}