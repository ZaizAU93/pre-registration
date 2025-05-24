package com.example.scammer.service;

import com.example.scammer.Booking;
import com.example.scammer.repo.BookingRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

@Service
public class BookingService {

    @Autowired
    private BookingRepository bookingRepository;

    public void save(Booking booking){
        bookingRepository.save(booking);

        System.out.println("данные формы " + booking.getCustomerName());
        System.out.println("данные формы " + booking.getInfo());
        System.out.println("данные формы " + booking.getPhone());
        System.out.println("данные формы " + booking.getPurposeId());
    }





}
