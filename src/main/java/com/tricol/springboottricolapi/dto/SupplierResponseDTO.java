package com.tricol.springboottricolapi.dto;

import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

import java.time.LocalDateTime;

@Data
@NoArgsConstructor
@AllArgsConstructor

public class SupplierResponseDTO {

    private Long id;
    private String raisonSociale;
    private String address;
    private String city;
    private String ice;
    private String contactPerson;
    private String email;
    private String phone;
    private LocalDateTime createdAt;
    private LocalDateTime updatedAt;
}
