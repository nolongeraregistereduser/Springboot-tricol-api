package com.tricol.springboottricolapi.dto;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDate;
import java.util.UUID;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder


public class ErrorResponse {

    private LocalDate timestamp;
    private int status;
    private String error;
    private String message;
    private String path;

}
