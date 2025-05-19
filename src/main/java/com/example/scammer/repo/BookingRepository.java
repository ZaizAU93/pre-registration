package com.example.scammer.repo;

import com.example.scammer.Booking;
import jakarta.transaction.Transactional;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import java.util.Optional;

@Repository
public interface BookingRepository extends JpaRepository<Booking, Long> {

    Optional<Booking> findById(Long id);

    @Transactional
    @Modifying
    @Query("DELETE FROM Booking b WHERE b.timeSlot.id = :id ")
    Integer deleteBooking(@Param("id") Long id);



}
