package com.example.controllers;

import com.example.DTO.MessageDTO;
import com.example.model.ChatMessage;
import com.example.repo.ChatMessageRepository;
import com.example.service.ChatMessageService;
import com.example.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.handler.annotation.Payload;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.Comparator;
import java.util.List;

@Controller
public class ChatMessageController {

    @Autowired
    private SimpMessagingTemplate messagingTemplate;

    @Autowired
    private  UserService userService;

    @Autowired
    private ChatMessageRepository chatMessageRepository;

    @Autowired
    private ChatMessageService chatMessageService;


    @MessageMapping("/chat")
    public void processMessage(@Payload MessageDTO messageDTO) {
        System.out.println("Получено сообщение: " + messageDTO);
        if (messageDTO.getContent() == null || messageDTO.getContent().isEmpty()) {
            System.out.println("Контент сообщения пустой!");
            return;
        }

        // Сохраняем сообщение в базу данных
        ChatMessage message = ChatMessage.builder()
                .senderType(messageDTO.getSenderType())
                .content(messageDTO.getContent())
                .senderId(messageDTO.getSenderId())
                .recipientId(messageDTO.getRecipientId())
                .timestamp(LocalDateTime.now())
                .senderType(messageDTO.getSenderType())
                .build();

        chatMessageRepository.save(message);


        String usernameRecipint = userService.getUserById(message.getRecipientId()).getUsername();


        String destination = "/queue/messages/" + message.getRecipientId();
        messagingTemplate.convertAndSendToUser(
                usernameRecipint,
                destination,
                message
        );

        // Отправляем уведомление отправителю (если нужно)
        if (message.getSenderType().equals("ADMIN")) {
            messagingTemplate.convertAndSendToUser(
                    message.getSenderId().toString(),
                    "/queue/notifications",
                    "Ваше сообщение доставлено пользователю."
            );
        } else {
            messagingTemplate.convertAndSendToUser(
                    message.getSenderId().toString(),
                    "/queue/notifications",
                    "Ваше сообщение доставлено администратору."
            );
        }
    }


    @GetMapping("/chat/admin")
    public String chatPageAdmin(@RequestParam("userId") Long userId, Model model) {
        model.addAttribute("userId", userId);
        model.addAttribute("adminId", userService.getCurrentUser().getId());
        model.addAttribute("role", userService.getCurrentUser().getRole());

        return "admin-chat";
    }

    @GetMapping("/chat/user")
    public String chatPageUser(@RequestParam("adminId") Long adminId, Model model) {
        model.addAttribute("adminId", adminId);
        model.addAttribute("sender", userService.getCurrentUser().getId() );
        model.addAttribute("role", userService.getCurrentUser().getRole());
        return "chat";
    }

    @GetMapping("/chat/notifications")
    public String notificationsPage(Model model){
        return "notifications";
    }

// нужно проверять


    @GetMapping("/api/conversations")
    @ResponseBody
    public List<ChatMessage> getListMessagesUsers(@RequestParam Long recipient){

        List<ChatMessage> listA = new ArrayList<>(chatMessageService.getChatUsers(recipient));
        List<ChatMessage> listB = new ArrayList<>(chatMessageService.getChatUsersTurnover(recipient));

        List<ChatMessage> listJoin = new ArrayList<>(listA);
        listJoin.addAll(listB);

        for (ChatMessage messages: listJoin) {
            System.out.println("Отправитель " + userService.getUserById(messages.getSenderId()).getUsername() + " Получатель " + userService.getUserById(messages.getRecipientId()).getUsername() + " Сообщение "+ messages.getContent());
        }

        listJoin.sort(Comparator.comparing(ChatMessage::getTimestamp));

        return listJoin;

    }


}
