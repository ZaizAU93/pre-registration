package com.example.scammer;

import com.fasterxml.jackson.annotation.*;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Data
@JsonIgnoreProperties("timeSlots")
public class TimeSlot {
    @Id
  //  @GeneratedValue
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "timeSlot_seq")
    @SequenceGenerator(name = "timeSlot_seq", sequenceName = "timeSlot_seq", allocationSize = 1)
    private Long id;

    private LocalDate data;

    @ManyToOne
    @JsonManagedReference
    private Registrar registrar;

    private LocalDateTime startTime;
    private LocalDateTime endTime;
    @JsonProperty("isFree")
    private boolean isFree;

    @OneToOne(mappedBy = "timeSlot")
    private Booking booking; // если есть

    private Integer prentryUid;

    private Integer  purposeCode;

}