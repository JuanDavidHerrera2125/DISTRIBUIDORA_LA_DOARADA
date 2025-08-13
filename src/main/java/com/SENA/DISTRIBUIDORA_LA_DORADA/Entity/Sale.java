package com.SENA.DISTRIBUIDORA_LA_DORADA.Entity;

import com.SENA.DISTRIBUIDORA_LA_DORADA.Enums.PaymentFrequency;
import com.SENA.DISTRIBUIDORA_LA_DORADA.Enums.PaymentType;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;
import jakarta.persistence.*;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

import java.time.LocalDateTime;
import java.util.ArrayList;
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

    // ✅ Usa java.util.Date con @Temporal
    @Temporal(TemporalType.TIMESTAMP)
    @Column(name = "sale_date")
    private Date date;

    @Enumerated(EnumType.STRING)
    @Column(name = "payment_type")
    private PaymentType paymentType;

    @Column(name = "total")
    private Double total;

    @Enumerated(EnumType.STRING)
    @Column(name = "payment_frequency")
    private PaymentFrequency paymentFrequency;

    // En Sale.java
    @Column(name = "status", nullable = false)
    private String status = "COMPLETED"; // Estado por defecto

    // Relación con los detalles de la venta
    @OneToMany(mappedBy = "sale" , cascade = CascadeType.ALL ,  fetch = FetchType.EAGER, orphanRemoval = true)
    @JsonIgnoreProperties("sale")  // ✅ Rompe la recursión hacia SaleDetail
    private List<SaleDetail> details = new ArrayList<>(); // ✅ Inicialización corregida


    // Relación con cuotas si es a crédito
    @OneToMany(mappedBy = "sale" ,  cascade = CascadeType.ALL , orphanRemoval = true)
    private List<Installment> installments = new ArrayList<>(); // ✅ Inicialización corregida

    // Relación con entrega
    @OneToOne(mappedBy = "sale" , cascade = CascadeType.ALL)
    private Delivery delivery;

    // Constructor adicional para inicializar listas
    public Sale(Client client, Date date, PaymentType paymentType, Double total,
                PaymentFrequency paymentFrequency, String status) {
        this.client = client;
        this.date = date;
        this.paymentType = paymentType;
        this.total = total;
        this.paymentFrequency = paymentFrequency;
        this.status = status;
        this.details = new ArrayList<>();
        this.installments = new ArrayList<>();
    }

    public Long getId() {
        return id;
    }

    public void setId(Long id) {
        this.id = id;
    }

    public Client getClient() {
        return client;
    }

    public void setClient(Client client) {
        this.client = client;
    }

    public Date getDate() {
        return date;
    }

    public void setDate(Date date) {
        this.date = date;
    }

    public PaymentType getPaymentType() {
        return paymentType;
    }

    public void setPaymentType(PaymentType paymentType) {
        this.paymentType = paymentType;
    }

    public Double getTotal() {
        return total;
    }

    public void setTotal(Double total) {
        this.total = total;
    }

    public PaymentFrequency getPaymentFrequency() {
        return paymentFrequency;
    }

    public void setPaymentFrequency(PaymentFrequency paymentFrequency) {
        this.paymentFrequency = paymentFrequency;
    }

    public String getStatus() {
        return status;
    }

    public void setStatus(String status) {
        this.status = status;
    }

    public List<SaleDetail> getDetails() {
        return details;
    }

    public void setDetails(List<SaleDetail> details) {
        this.details = details;
    }

    public List<Installment> getInstallments() {
        return installments;
    }

    public void setInstallments(List<Installment> installments) {
        this.installments = installments;
    }

    public Delivery getDelivery() {
        return delivery;
    }

    public void setDelivery(Delivery delivery) {
        this.delivery = delivery;
    }
}