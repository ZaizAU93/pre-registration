package com.example.scammer;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.Data;

import java.time.LocalDate;

@Entity
@Data
public class DayComment {
    @Id
    @GeneratedValue
    private Long id;
    @ManyToOne
    @JoinColumn(name = "registrar_id")
    @JsonIgnore
    private Registrar registrar;
    @Column(name = "comment_date")
    private LocalDate date;
    private String commentText ;
}