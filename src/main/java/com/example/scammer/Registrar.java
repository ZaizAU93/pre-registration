package com.example.scammer;

import com.fasterxml.jackson.annotation.JsonBackReference;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonManagedReference;
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
    @JsonBackReference
    private List<TimeSlot> timeSlots;

}