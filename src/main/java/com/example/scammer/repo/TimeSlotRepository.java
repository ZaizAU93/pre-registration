package com.example.scammer.repo;

import com.example.scammer.Registrar;
import com.example.scammer.Request;
import com.example.scammer.TimeSlot;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Modifying;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Date;
import java.util.List;
import java.util.Optional;

public interface TimeSlotRepository extends JpaRepository<TimeSlot, Long> {

    TimeSlot findByRegistrarIdAndStartTime(Long registrarId, LocalDateTime time);

    List<TimeSlot> findByRegistrar(Registrar registrar);
    Optional<TimeSlot> findById(Long id);

    Optional<TimeSlot> findByPrentryUid(Integer id);

    @Modifying
    @Transactional
    @Query("UPDATE TimeSlot e SET e.isFree = ?2 WHERE e.id = ?1")
    void updateIsFreeById(@Param("id") Long id, @Param("isFree") Boolean isFree);


    @Query("SELECT DISTINCT t.registrar FROM TimeSlot t WHERE t.isFree = true AND t.data = :date")
    List<Registrar> findRegistrarsWithFreeSlotsOnDate(@Param("date") LocalDate date);

    @Query("SELECT DISTINCT t.registrar FROM TimeSlot t WHERE t.isFree = true AND t.data = :date AND " +
            "t.startTime >= :startTime AND t.endTime <= :endTime")
    List<Registrar> findRegistrarsWithFreeSlotsInInterval(@Param("date") LocalDate date,
                                                          @Param("startTime") LocalDateTime startTime,
                                                          @Param("endTime") LocalDateTime endTime);


    @Modifying
    @Transactional
    @Query("UPDATE TimeSlot e SET e.prentryUid = ?2 WHERE e.id = ?1")
    void updateUidPrentry(@Param("id") Long id, @Param("isFree") int uid);


    List<TimeSlot> findByRegistrarAndDataBetween(
            Registrar registrar,
            LocalDate start,
            LocalDate end
    );


    @Modifying
    @Transactional
    @Query("DELETE TimeSlot e  WHERE e.id = ?1")
    void вуUidPrentry(@Param("id") Long id, @Param("isFree") int uid);

    /*
    @Transactional
    void  deleteTimeSlotByStartTimeAndEndTimeAndRegistrarAndData(LocalDateTime startTime, LocalDateTime endTime, Registrar registrar, LocalDate date);
    */

    Optional<TimeSlot> findByStartTimeAndEndTimeAndRegistrarAndData(LocalDateTime startTime, LocalDateTime endTime, Registrar registrar, LocalDate date);



    @Query("SELECT s FROM TimeSlot s WHERE "
            + "(:date IS NULL OR s.data = :date) AND "
            + "(:startTime IS NULL OR s.startTime >= :startTime) AND "
            + "(:endTime IS NULL OR s.endTime <= :endTime) AND "
            + "(:name IS NULL OR s.registrar.name LIKE %:name%) "
            + "ORDER BY s.data DESC")
    List<TimeSlot> findByFilters(
            @Param("date") LocalDate date,
            @Param("startTime") LocalDateTime startTime,
            @Param("endTime") LocalDateTime endTime,
            @Param("name") String name
    );




}
