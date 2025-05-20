package com.example.scammer.controllers;
import com.example.scammer.Registrar;
import com.example.scammer.DayComment;
import com.example.scammer.TimeSlot;
import com.example.scammer.repo.*;
import com.example.scammer.service.TimeSlotService;
import com.example.service.UserService;
import org.apache.tomcat.util.net.openssl.ciphers.Authentication;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;

import java.security.Principal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Collections;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.stream.Collectors;

@Controller
@RequestMapping("/registrar")
public class RegistrarController {

    private final RegistratorRepo registrarRepository;
    private final TimeSlotRepository timeSlotRepository;

    @Autowired
    private BookingRepository bookingRepository;
    @Autowired
    private SimpMessagingTemplate messagingTemplate;
    @Autowired
    private UserService userService;

    @Autowired
    private TimeSlotService timeSlotService;

    @Autowired
    private PreEntryRepository preEntryRepository;

    @Autowired
    private DayCommentRepository dayCommentRepository;

    public RegistrarController(RegistratorRepo registrarRepository, TimeSlotRepository timeSlotRepository) {
        this.registrarRepository = registrarRepository;
        this.timeSlotRepository = timeSlotRepository;
    }
/*
    @GetMapping("/free-slots")
    public String showFreeSlotsForm(Model model) {
        Long userId = userService.getCurrentUser().getId();
        Optional<Registrar> registrarOpt = registrarRepository.findByUserIdReg(userId);
        List<TimeSlot> timeSlotList = Collections.emptyList();
        boolean hasTimeSlots = false;

        if (registrarOpt.isPresent()) {
            Long registrarId = registrarOpt.get().getId();
            timeSlotList = timeSlotService.getTimeSlotUser(registrarId);
            hasTimeSlots = !timeSlotList.isEmpty();
        }

        model.addAttribute("timeSlot", timeSlotList);
        model.addAttribute("hasTimeSlots", hasTimeSlots);
        return "createFreeSlots";
    }
*/
    @GetMapping("/free-slots")
    public String showTimeSlotsForm(Model model) {
    Long userId = userService.getCurrentUser().getId();
    Optional<Registrar> registrar = registrarRepository.findByUserIdReg(userId);

        Map<LocalDate, List<TimeSlot>> slotsByDate = timeSlotRepository.findByRegistrar(registrar.get())
                .stream()
                .collect(Collectors.groupingBy(TimeSlot::getData));

        Map<LocalDate, String> commentsByDate = dayCommentRepository.findByRegistrar(registrar.get())
                .stream()
                .collect(Collectors.toMap(DayComment::getDate, DayComment::getCommentText));

        model.addAttribute("slotsByDate", slotsByDate);
        model.addAttribute("commentsByDate", commentsByDate);
        model.addAttribute("registrarId", registrar.get().getId());
        model.addAttribute("hasTimeSlots", !slotsByDate.isEmpty());

        return "1";
    }






    @PostMapping("/free-slots")
    public String saveFreeSlot(@RequestParam String startTime,
                               @RequestParam String endTime) {
        Registrar registrar = registrarRepository.findByUserIdReg(userService.getCurrentUser().getId())
                .orElseThrow(() -> new IllegalArgumentException("Registrar не найден"));

        LocalDate localDate = LocalDate.now();
        LocalTime start = LocalTime.parse(startTime);
        LocalTime end = LocalTime.parse(endTime);

        LocalDateTime slotStart = LocalDateTime.of(localDate, start);
        LocalDateTime slotEnd = LocalDateTime.of(localDate, end);

        TimeSlot slot = new TimeSlot();
        slot.setData(localDate);
        slot.setRegistrar(registrar);
        slot.setStartTime(slotStart);
        slot.setEndTime(slotEnd);
        slot.setFree(true);
        timeSlotRepository.save(slot);

        return "redirect:/registrar/free-slots?success";
    }


    @GetMapping("/registrar/free-slots-data")
    @ResponseBody
    public Map<String, Object> getTimeSlotsData() {
        Long userId = userService.getCurrentUser().getId();
        Registrar registrar = registrarRepository.findByUserIdReg(userId)
                .orElseThrow(() -> new IllegalArgumentException("Регистратор не найден"));

        Map<LocalDate, List<TimeSlot>> slotsByDate = timeSlotRepository.findByRegistrar(registrar)
                .stream()
                .collect(Collectors.groupingBy(TimeSlot::getData));

        Map<LocalDate, String> commentsByDate = dayCommentRepository.findByRegistrar(registrar)
                .stream()
                .collect(Collectors.toMap(DayComment::getDate, DayComment::getCommentText));

        return Map.of(
                "slots", slotsByDate,
                "comments", commentsByDate
        );
    }


    // Отобразить всех регистраторов и их слоты
    @GetMapping
    public String listRegistrars(Model model, Principal principal) {
        List<Registrar> registrars = registrarRepository.findAll();
        model.addAttribute("registrars", registrars);
        System.out.println("строка запроса к rsds600: " + preEntryRepository.getUser(principal.getName()).getUSERNAME());
        return "tableSvod"; // название шаблона
    }

    // Выбрать время для назначения записи
    @PostMapping("/registrars/{timeSlotId}")
    @MessageMapping("/topic/slots")
    public ResponseEntity<Void> bookTimeSlot(@PathVariable Long timeSlotId) {
        timeSlotRepository.updateIsFreeById(timeSlotId, false);
        messagingTemplate.convertAndSend("/topic/slots", timeSlotId);
        return ResponseEntity.ok().build();
    }


    @PostMapping("/slots/{slotId}/cancel")
    public ResponseEntity<Void> bookTimeSlotTrue(@PathVariable Long slotId) {
        timeSlotRepository.updateIsFreeById(slotId, true);
        messagingTemplate.convertAndSend("/topic/slots", slotId);
        bookingRepository.deleteBooking(slotId);
        return ResponseEntity.ok().build();
    }

    @GetMapping("/daycomment")
    @ResponseBody
    public Map<String, String> getDayComment(@RequestParam LocalDate date) {
        Long userId = userService.getCurrentUser().getId();
        Registrar registrar = registrarRepository.findByUserIdReg(userId)
                .orElseThrow(() -> new IllegalArgumentException("Регистратор не найден"));

        Optional<DayComment> commentOpt = dayCommentRepository.findByRegistrarAndDate(registrar, date);
        String commentText = commentOpt.map(DayComment::getCommentText).orElse("");
        return Map.of("comment", commentText);
    }


    @PostMapping("/daycomment")
    @ResponseBody
    public void saveDayComment(@RequestBody DayCommentRequest request) {
        Long userId = userService.getCurrentUser().getId();
        Registrar registrar = registrarRepository.findByUserIdReg(userId)
                .orElseThrow(() -> new IllegalArgumentException("Регистратор не найден"));

        DayComment comment = dayCommentRepository.findByRegistrarAndDate(registrar, request.date())
                .orElseGet(() -> {
                    DayComment newComment = new DayComment();
                    newComment.setRegistrar(registrar);
                    newComment.setDate(request.date());
                    return newComment;
                });

        comment.setCommentText(request.comment());
        dayCommentRepository.save(comment);
    }


    // DTO для запроса
    public record DayCommentRequest(LocalDate date, String comment) {}

}
