package com.example.config;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.web.servlet.HandlerInterceptor;
import org.springframework.stereotype.Component;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.Map;
import java.util.stream.Collectors;

@Component
public class UserLoggingInterceptor implements HandlerInterceptor {
    private static final Logger logger = LoggerFactory.getLogger(UserLoggingInterceptor.class);

    // Объявление формата времени
    private static final DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-MM-dd HH:mm:ss.SSS");
    private static final String START_TIME_ATTRIBUTE = "startTime";
    // В методе preHandle
    @Override
    public boolean preHandle(HttpServletRequest request, HttpServletResponse response, Object handler) throws Exception {
        String currentTime = LocalDateTime.now().format(formatter);
        request.setAttribute(START_TIME_ATTRIBUTE, currentTime);

        Authentication auth = SecurityContextHolder.getContext().getAuthentication();
        String username = (auth != null && auth.isAuthenticated()) ? auth.getName() : "Anonymous";

        String requestURI = request.getRequestURI();
        String method = request.getMethod();

        // Получение параметров запроса
        Map<String, String[]> params = request.getParameterMap();
        String paramsString = params.entrySet().stream()
                .map(e -> e.getKey() + "=" + String.join(",", e.getValue()))
                .collect(Collectors.joining(", "));

        logger.info("{}. Пользователь: {} делает {} запрос к {} с параметрами: [{}]", currentTime, username, method, requestURI, paramsString);
        return true;
    }

    // В методе afterCompletion
    @Override
    public void afterCompletion(HttpServletRequest request, HttpServletResponse response, Object handler, Exception ex) throws Exception {
        String endTime = LocalDateTime.now().format(formatter);
        String requestURI = request.getRequestURI();
        int status = response.getStatus();

        logger.info("{}. Запрос к {} завершен с статусом {} в {}", endTime, requestURI, status, endTime);
        if (ex != null) {
            logger.error("Обработка запроса завершилась исключением", ex);
        }
    }

}