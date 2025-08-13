package com.SENA.DISTRIBUIDORA_LA_DORADA.DTO;

import lombok.Data;

@Data
public class SaleDetailDto {
    private String productName;
    private String productModel;
    private Integer quantity;
    private Double unitPrice;
    private Double subtotal;
}