package com.example.scammer.DTO;

import lombok.Data;

@Data
public class UpdateRegistrarRequest {
    private Long preentryId;
    private String newRegCode;
}
