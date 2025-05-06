package com.example.config;

import com.example.model.User;
import com.example.scammer.DTO.UserDTO;
import com.example.scammer.repo.PreEntryRepository;
import com.example.scammer.service.RegistratorService;
import com.example.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Collections;

@Component
public class OracleAuthenticationProvider implements AuthenticationProvider {

    @Autowired
    PreEntryRepository preEntryRepository;

    private RegistratorService registratorService;

    @Autowired
    public void setRegistratorService(RegistratorService registratorService) {
        this.registratorService = registratorService;
    }

    @Autowired
    UserService userService;

    @Override
    public Authentication authenticate(Authentication authentication)
            throws AuthenticationException {

        String username = authentication.getName();
        String password = authentication.getCredentials().toString();



        try (Connection connection = DriverManager.getConnection(
                "jdbc:oracle:thin:@DENISBAS",
                username,
                password)) {

            // Если подключение успешно — создаем и сохраняем пользователя
            UserDTO userDTO = preEntryRepository.getUser(username);

            // Проверка, существует ли пользователь уже
            User existingUser = userService.getUserByUsername(userDTO.getDBUSER());
            if (existingUser == null) {
                User user = User.builder()
                        .username(userDTO.getDBUSER())
                        .name(userDTO.getUSERNAME())
                        .password(password)
                        .userUID(userDTO.getUSERUID())
                        .build();
                userService.createUser(user);

                User userD = userService.getUserByUsername(userDTO.getDBUSER());
                registratorService.saveDTO(userDTO, userD);
            }

            // Сохраняем DTO


            // Возвращаем успешную аутентификацию
            return new UsernamePasswordAuthenticationToken(
                    username,
                    password,
                    Collections.emptyList()
            );

        } catch (SQLException e) {
            throw new BadCredentialsException("Invalid Oracle credentials");
        }
    }


    @Override
    public boolean supports(Class<?> authentication) {
        return authentication.equals(UsernamePasswordAuthenticationToken.class);
    }
}
