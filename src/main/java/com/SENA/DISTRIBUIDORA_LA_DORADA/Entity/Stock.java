package com.SENA.DISTRIBUIDORA_LA_DORADA.Entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table(name = "stocks")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Stock {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    // Cantidad actual de stock
    @Column(name = "current_stock", nullable = false)
    private Integer currentStock;

    // Relación 1:1 con Product
    @OneToOne
    @JoinColumn(name = "product_id", nullable = false, unique = true)
    private Product product;

}
