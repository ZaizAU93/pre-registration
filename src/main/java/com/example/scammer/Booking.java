package com.example.scammer;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;

@Entity
@Data
@Builder
@AllArgsConstructor
@NoArgsConstructor
public class Booking {
    @Id
    @GeneratedValue
    private Long id;
    @OneToOne
    @JsonIgnore
    @JoinColumn( unique = true)
    private TimeSlot timeSlot;
    private LocalDate receiptDate;             // p_receiptdate — дата получения
    private String customerName;               // p_customername — имя клиента
    private Integer purposeId;                 // p_purposeid — идентификатор цели
    private String info;                       // p_info — дополнительная информация
    private String phone;                      // p_phone — телефон клиента
    private Integer regCode;                    // p_regcode — регистрационный код

    private Integer userUid;
}