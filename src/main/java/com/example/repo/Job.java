package com.example.repo;

import com.example.model.JobTitle;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface Job extends JpaRepository<JobTitle, Long> {

    List<JobTitle> findAllBy();

    Optional<JobTitle> findById(Long id);

}
