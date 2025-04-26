package com.example.scammer.controllers;

import com.example.scammer.Booking;
import com.example.scammer.BookingRequest;
import com.example.scammer.TimeSlot;
import com.example.scammer.repo.BookingRepository;
import com.example.scammer.repo.TimeSlotRepository;
import com.example.scammer.service.NotificationService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;

import java.util.Optional;

@Controller
@RequestMapping("/api/admin")
public class AdminController {

    @Autowired
    private TimeSlotRepository timeSlotRepository;

    @Autowired
    private BookingRepository bookingRepository;

    @Autowired
    private NotificationService notificationService;


    @GetMapping("/page")
    public String getRegistratorPage(){
        return "scammer";
    }



    @PostMapping("/book")
    public ResponseEntity<?> bookTime(@RequestBody BookingRequest request) {
        Optional<TimeSlot> optionalSlot = timeSlotRepository.findById(Long.parseLong(request.getSlotId()));
        if (optionalSlot.isPresent()) {
            TimeSlot slot = optionalSlot.get();
            if (slot.isFree()) {
                Booking booking = new Booking();
                booking.setTimeSlot(slot);
                booking.setClientFirstName(request.getFirstName());
                booking.setClientLastName(request.getLastName());
                bookingRepository.save(booking);

                slot.setFree(false);
                timeSlotRepository.save(slot);

                notificationService.notifyBooking(slot.getRegistrar().getId());
                return ResponseEntity.ok().build();
            } else {
                return ResponseEntity.status(HttpStatus.CONFLICT).body("Время уже занято");
            }
        } else {
            return ResponseEntity.notFound().build();
        }
    }
}