package com.example.service;

import com.example.model.ChatMessage;
import com.example.repo.ChatMessageRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.List;

@Service
public class ChatMessageService {

    @Autowired
    private ChatMessageRepository repository;

    public ChatMessage save(ChatMessage message) {
        message.setTimestamp(LocalDateTime.now());
        return repository.save(message);
    }

    public List<ChatMessage> findChatMessages(Long senderId, Long recipientId) {
        return repository.findBySenderIdAndRecipientIdOrRecipientIdAndSenderIdOrderByTimestamp(
                senderId, recipientId, senderId, recipientId);
    }
}