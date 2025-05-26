package com.example.scammer.controllers;

import com.example.scammer.DTO.RegistrarDTO;
import com.example.scammer.Registrar;
import com.example.scammer.DayComment;
import com.example.scammer.TimeSlot;
import com.example.scammer.repo.*;
import com.example.scammer.service.TimeSlotService;
import com.example.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.dao.EmptyResultDataAccessException;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.messaging.handler.annotation.MessageMapping;
import org.springframework.messaging.simp.SimpMessagingTemplate;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.*;
import org.springframework.web.server.ResponseStatusException;
import java.security.Principal;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.time.format.DateTimeParseException;
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
    private PreEntryRepository preEntryRepository;

    @Autowired
    private DayCommentRepository dayCommentRepository;

    public RegistrarController(RegistratorRepo registrarRepository, TimeSlotRepository timeSlotRepository) {
        this.registrarRepository = registrarRepository;
        this.timeSlotRepository = timeSlotRepository;
    }

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

        return "2";
    }




    @PostMapping("/free-slots")
    public String saveFreeSlot(@RequestParam String startTime,
                               @RequestParam String endTime,
                               @RequestParam LocalDate date) {
        Registrar registrar = registrarRepository.findByUserIdReg(userService.getCurrentUser().getId())
                .orElseThrow(() -> new IllegalArgumentException("Registrar не найден"));

       // LocalDate localDate = LocalDate.now();
        LocalDate localDate = date;
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


    // @GetMapping("/registrar/free-slots-data")
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
        return "tableSvod_11"; // название шаблона
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
        return Map.of("comment", commentText,
                 "id", commentOpt.get().getId().toString());
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

    @GetMapping("/registrar/free-slots-data")
    @ResponseBody
    public Map<String, Object> getCalendarData(
            @RequestParam int year,
            @RequestParam int month
    ) {
        Long userId = userService.getCurrentUser().getId();
        Registrar registrar = registrarRepository.findByUserIdReg(userId)
                .orElseThrow(() -> new IllegalArgumentException("Регистратор не найден"));

        LocalDate startDate = LocalDate.of(year, month, 1);
        LocalDate endDate = startDate.withDayOfMonth(startDate.lengthOfMonth());

        Map<String, List<TimeSlotDTO>> slots = timeSlotRepository
                .findByRegistrarAndDataBetween(registrar, startDate, endDate)
                .stream()
                .collect(Collectors.groupingBy(
                        ts -> ts.getData().toString(),
                        Collectors.mapping(ts -> new TimeSlotDTO(
                                ts.getStartTime(),
                                ts.getEndTime(),
                                ts.isFree()
                        ), Collectors.toList())
                ));

        Map<String, DayCommentDTO> comments = dayCommentRepository
                .findByRegistrarAndDateBetween(registrar, startDate, endDate)
                .stream()
                .collect(Collectors.toMap(
                        dc -> dc.getDate().toString(),
                        dc -> new DayCommentDTO(dc.getId(), dc.getCommentText())
                ));

        return Map.of(
                "slots", slots,
                "comments", comments
        );
    }

    public record DayCommentDTO(Long id, String text) {}

    // DTO для временных слотов
    public record TimeSlotDTO(LocalDateTime startTime, LocalDateTime endTime, boolean isFree) {}
    /*
    @GetMapping("/{registrarId}/day-comment")
    public ResponseEntity<Map<String, String>> getDayComment(@PathVariable Long registrarId, @RequestParam String date) {
        Optional<Registrar> regOpt = registrarRepository.findById(registrarId);
        if (!regOpt.isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Collections.singletonMap("comment", "Регистратор не найден"));
        }
        Registrar registrar = regOpt.get();
        LocalDate day = LocalDate.parse(date);
        Optional<DayComment> commentOpt = dayCommentRepository.findByRegistrarAndDate(registrar, day);
        String commentText = commentOpt.map(DayComment::getCommentText).orElse("Нет комментария");
        return ResponseEntity.ok(Collections.singletonMap("commentText", commentText));
    }
*/

    @GetMapping("/{registrarId}/day-comment")
    public ResponseEntity<Map<String, String>> getDayComment(@PathVariable Long registrarId, @RequestParam String date) {
        Optional<Registrar> regOpt = registrarRepository.findById(registrarId);
        if (!regOpt.isPresent()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Collections.singletonMap("comment", "Регистратор не найден"));
        }
        Registrar registrar = regOpt.get();
        // Используем форматтер, чтобы принимать даты без ведущих нулей
        DateTimeFormatter formatter = DateTimeFormatter.ofPattern("yyyy-M-d");
        LocalDate day;
        try {
            day = LocalDate.parse(date, formatter);
        } catch (DateTimeParseException e) {
            // Обработка неправильного формата даты
            return ResponseEntity.badRequest().body(Collections.singletonMap("error", "Некорректный формат даты. Используйте формат yyyy-M-d."));
        }
        Optional<DayComment> commentOpt = dayCommentRepository.findByRegistrarAndDate(registrar, day);
        String commentText = commentOpt.map(DayComment::getCommentText).orElse("Нет комментария");
        return ResponseEntity.ok(Collections.singletonMap("commentText", commentText));
    }

    @GetMapping("/list")
    @ResponseBody
    public List<RegistrarDTO> getRegistrars() {
        List<Registrar> registrars = registrarRepository.findAll();
        return registrars.stream()
                .map(r -> new RegistrarDTO(r.getRegCode(), r.getName(), r.getSurname()))
                .collect(Collectors.toList());
    }


    @GetMapping("/name/{regCode}")
    @ResponseBody
    public RegistrarDTO getRegistrarName(@PathVariable String regCode) {
        Registrar registrar = registrarRepository.findByRegCode(regCode);
        if (registrar != null) {
            return new RegistrarDTO(registrar.getRegCode(), registrar.getName(), registrar.getSurname());
        } else {
            throw new ResponseStatusException(HttpStatus.NOT_FOUND);
        }
    }


    @PostMapping("/daycomment/delete")
    @ResponseBody
    public Map<String, String> deleteDayComment(@RequestBody Map<String, String> payload) {
        String idStr = payload.get("id");
        if (idStr == null || idStr.isEmpty()) {
            return Map.of("status", "error", "message", "Нет ID для удаления");
        }
        try {
            Long commentId = Long.parseLong(idStr);
            // Удаление комментария по id
            dayCommentRepository.deleteById(commentId);
            return Map.of("status", "success");
        } catch (NumberFormatException e) {
            return Map.of("status", "error", "message", "Некорректный формат ID");
        } catch (EmptyResultDataAccessException e) {
            return Map.of("status", "error", "message", "Комментарий не найден");
        }
    }


}
