package com.example.scammer.DTO;

import jakarta.persistence.criteria.CriteriaBuilder;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
public class UserDTO {
   private Integer USERUID;
    private Integer USERTYPECODE;
    private String USERNAME;

    private String DBUSER;

    private Integer REGCODE;

    private Integer ACT;

    private Integer EMPLOYEEUID;
    private String RH_LOGIN;
    private String RH_PASS;
    private Byte FACSIMILE;



}
