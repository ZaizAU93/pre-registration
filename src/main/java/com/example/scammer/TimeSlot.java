package com.example.scammer;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Data
@JsonIgnoreProperties("timeSlots")
public class TimeSlot {
    @Id
    @GeneratedValue
    private Long id;

    private LocalDate data;

    @ManyToOne
    @JsonIgnore
    private Registrar registrar;

    private LocalDateTime startTime;
    private LocalDateTime endTime;
    private boolean isFree;

    @OneToOne(mappedBy = "timeSlot")
    private Booking booking; // если есть
}