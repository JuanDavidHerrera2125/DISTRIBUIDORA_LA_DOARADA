package com.SENA.DISTRIBUIDORA_LA_DORADA.Entity;

import jakarta.persistence.*;
import lombok.*;

import java.util.Date;
import java.util.List;

@Entity
@Table(name = "products")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder

public class Product {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String name;

    private String description;

    @Column(name = "unit_prince")
    private Double unitPrince;

    @Column(name = "unit_measure")
    private String unitMeasure;

    @Temporal(TemporalType.DATE)
    @Column(name = "registration_date")
    private Date registrationDate;

    private Boolean active;

    // Relación con los detalles de venta 1:N

    @OneToMany(mappedBy = "product", cascade = CascadeType.ALL , orphanRemoval = true)
    private List<SaleDetail> saleDetails;

    // Relación 1:1 con stock

    @OneToOne(mappedBy = "product", cascade = CascadeType.ALL)
    private Stock stock;


}
