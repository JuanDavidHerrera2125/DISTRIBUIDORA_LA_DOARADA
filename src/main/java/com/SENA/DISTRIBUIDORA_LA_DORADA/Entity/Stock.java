package com.SENA.DISTRIBUIDORA_LA_DORADA.Entity;

import jakarta.persistence.*;
import lombok.*;

@Entity
@Table
@Getter
@Setter
@AllArgsConstructor
@NoArgsConstructor
@Builder

public class Stock {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @OneToOne
    @JoinColumn(name = "product_id" , nullable = false , unique = true)
    private Product product;

    @Column(name = "current_stock")
    private Integer currentStock;

}
