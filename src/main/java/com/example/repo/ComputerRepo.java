package com.example.repo;

import com.example.model.Computer;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface ComputerRepo extends JpaRepository<Computer, Long> {

    Computer getComputerById(Long id);

}
