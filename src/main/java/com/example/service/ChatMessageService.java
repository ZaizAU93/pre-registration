package com.example.service;

import com.example.model.ChatMessage;
import com.example.repo.ChatMessageRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

@Service
public class ChatMessageService {

    @PersistenceContext
    private EntityManager entityManager;
    @Autowired
    private ChatMessageRepository repository;

    @Autowired
    private UserService userService;

    public ChatMessage save(ChatMessage message) {
        message.setTimestamp(LocalDateTime.now());
        return repository.save(message);
    }




    public List<ChatMessage> getChatUsers(Long id){
        Long userOne = userService.getCurrentUser().getId();
        Long userToo = id;

        String jpql = "SELECT t FROM ChatMessage t WHERE t.recipientId =:userOne and t.senderId =:userToo";
        TypedQuery<ChatMessage> query = entityManager.createQuery(jpql, ChatMessage.class);
        query.setParameter("userOne", userOne );
        query.setParameter("userToo", userToo);
        return query.getResultList();
    }


}