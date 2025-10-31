package com.tricol.springboottricolapi.entity;


import jakarta.persistence.*;
<<<<<<< HEAD
import jakarta.validation.constraints.Email;
import jakarta.validation.constraints.NotBlank;
=======
>>>>>>> 4266868df583093a50b81c01d3a290a7c54b3c73
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import java.time.LocalDateTime;
<<<<<<< HEAD
import java.util.ArrayList;
import java.util.List;
=======
>>>>>>> 4266868df583093a50b81c01d3a290a7c54b3c73


@Entity
@Table(name = "suppliers")
@Data
@NoArgsConstructor
@AllArgsConstructor

public class Supplier {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

<<<<<<< HEAD
    @NotBlank(message = "Raison sociale is mandatory")
    @Column(name = "raison_sociale", nullable = false, length = 150)
=======
    @Column(name = "raison_sociale", nullable = false, length = 200)
>>>>>>> 4266868df583093a50b81c01d3a290a7c54b3c73
    private String raisonSociale;

    @Column(name = "address", columnDefinition = "TEXT")
    private String address;

    @Column(name = "city", length = 100)
    private String city;

    @Column(name = "ice", length = 50)
    private String ice;

    @Column(name="contact_person", length = 100)
    private String contactPerson;

<<<<<<< HEAD
    @Email(message = "Email should be valid")
=======
>>>>>>> 4266868df583093a50b81c01d3a290a7c54b3c73
    @Column(name="email", length =120)
    private String email;

     @Column(name = "phone", length = 50)
    private String phone;

    @CreationTimestamp
    @Column(name = "created_at", updatable = false)
    private LocalDateTime createdAt;

    @UpdateTimestamp
    @Column(name = "updated_at")
    private LocalDateTime updatedAt;


<<<<<<< HEAD
    @OneToMany(mappedBy = "supplier", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<SupplierOrder> orders = new ArrayList<>();
=======
//    @OneToMany(mappedBy = "supplier", cascade = CascadeType.ALL, orphanRemoval = true)
//    private List<SupplierOrder> orders = new ArrayList<>();

>>>>>>> 4266868df583093a50b81c01d3a290a7c54b3c73

}
