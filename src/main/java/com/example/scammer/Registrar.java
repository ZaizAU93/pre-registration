package com.example.scammer;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.util.List;

@Entity
@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class Registrar {
    @Id
    @GeneratedValue
    private Long id;
    private String name;

    private String surname;

    private String fathername;

    private Long userIdReg;

    @OneToMany(mappedBy = "registrar", cascade = CascadeType.ALL)
    private List<TimeSlot> timeSlots;
}