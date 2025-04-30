package com.example.scammer;

import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.OneToOne;
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
    private TimeSlot timeSlot;
    private LocalDate receiptDate;             // p_receiptdate — дата получения
    private String customerName;               // p_customername — имя клиента
    private Integer purposeId;                 // p_purposeid — идентификатор цели
    private String info;                       // p_info — дополнительная информация
    private String phone;                      // p_phone — телефон клиента
    private String regCode;                    // p_regcode — регистрационный код
}