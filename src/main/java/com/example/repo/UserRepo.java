package com.example.repo;

import com.example.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import java.util.Optional;

import jakarta.transaction.Transactional;
@Repository
public interface UserRepo extends JpaRepository<User, Long> {

    User findByUsername(String username);

    Optional<User> findById(Long id);


}
