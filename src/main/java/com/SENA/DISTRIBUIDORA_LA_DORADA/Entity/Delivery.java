package com.SENA.DISTRIBUIDORA_LA_DORADA.Entity;

import com.SENA.DISTRIBUIDORA_LA_DORADA.Enums.DeliveryStatus;
import jakarta.persistence.*;
import lombok.*;

import java.util.Date;

@Entity
@Table( name = "deliveries")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder

public class Delivery {

    @Id
    @GeneratedValue (strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "client_id" , nullable = false)
    private Client client;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "sale_id" , nullable = false , unique = true)
    private Sale sale;

    @Temporal(TemporalType.DATE)
    @Column(name = "scheduled_date")
    private Date scheduled_date;

    @Temporal(TemporalType.DATE)
    @Column(name = "delivered_date")
    private Date deliveryDate;

    @Enumerated(EnumType.STRING)
    private DeliveryStatus status;

    private String notes;


}
