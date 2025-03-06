package com.example.model;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;


@Data
@Entity
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Ticket  {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String description;

    private LocalDateTime createdAt;
    private LocalDateTime closedAt;

    @Enumerated(EnumType.STRING)
    private Status status;


    private Long adminId;

    private String location;

    @ManyToOne
    @JoinColumn
    private User user;

    @ManyToOne
    private Problem problems;

    @ManyToOne
    private Computer computer;


    private Long reportId;
}
