package com.example.scammer;

import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.springframework.stereotype.Component;

import java.util.Date;

@Builder
@AllArgsConstructor
@NoArgsConstructor
@Data
@Table(name = "users", schema = "rsds600")
@Entity
@Component
public class Users {
    @Id()
    //  @GeneratedValue(strategy = GenerationType.IDENTITY)
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "userss_seq")
    @SequenceGenerator(name = "userss_seq", sequenceName = "userss_seq", allocationSize = 1)
    @Column(name = "USERUID")
    private Long USERUID;

    @Column(name = "USERNAME")
    private String USERNAME;


    @Column(name = "regcode", unique = true)
    private Integer regcode;
}