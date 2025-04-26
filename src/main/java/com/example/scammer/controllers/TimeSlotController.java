package com.example.scammer.controllers;

import com.example.scammer.Registrar;
import com.example.scammer.repo.RegistratorRepo;
import com.example.scammer.service.RegistratorService;
import com.example.scammer.service.TimeSlotService;
import com.example.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalTime;
import java.util.Optional;

@Controller
@RequestMapping("/timeslots")
public class TimeSlotController {

    private final TimeSlotService timeSlotService;
    private final RegistratorRepo registrarRepository;

    @Autowired
    private UserService userService;

    @Autowired
    private RegistratorService registratorService;
    public TimeSlotController(TimeSlotService timeSlotService, RegistratorRepo registrarRepository) {
        this.timeSlotService = timeSlotService;
        this.registrarRepository = registrarRepository;
    }

    @GetMapping("/create")
    public String showCreateForm(Model model) {
        model.addAttribute("registrars", registrarRepository.findAll());
        return "redistratorTime";
    }

    @PostMapping("/save-interval")
    public String saveInterval(
                               @RequestParam String date,
                               @RequestParam String startTime,
                               @RequestParam String endTime) {
        LocalDate localDate = LocalDate.parse(date);
        LocalTime start = LocalTime.parse(startTime);
        LocalTime end = LocalTime.parse(endTime);
        Optional<Registrar> registrarId = registrarRepository.findByUserIdReg(userService.getCurrentUser().getId());
        // Проверка правильности интервала
        if (start.isAfter(end) || start.equals(end)) {
            // Можно добавить обработку ошибок, например, redirect с ошибкой
            return "redirect:/timeslots/create?error=invalidInterval";
        }

        timeSlotService.createTimeSlotsFromInterval(registrarId.get().getId(), localDate, start, end);
        return "redirect:/registrar/free-slots";
    }

}
