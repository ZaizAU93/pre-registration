package com.example.scammer;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.time.LocalDateTime;

@Entity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Booking {
    @Id
   // @GeneratedValue
    @GeneratedValue(strategy = GenerationType.SEQUENCE, generator = "book_seq")
    @SequenceGenerator(name = "book_seq", sequenceName = "book_seq", allocationSize = 1)
    private Long id;
    @OneToOne
    @JsonIgnore
    @JoinColumn( unique = true)
    private TimeSlot timeSlot;
    private LocalDateTime receiptDate;             // p_receiptdate — дата получения
    private String customerName;               // p_customername — имя клиента
    private Integer purposeId;                 // p_purposeid — идентификатор цели
    private String info;                       // p_info — дополнительная информация
    private String phone;                      // p_phone — телефон клиента
    private Integer regCode;                    // p_regcode — регистрационный код

    private Integer userUid;

}