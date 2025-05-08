package com.example.config;

import com.example.model.Role;
import com.example.repo.UserRepo;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.web.authentication.AuthenticationSuccessHandler;

import java.io.IOException;
import java.util.Arrays;
import java.util.Collection;
import java.util.List;

public class CustomAuthenticationSuccessHandler implements AuthenticationSuccessHandler {


    @Override
    public void onAuthenticationSuccess(HttpServletRequest request,
                                        HttpServletResponse response,
                                        Authentication authentication) throws IOException, ServletException {
        Collection<? extends GrantedAuthority> authorities = authentication.getAuthorities();
        System.out.println("Роли пользователя:");
        String redirectUrl = "/login"; // URL по умолчанию

        for (GrantedAuthority authority : authorities) {
            String role = authority.getAuthority();
            System.out.println("Роль: " + role);
            if (role.equals("ROLE_ADMIN") || role.equals("ROLE_ADMINISTRATOR") ||
                    role.equals("ROLE_KOORDINATOR") || role.equals("ROLE_BOSSREGISR")) {
                redirectUrl = "/registrar";
                System.out.println("Redirecting to: " + redirectUrl);
                break;
            } else if (role.equals("ROLE_REGISTRATOR")) {
                redirectUrl = "/registrar/free-slots";
                System.out.println("Redirecting to: " + redirectUrl);
                break;
            }
        }

        response.sendRedirect(redirectUrl);
        System.out.println("Перенаправление на: " + redirectUrl);
    }


}
