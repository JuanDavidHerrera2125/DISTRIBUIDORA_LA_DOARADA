package com.SENA.DISTRIBUIDORA_LA_DORADA.Entity;


import com.SENA.DISTRIBUIDORA_LA_DORADA.Enums.ClientType;
import jakarta.persistence.*;
import lombok.*;


import java.util.Date;
import java.util.List;

@Entity
@Table (name = "clients")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder

public class Client {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private String address;

    private String ponhe;

    private String email;

    @Enumerated(EnumType.STRING)
    @Column(name = "client_type")
    private ClientType Type;

    @Temporal(TemporalType.DATE)
    @Column(name = "registration_date")
    private Date registrationDate ;

    // Relación 1:N con Venta

    @OneToMany(mappedBy = "client" , cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Sale> sales;

    // Relación 1:N con Entrega

    @OneToMany(mappedBy = "client" , cascade = CascadeType.ALL , orphanRemoval = true)
    private List<Delivery> deliveries;
}
