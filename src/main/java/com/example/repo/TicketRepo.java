package com.example.repo;

import com.example.model.Status;
import com.example.model.Ticket;
import com.example.model.User;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TicketRepo extends JpaRepository<Ticket, Long> {

    Ticket getTicketById(Long id);
    List<Ticket> getTicketByUser(User user);

    @Query("SELECT t FROM Ticket t WHERE t.user = :user AND t.status = :status")
    List<Optional<Ticket>> findTicketByUserIdAndStatus(@Param("user") User user, @Param("status") Status status);

    List<Ticket> findByUserId( Long id);


    @Modifying
    @Transactional
    @Query("UPDATE Ticket t SET t.status = :status, t.adminId = :admin WHERE t.id = :id")
    void jobs(@Param("status") Status status, @Param("admin") Long admin, @Param("id") Long id);

    @Transactional
    @Query("SELECT t FROM Ticket t WHERE t.status = :status AND t.adminId =:admin")
    List<Optional<Ticket>> ticketStatus(@Param("status") Status status, @Param("admin") Long admin);


    @Modifying
    @Transactional
    @Query("UPDATE Ticket e SET e.reportId = ?1 WHERE e.id = ?2")
    void updateReport(Long reportId, Long id);
}

