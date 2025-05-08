package com.example.config;

import com.example.model.Role;
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
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.stereotype.Component;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Collection;
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
                        .userTypeCode(userDTO.getUSERTYPECODE())
                        .build();
                userService.createUser(user);

                User userD = userService.getUserByUsername(userDTO.getDBUSER());
                registratorService.saveDTO(userDTO, userD);
            }

            String userRole = determineUserRole(userDTO); // ваш метод или логика


            GrantedAuthority authority = new SimpleGrantedAuthority( "ROLE_" +userRole);
            Collection<GrantedAuthority> authorities = Collections.singletonList(authority);


            // Возвращаем успешную аутентификацию
            return new UsernamePasswordAuthenticationToken(
                    username,
                    password,
                  //  Collections.emptyList()
                    authorities
            );

        } catch (SQLException e) {
            throw new BadCredentialsException("Invalid Oracle credentials");
        }
    }


    @Override
    public boolean supports(Class<?> authentication) {
        return authentication.equals(UsernamePasswordAuthenticationToken.class);
    }


    private String determineUserRole(UserDTO userDTO) {
        int typeCode = userDTO.getUSERTYPECODE();

        Role role = switch (typeCode) {
            case 99 -> {
                // сложные операции
                yield Role.ADMINISTRATOR;
            }
            case 0,3 -> {

                yield Role.REGISTRATOR;
            }
            case 1 -> {
                yield Role.BOSSREGISR;
            }
            case 51 -> {
                yield Role.KOORDINATOR;
            }

            default -> {
                yield Role.GUEST;
            }
        };
        return role.toString();
    }
}
