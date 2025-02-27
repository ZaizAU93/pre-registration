package com.example.service;

import com.example.DTO.UserDTO;
import com.example.model.Role;
import com.example.model.User;
import com.example.repo.UserRepo;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

@Service
public class UserService {


    @Autowired
    private UserRepo userRepository;

    @Autowired
    private PasswordEncoder passwordEncoder;


    public void createUser(User user) {

        System.out.println("Деапартамент ID: " + user.getDepartametId());


        // Проверяем, существует ли пользователь с данным именем
        if (userRepository.findByUsername(user.getUsername()) == null) {
            User userNew = User.builder()
                    .username(user.getUsername())
                    .password(passwordEncoder.encode(user.getPassword()))
                    .role(Role.USER)
                    .surname(user.getSurname())
                    .name(user.getName())
                    .fathername(user.getFathername())
                    .departametId(user.getDepartametId())
                    .build();

            userRepository.save(userNew);
        }
    }


    public void createAdmin(User user) {
        // Проверяем, существует ли пользователь с данным именем
        if (userRepository.findByUsername(user.getUsername()) == null) {
            User userNew = User.builder()
                    .username(user.getUsername())
                    .password(passwordEncoder.encode(user.getPassword()))
                    .role(Role.ADMIN)
                    .surname(user.getSurname())
                    .name(user.getName())
                    .fathername(user.getFathername())
                    .departametId(user.getDepartametId())
                    .build();

            userRepository.save(userNew);
        }
    }


    public User getCurrentUser() {
        Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
        // Получаем детали пользователя
        Object principal = authentication.getPrincipal();

        UserDetails userDetails = (UserDetails) principal;
        String username = userDetails.getUsername();

        User user = userRepository.findByUsername(username);


        return user;
    }


    public User getUserById(Long id){
        return userRepository.findById(id).get();
    }


    public User getUserByUsername(String username){
        return userRepository.findByUsername(username);
    };

}