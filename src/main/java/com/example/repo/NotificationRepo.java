package com.example.repo;

import com.example.model.Notification;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NotificationRepo extends JpaRepository<Notification, Long> {

    List<Notification> findByAdminId(Long idAdmin);

    List<Notification> findBySender(Long adSender);

}
