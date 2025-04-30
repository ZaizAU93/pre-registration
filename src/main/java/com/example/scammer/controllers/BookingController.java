package com.example.scammer.controllers;

import com.example.scammer.Booking;
import com.example.scammer.DTO.BookingDto;
import com.example.scammer.TimeSlot;
import com.example.scammer.service.BookingService;
import com.example.scammer.service.OraclePackageService;
import com.example.scammer.service.RegistratorService;
import com.example.scammer.service.TimeSlotService;
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
    private OraclePackageService oraclePackageService;

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
            booking.setCustomerName(customerName);
            booking.setInfo(info);
            booking.setPhone(phone);
            booking.setTimeSlot(slot);
            booking.setReceiptDate(slot.getData());
            booking.setPurposeId(purposeId);
            booking.setRegCode(slot.getRegistrar().getRegCode());
            bookingService.save(booking);


            oraclePackageService.callAddEntryProcedure(booking);

            return ResponseEntity.ok().build();


        } catch (Exception e) {
            return ResponseEntity.status(500).body("Ошибка: " + e.getMessage());
        }
    }

}
