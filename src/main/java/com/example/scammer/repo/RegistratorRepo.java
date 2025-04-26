package com.example.scammer.repo;

import com.example.scammer.Registrar;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface RegistratorRepo extends JpaRepository<Registrar, Long> {

    Optional<Registrar> findById(Long id);

    Optional<Registrar> findByUserIdReg(Long id);

    List<Registrar> findBySurname(String surname);

}
