package com.example.scammer.controllers;

import com.example.model.User;
import com.example.scammer.Booking;
import com.example.scammer.TimeSlot;
import com.example.scammer.repo.PreEntryRepository;
import com.example.scammer.service.BookingService;
import com.example.scammer.service.OraclePackageService;
import com.example.scammer.service.TimeSlotService;
import com.example.service.UserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.*;


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

    @PostMapping("/save")
    public ResponseEntity<?> save(
            @RequestParam Long timeSlot,
            @RequestParam String customerName,
            @RequestParam int purposeId,
            @RequestParam String info,
            @RequestParam String phone) {


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
                    String numericPart = regCode.substring(1);
                    int regCodeInt = Integer.parseInt(numericPart);
                    booking.setRegCode(regCodeInt);
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
            booking.setReceiptDate(slot.getData());
            booking.setPurposeId(purposeId);
            booking.setUserUid(user.getUserUID());
            bookingService.save(booking);


           // oraclePackageService.callAddEntryProcedure(booking);
        //    preEntryRepository.addPreEntry(booking);
             int uidPrentry =  preEntryRepository.addPreEntryAPIExemple(booking);

             timeSlotService.setPrentryUid(slot.getId(), uidPrentry);


            System.out.println("айди заказа: " + uidPrentry);


            return ResponseEntity.ok().build();


        } catch (Exception e) {
            return ResponseEntity.status(500).body("Ошибка: " + e.getMessage());
        }
    }

}
