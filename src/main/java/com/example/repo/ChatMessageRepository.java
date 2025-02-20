package com.example.repo;

import com.example.model.ChatMessage;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ChatMessageRepository extends JpaRepository<ChatMessage, Long> {

   List<ChatMessage> findBySenderIdAndRecipientIdOrRecipientIdAndSenderIdOrderByTimestamp(
           Long senderId, Long recipientId, Long otherSenderId, Long otherRecipientId);


}
