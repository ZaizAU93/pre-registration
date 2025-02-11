package com.example.repo;

import com.example.model.Computer;
import com.example.model.Status;
import com.example.model.Ticket;
import com.example.model.User;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.List;
import java.util.Optional;

@Repository
public interface TicketRepo extends JpaRepository<Ticket, Long> {

    Ticket getTicketById(Long id);

    @Query("SELECT t FROM Ticket t WHERE t.user.id = :userId AND t.status = :status")
    List<Optional<Ticket>> findTicketByUserIdAndStatus(@Param("userId") Long userId, @Param("status") Status status);

    List<Ticket> findByUser(User user);

    @Query("UPDATE Ticket t SET t.status =: status WHERE t.id =: id ")
    Ticket jobs(@Param("status") Status status, @Param("id") Long id);

}

