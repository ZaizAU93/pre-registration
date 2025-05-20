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

    @Column(name = "regCode", unique = true)
    private String regCode;
    private String name;

    private String surname;

    private String fathername;
    @Column(name = "USERIDREG")
    private Long userIdReg;

    @OneToMany(mappedBy = "registrar", cascade = CascadeType.ALL)
    private List<TimeSlot> timeSlots;

}