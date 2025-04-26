package com.example.scammer.controllers;

import com.example.scammer.Registrar;
import com.example.scammer.repo.RegistratorRepo;
import com.example.scammer.repo.TimeSlotRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.util.List;

@Controller
@RequestMapping("/api/registrars/filter")
public class RegistrarFilterController {

    @Autowired
    private RegistratorRepo registrarRepository;

    @Autowired
    private TimeSlotRepository timeSlotRepository;

    // 1. Ищет регистратора по фамилии
    @GetMapping("/by-surname")
    @ResponseBody
    public ResponseEntity<List<Registrar>> getRegistrarsBySurname(@RequestParam String surname) {
        System.out.println("1 фильтр");
        List<Registrar> registrars = registrarRepository.findBySurname(surname);
        return ResponseEntity.ok(registrars);
    }

    // 2. Выводит список регистратор у которых есть свободные таймслоты на определенную дату
    @GetMapping("/free-on-date")
    @ResponseBody
    public ResponseEntity<List<Registrar>> getRegistrarsWithFreeSlotsOnDate(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date) {
        System.out.println("2 фильтр");
        List<Registrar> registrars = timeSlotRepository.findRegistrarsWithFreeSlotsOnDate(date);
        return ResponseEntity.ok(registrars);
    }

    // 3. Выводит список регистраторов у которых есть свободные таймслоты на определенный промежуток времени на определенную дату
    @GetMapping("/free-in-interval")
    @ResponseBody
    public ResponseEntity<List<Registrar>> getRegistrarsWithFreeSlotsInInterval(
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.DATE) LocalDate date,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.TIME) LocalDateTime startTime,
            @RequestParam @DateTimeFormat(iso = DateTimeFormat.ISO.TIME) LocalDateTime endTime) {
        System.out.println("3 фильтр");
        // Объединяем дату и время, чтобы получить полные LocalDateTime
        LocalDateTime startDateTime = LocalDateTime.of(date, startTime.toLocalTime());
        LocalDateTime endDateTime = LocalDateTime.of(date, endTime.toLocalTime());

        List<Registrar> registrars = timeSlotRepository.findRegistrarsWithFreeSlotsInInterval(date, startDateTime, endDateTime);
        return ResponseEntity.ok(registrars);
    }
}
