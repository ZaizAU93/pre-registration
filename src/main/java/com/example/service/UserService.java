package com.example.service;

import com.example.model.Role;
import com.example.model.User;
import com.example.repo.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {


    @Autowired
    private UserRepo userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;


    public void createUser(User user) {
        // Проверяем, существует ли пользователь с данным именем
        if (userRepository.findByUsername(user.getUsername()) == null) {
            User userNew = User.builder()
                            .username(user.getUsername())
                                    .password(passwordEncoder.encode(user.getPassword()))
                                            .role(Role.USER)
                                                    .surname(user.getSurname())
                                                            .name(user.getName())
                                                                    .fathername(user.getFathername())

                    .build();

            userRepository.save(userNew);
        }
    }

}
