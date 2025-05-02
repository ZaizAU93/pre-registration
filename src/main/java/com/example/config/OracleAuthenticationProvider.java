package com.example.config;

import org.springframework.security.authentication.AuthenticationProvider;
import org.springframework.security.authentication.BadCredentialsException;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.AuthenticationException;
import org.springframework.stereotype.Component;

import java.sql.Connection;
import java.sql.DriverManager;
import java.sql.SQLException;
import java.util.Collections;

@Component
public class OracleAuthenticationProvider implements AuthenticationProvider {

    @Override
    public Authentication authenticate(Authentication authentication)
            throws AuthenticationException {

        String username = authentication.getName();
        String password = authentication.getCredentials().toString();

        // Попытка подключения к Oracle с введёнными данными
        try (Connection connection = DriverManager.getConnection(
                "jdbc:oracle:thin:@DENISBAS",
                username,
                password)) {

            // Если подключение успешно - пользователь аутентифицирован
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
