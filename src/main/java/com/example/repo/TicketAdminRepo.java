package com.example.repo;

import com.example.model.TicketAdmin;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface TicketAdminRepo extends JpaRepository<TicketAdmin, Long> {

    Optional<TicketAdmin> findById(Long id);

    @Modifying
    @Transactional
    @Query("UPDATE TicketAdmin e SET e.rupAdmin = ?2 WHERE e = ?1")
    void updateRupTicketAdmin(TicketAdmin ticketAdmin, Long id);

}
