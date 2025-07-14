package com.SENA.DISTRIBUIDORA_LA_DORADA.Entity;

import com.SENA.DISTRIBUIDORA_LA_DORADA.Enums.PaymentFrequency;
import com.SENA.DISTRIBUIDORA_LA_DORADA.Enums.PaymentType;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.util.Date;
import java.util.List;

@Entity
@Table(name = "sales")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor

public class Sale {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @ManyToOne(fetch = FetchType.LAZY)
    @JoinColumn(name = "client_id" , nullable = false)
    private Client client;

    @Temporal(TemporalType.DATE)
    private Date date;

    @Enumerated(EnumType.STRING)
    @Column(name = "payment_type")
    private PaymentType paymentType;

    @Column(name = "total")
    private Double total;

    @Enumerated(EnumType.STRING)
    @Column(name = "payment_frequency")
    private PaymentFrequency paymentFrequency;

    // Relación con los detalles de la venta
    @OneToMany(mappedBy = "sale" , cascade = CascadeType.ALL , orphanRemoval = true)
    private List<SaleDetail> details;


    // Relación con cuotas si es a crédito
    @OneToMany(mappedBy = "sale" ,  cascade = CascadeType.ALL , orphanRemoval = true)
    private List<Installment> installments;

    // Relación con entrega
    @OneToOne(mappedBy = "sale" , cascade = CascadeType.ALL)
    private Delivery delivery;

}
