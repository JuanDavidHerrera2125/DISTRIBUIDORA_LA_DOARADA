package com.SENA.DISTRIBUIDORA_LA_DORADA.Entity;


import com.SENA.DISTRIBUIDORA_LA_DORADA.Enums.ClientType;
import com.fasterxml.jackson.annotation.JsonIgnore;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.*;


import java.util.Date;
import java.util.List;

@Getter
@Setter
@Entity
@Table (name = "clients")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@NoArgsConstructor
@AllArgsConstructor
@Builder

public class Client {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private String address;

    @Column(unique = true , nullable = false)
    private String phone;

    private String email;

    @Enumerated(EnumType.STRING)
    @Column(name = "client_type")
    private ClientType clientType;


    @Temporal(TemporalType.DATE)
    @Column(name = "register_date")
    private Date registerDate = new Date();

    // Relación 1:N con Venta

    @JsonIgnore
    @OneToMany(mappedBy = "client" , cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Sale> sales;

    // Relación 1:N con Entrega

    @JsonIgnore
    @OneToMany(mappedBy = "client" , cascade = CascadeType.ALL , orphanRemoval = true)
    private List<Delivery> deliveries;

    // Client.java
    @Column(name = "active")
    private Boolean active = true; // ✅ Valor por defecto
}
