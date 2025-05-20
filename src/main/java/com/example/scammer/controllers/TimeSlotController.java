package com.example.scammer.controllers;

import com.example.scammer.DayComment;
import com.example.scammer.Registrar;
import com.example.scammer.TimeSlot;
import com.example.scammer.repo.DayCommentRepository;
import com.example.scammer.repo.RegistratorRepo;
import com.example.scammer.repo.RequestRepository;
import com.example.scammer.repo.TimeSlotRepository;
import com.example.scammer.service.RegistratorService;
import com.example.scammer.service.TimeSlotService;
import com.example.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.format.annotation.DateTimeFormat;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.*;

import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Calendar;
import java.util.Date;
import java.util.List;
import java.util.Optional;

@Controller
@RequestMapping("/timeslots")
public class TimeSlotController {

    private final TimeSlotService timeSlotService;
    private final RegistratorRepo registrarRepository;

    @Autowired
    private TimeSlotRepository timeSlotRepository;

    @Autowired
    private DayCommentRepository dayCommentRepository;

    @Autowired
    private RequestRepository requestRepository;

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
/*
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
*/
@PostMapping("/timeslots/save-interval")
public String saveInterval(
        @RequestParam @DateTimeFormat(pattern = "yyyy-MM-dd") LocalDate date,
        @RequestParam @DateTimeFormat(pattern = "HH:mm") LocalTime startTime,
        @RequestParam @DateTimeFormat(pattern = "HH:mm") LocalTime endTime,
        @RequestParam Long registrarId,
        @RequestParam(required = false) String comment
) {
    Registrar registrar = registrarRepository.findById(registrarId)
            .orElseThrow(() -> new IllegalArgumentException("Регистратор не найден"));

    // Проверка, что endTime после startTime
    if (endTime.isBefore(startTime) || endTime.equals(startTime)) {
        throw new IllegalArgumentException("Время окончания должно быть после времени начала");
    }

    // Сохранение тайм-слота
    TimeSlot timeSlot = new TimeSlot();
    timeSlot.setData(date);
    timeSlot.setStartTime(LocalDateTime.of(date, startTime));
    timeSlot.setEndTime(LocalDateTime.of(date, endTime));
    timeSlot.setRegistrar(registrar);
    timeSlot.setFree(true);
    timeSlotRepository.save(timeSlot);

    // Обработка комментария
    if (StringUtils.hasText(comment)) {
        DayComment dayComment = dayCommentRepository.findByRegistrarAndDate(registrar, date)
                .orElseGet(() -> {
                    DayComment dc = new DayComment();
                    dc.setRegistrar(registrar);
                    dc.setDate(date);
                    return dc;
                });
        dayComment.setCommentText(comment.trim());
        dayCommentRepository.save(dayComment);
    }

    return "redirect:/registrar/free-slots";
}


    @GetMapping("/phones/check")
    @ResponseBody
    public ResponseEntity<Integer> checkingPhone(@RequestParam String phone) {
        Date currentDate = new Date();

        // Вычисляем дату три месяца назад
        Calendar cal = Calendar.getInstance();
        cal.setTime(currentDate);
        cal.add(Calendar.MONTH, -3);
        Date threeMonthsAgo = cal.getTime();

        // Вызов метода репозитория
        Integer count = requestRepository.findCountByPhoneAndDateRange(phone.replaceAll("[^0-9]", ""), threeMonthsAgo, currentDate);

        System.out.println("количество телефонов " + count);

        System.out.println("телефон: " +  phone.replaceAll("[^0-9]", ""));


        return ResponseEntity.ok(count);
    }


}
