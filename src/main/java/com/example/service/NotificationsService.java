package com.example.service;

import com.example.model.Notification;
import com.example.model.Status;
import com.example.model.Ticket;
import com.example.repo.NotificationRepo;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import jakarta.transaction.Transactional;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;

@Service
public class NotificationsService {

    @Autowired
    private NotificationRepo repo;

    @Autowired
    private UserService userService;


    public List<Notification> getAllNotificationsRecipient(Long id){
        return repo.findByAdminId(id);
    }


    public void saveNotifications(Notification notification){
        Notification saveNotifications = Notification.builder()
                .adminId(notification.getAdminId())
                .sender(notification.getSender())
                .localDate(LocalDate.now())
                .content(notification.getContent())
                .ticketId(notification.getTicketId())
                .actual(true)
                .build();

        repo.save(saveNotifications);
    }



    @PersistenceContext
    private EntityManager entityManager;
    public List<Ticket> getAllNotificationUser() {

        Long admin = userService.getCurrentUser().getId();

        String jpql = "SELECT t FROM Notification t WHERE t.adminId =:admin";
        TypedQuery<Ticket> query = entityManager.createQuery(jpql, Ticket.class);
        query.setParameter("admin",  admin);

        return query.getResultList(); // Получение списка результатов
    }


    public void deleteHistoryNotifications(Long id, Long user, Long ticket){

        System.out.println("id отправителя " + id);
        System.out.println("id получателя " + id);
        System.out.println("id тикета " + id);
        repo.deleteAllNotificationUser(id, user, ticket);
    }


}
