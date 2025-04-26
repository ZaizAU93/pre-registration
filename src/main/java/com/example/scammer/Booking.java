package com.example.scammer;

import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;
import lombok.Data;

@Entity
@Data
public class Booking {
    @Id
    @GeneratedValue
    private Long id;

    @OneToOne
    private TimeSlot timeSlot;

    private String clientFirstName;
    private String clientLastName;
}