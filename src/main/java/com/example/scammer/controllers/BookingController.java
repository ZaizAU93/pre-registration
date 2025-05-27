package com.example.scammer.controllers;

import com.example.model.User;
import com.example.scammer.Booking;
import com.example.scammer.DTO.BookingRequest;
import com.example.scammer.Registrar;
import com.example.scammer.Request;
import com.example.scammer.TimeSlot;
import com.example.scammer.repo.*;
import com.example.scammer.service.BookingService;
import com.example.scammer.service.OraclePackageService;
import com.example.scammer.service.TimeSlotService;
import com.example.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;


import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.time.format.DateTimeFormatter;
import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

@Controller
@RequestMapping("/booking")
public class BookingController {

    @Autowired
    private BookingService bookingService;

    @Autowired
    private TimeSlotService timeSlotService;

    @Autowired
    private PreEntryRepository preEntryRepository;

    @Autowired
    private UserService userService;

    @Autowired
    private BookingRepository bookingRepository;

    @Autowired
    private RegistratorRepo registratorRepo;

    @Autowired
    private RequestRepository requestRepository;
    @Autowired
    private TimeSlotRepository timeSlotRepository;

    @PostMapping("/save")
    public ResponseEntity<?> save(@RequestBody BookingRequest request) {
        Long timeSlot = request.getTimeSlot();
        String customerName = request.getCustomerName();
        int purposeId = request.getPurposeId();
        String info = request.getInfo();
        String phone = request.getPhone();

        try {
            Optional<TimeSlot> slotOptional = timeSlotService.findById(timeSlot);
            if (!slotOptional.isPresent()) {
                return ResponseEntity.badRequest().body("Слот не найден");
            }


            TimeSlot slot = slotOptional.get();
            Booking booking = new Booking();

            // Получаем regCode из объекта Registrar
            String regCode = slot.getRegistrar().getRegCode();


            if (regCode != null && regCode.length() > 1) {
                try {
                    // Удаляем первый символ и преобразуем в Integer
                //    String numericPart = regCode.substring(1);
                //    int regCodeInt = Integer.parseInt(numericPart);
                    booking.setRegCode(Integer.parseInt(regCode));
                } catch (NumberFormatException e) {
                    // Обработка ошибки: часть после первого символа не является числом
                    throw new IllegalArgumentException("Invalid regCode format: " + regCode);
                }
            } else {
                // Обработка случая, когда regCode отсутствует или слишком короткий
                throw new IllegalArgumentException("regCode is null or too short: " + regCode);
            }

            User user = userService.getCurrentUser();

            booking.setCustomerName(customerName);
            booking.setInfo(info);
            booking.setPhone(phone);
            booking.setTimeSlot(slot);
            booking.setReceiptDate(slot.getStartTime());
            booking.setPurposeId(purposeId);
            booking.setUserUid(user.getUserUID());
            bookingService.save(booking);


           // oraclePackageService.callAddEntryProcedure(booking);
        //    preEntryRepository.addPreEntry(booking);
             int uidPrentry =  preEntryRepository.addPreEntryAPIExemple(booking);

             timeSlotService.setPrentryUid(slot.getId(), uidPrentry);
             timeSlotService.updateStateTimeSlot(slot, false);

            System.out.println("айди заказа: " + uidPrentry);


            return ResponseEntity.ok().build();


        } catch (Exception e) {
            return ResponseEntity.status(500).body("Ошибка: " + e.getMessage());
        }
    }


    @GetMapping("/registrar/booking-details")
    @ResponseBody
    public Map<String, Object> getBookingDetails(
            @RequestParam String date,
            @RequestParam String startTime,
            @RequestParam String endTime
    ) {
        System.out.println("дата пришла " + startTime);

        Long userId = userService.getCurrentUser().getId();
        Registrar registrar = registratorRepo.findByUserIdReg(userId)
                .orElseThrow(() -> new IllegalArgumentException("Регистратор не найден"));

        String[] parts = startTime.split(",");
        if (parts.length != 5) {
            throw new IllegalArgumentException("Некорректный формат даты");
        }

        int year = Integer.parseInt(parts[0]);
        int month = Integer.parseInt(parts[1]);
        int day = Integer.parseInt(parts[2]);
        int hour = Integer.parseInt(parts[3]);
        int minute = Integer.parseInt(parts[4]);

        LocalDateTime startDateTime = LocalDateTime.of(year, month, day, hour, minute);
        System.out.println("Обработанное время: " + startDateTime);
        System.out.println("итоговое время в запрос " + startDateTime);

        Optional<Booking> bookingOpt = bookingRepository.findByReceiptDateAndRegCode(
                startDateTime, Integer.parseInt(registrar.getRegCode()));

        Optional<Request> requestOpt = requestRepository.findByPreentryid(timeSlotRepository.findByBooking(bookingOpt.get()).get().getPrentryUid());




        if (requestOpt.isPresent()) {
            Request request  = requestOpt.get();
            String purposeStr = null;
            if (request.getPurposeid() == 2) {
                purposeStr = "Государственная регистрация";
            } else {
                purposeStr = "Удостоверение сделки";
            }



            Map<String, Object> data = new HashMap<>();
            data.put("startTime", request.getReceiptdate().toString().substring(0, 16));
            data.put("customerName", request.getCustomername());
            data.put("purposeId", purposeStr);
            data.put("info", request.getInfo());
            data.put("phone", request.getPhone());
            data.put("regCode", registratorRepo.findByRegCode(String.valueOf(request.getRegcode())).getName());
            data.put("userUid", request.getDatein().toString().substring(0, 16));
            return data;
        } else {
            Map<String, Object> data = new HashMap<>();
            data.put("startTime", startTime);
            data.put("customerName", "Нет данных");
            data.put("purposeId", null);
            data.put("info", null);
            data.put("phone", null);
            data.put("regCode", null);
            data.put("userUid", null);
            return data;
        }
    }




    @GetMapping("/request-details")
    @ResponseBody
    public Map<String, Object> getRequestDetails(
            @RequestParam String id

    ) {

        Optional<Request> requestOpt = requestRepository.findByPreentryid(timeSlotRepository.findById(Long.parseLong(id)).get().getPrentryUid());


        if (requestOpt.isPresent()) {
            Request request  = requestOpt.get();
            String purposeStr = null;
            if (request.getPurposeid() == 2) {
                purposeStr = "Государственная регистрация";
            } else {
                purposeStr = "Удостоверение сделки";
            }

            Map<String, Object> data = new HashMap<>();
            data.put("startTime", request.getReceiptdate().toString().substring(0, 16));
            data.put("customerName", request.getCustomername());
            data.put("purposeId", purposeStr);
            data.put("info", request.getInfo());
            data.put("phone", request.getPhone());
            data.put("regCode", registratorRepo.findByRegCode(String.valueOf(request.getRegcode())).getName());
            data.put("userUid", request.getDatein().toString().substring(0, 16));
            return data;
        } else {
            Map<String, Object> data = new HashMap<>();
            data.put("startTime", "Нет данных");
            data.put("customerName", "Нет данных");
            data.put("purposeId", null);
            data.put("info", null);
            data.put("phone", null);
            data.put("regCode", null);
            data.put("userUid", null);
            return data;
        }
    }


}
