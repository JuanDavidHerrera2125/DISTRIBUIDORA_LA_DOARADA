package com.SENA.DISTRIBUIDORA_LA_DORADA.DTO;

import lombok.Data;

@Data
public class SaleRequestDto {
    // Datos del cliente (sin ID)
    private String clientName;
    private String clientPhone;
    private String clientAddress;

    // Datos del producto (sin ID, pero con nombre y modelo)
    private String productName;   // Ej: "Silla Mesedora"
    private String productModel;  // Ej: "Wuayú"
    private Integer quantity;
    private Double unitPrice;

    // Opcional: si quieres permitir usar directamente el ID del producto
    private Long productId;
}
