package com.example.repo;

import com.example.model.Notification;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;

@Repository
public interface NotificationRepo extends JpaRepository<Notification, Long> {

    List<Notification> findByAdminId(Long idAdmin);

    List<Notification> findBySender(Long adSender);
    @Modifying
    @Transactional
    @Query("delete Notification e WHERE e.adminId =:id")
    void deleteAllNotificationUser(@Param("id") Long id);



}
