package com.example.scammer.service;

import com.example.scammer.Registrar;
import com.example.scammer.TimeSlot;
import com.example.scammer.repo.RegistratorRepo;
import com.example.scammer.repo.TimeSlotRepository;
import com.example.service.UserService;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.List;
import java.util.Optional;

@Service
public class TimeSlotService {

    private final RegistratorRepo registrarRepository;
    private final TimeSlotRepository timeSlotRepository;
    @PersistenceContext
    private EntityManager entityManager;
    @Autowired
    private UserService userService;

    @Autowired
    private BookingService bookingService;

    public TimeSlotService(RegistratorRepo registrarRepository, TimeSlotRepository timeSlotRepository) {
        this.registrarRepository = registrarRepository;
        this.timeSlotRepository = timeSlotRepository;
    }

    @Transactional
    public void createTimeSlotsFromInterval(Long registrarId, LocalDate date, LocalTime start, LocalTime end) {
        Registrar registrar = registrarRepository.findById(registrarId)
                .orElseThrow(() -> new IllegalArgumentException("Registrar not found"));


        LocalTime currentTime = start;
        LocalTime endTime = end;

            LocalDateTime slotStart = LocalDateTime.of(date, currentTime);
            LocalDateTime slotEnd = LocalDateTime.of(date, endTime);


            TimeSlot timeSlot = new TimeSlot();
            timeSlot.setData(date);
            timeSlot.setRegistrar(registrar);
            timeSlot.setStartTime(slotStart);
            timeSlot.setEndTime(slotEnd);
            timeSlot.setFree(true);



        timeSlotRepository.save(timeSlot);
    }




    public List<TimeSlot> getTimeSlotUser(Long id) {
        String jpql = "SELECT t FROM TimeSlot t WHERE t.registrar.id =:id and t.isFree =:isBoll";
        TypedQuery<TimeSlot> query = entityManager.createQuery(jpql, TimeSlot.class);
        query.setParameter("id", id );
        query.setParameter("isBoll", true );

        return query.getResultList(); // Получение списка результатов
    }

    public List<TimeSlot> getTimeSlotAllUser() {
        String jpql = "SELECT t FROM TimeSlot t WHERE t.isFree =:isBoll";
        TypedQuery<TimeSlot> query = entityManager.createQuery(jpql, TimeSlot.class);
        query.setParameter("isBoll", true );

        return query.getResultList(); // Получение списка результатов
    }



    public void blockTimeSlot(Long timeSlotId) {
        TimeSlot slot = timeSlotRepository.findById(timeSlotId).orElseThrow();
        // Можно добавить статус "заблокировано"
        // например, установить isFree в false и пометить как заблокировано
        slot.setFree(false);
        // или добавить отдельное поле статус
        timeSlotRepository.save(slot);
    }

    public Optional<TimeSlot> findById(Long id){
        return timeSlotRepository.findById(id);
    }


    public void setPrentryUid(Long id, Integer uid){
        timeSlotRepository.updateUidPrentry(id , uid);
    }


}
