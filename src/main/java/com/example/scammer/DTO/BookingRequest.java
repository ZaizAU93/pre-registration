package com.example.scammer.DTO;

import lombok.Data;

@Data
public class BookingRequest {
    private Long timeSlot;
    private String customerName;
    private int purposeId;
    private String info;
    private String phone;

}