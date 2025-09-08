package com.SENA.DISTRIBUIDORA_LA_DORADA.Entity;

import com.SENA.DISTRIBUIDORA_LA_DORADA.Enums.PaymentFrequency;
import com.SENA.DISTRIBUIDORA_LA_DORADA.Enums.PaymentType;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.*;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

@Entity
@Table(name = "sales")
@JsonIgnoreProperties({"hibernateLazyInitializer", "handler"})
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Sale {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY) // 🔹 recomendado LAZY
    @JoinColumn(name = "client_id", nullable = false)
    private Client client;

    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "sale_date")
    private Date date = new Date(); // 🔹 inicializado por defecto

    @Enumerated(EnumType.STRING)
    @Column(name = "payment_type")
    private PaymentType paymentType;

    @Column(name = "total")
    private Double total;

    @Enumerated(EnumType.STRING)
    @Column(name = "payment_frequency")
    private PaymentFrequency paymentFrequency;

    @Column(name = "status", nullable = false)
    private String status = "COMPLETED"; // 🔹 valor por defecto

    // Relación con los detalles
    @OneToMany(mappedBy = "sale", cascade = CascadeType.ALL, fetch = FetchType.LAZY, orphanRemoval = true)
    @JsonIgnoreProperties("sale")
    private List<SaleDetail> details = new ArrayList<>();

    // Relación con cuotas
    @OneToMany(mappedBy = "sale", cascade = CascadeType.ALL, orphanRemoval = true)
    private List<Installment> installments = new ArrayList<>();

    // Relación con entrega
    @OneToOne(mappedBy = "sale", cascade = CascadeType.ALL)
    private Delivery delivery;
}
