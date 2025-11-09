package com.tricol.springboottricolapi.dto;

import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
import jakarta.validation.constraints.Size;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor

public class SupplierRequestDTO {
    
    @NotBlank(message = "the social reason is required")
    @Size(min = 3, max = 150, message = "the sogcial reason must be between 3 and 150 characters")
    private String raisonSociale;

    @Size(min =3 , max = 255, message = "the address must be between 3 and 255 characters")
    private String address;

    @Size(min =3 , max = 100, message = "the city must be between 3 and 100 characters")
    private String city;

    @Size(min =3 , max = 50, message = "the ice must be between 3 and 50 characters")
    private String ice;

    @Size(min =3 , max = 100, message = "the contact person must be between 3 and 100 characters")
    private String contactPerson;

    @Email(message = "the email must be a valid email address")
    @Size( max = 120, message = "the email must not exceed 120 characters")
    private String email;

    @Size(min =8 , max = 50, message = "the phone must be between 8 and 50 characters")
    private String phone;

}
