package com.SENA.DISTRIBUIDORA_LA_DORADA.DTO;

public class StockDto {
    private Long productId;
    private Integer currentStock;

    // Getters y Setters
    public Long getProductId() {
        return productId;
    }

    public void setProductId(Long productId) {
        this.productId = productId;
    }

    public Integer getCurrentStock() {
        return currentStock;
    }

    public void setCurrentStock(Integer currentStock) {
        this.currentStock = currentStock;
    }



}