package com.example.scammer.DTO;

import com.example.scammer.TimeSlot;
import lombok.Data;

import java.time.LocalDate;

@Data
public class BookingDto {
    private TimeSlot timeSlot;
    private LocalDate receiptDate;
    private String customerName;
    private int purposeId;
    private String info;
    private String phone;

}
