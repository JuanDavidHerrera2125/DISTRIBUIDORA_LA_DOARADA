package com.SENA.DISTRIBUIDORA_LA_DORADA.Entity;


import com.SENA.DISTRIBUIDORA_LA_DORADA.Enums.ClientType;
import com.fasterxml.jackson.annotation.JsonIgnore;
import jakarta.persistence.*;
import lombok.*;


import java.util.Date;
import java.util.List;

@Getter
@Setter
@Entity
@Table (name = "clients")
@NoArgsConstructor
@AllArgsConstructor
@Builder

public class Client {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private String address;

    private String phone;

    private String email;

    @Enumerated(EnumType.STRING)
    @Column(name = "client_type")
    private ClientType Type;

    @Temporal(TemporalType.DATE)
    @Column(name = "register_date")
    private Date registerDate ;

    // Relación 1:N con Venta

    @JsonIgnore
    @OneToMany(mappedBy = "client" , cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Sale> sales;

    // Relación 1:N con Entrega

    @JsonIgnore
    @OneToMany(mappedBy = "client" , cascade = CascadeType.ALL , orphanRemoval = true)
    private List<Delivery> deliveries;

    public void setActive(boolean b) {
    }
}
