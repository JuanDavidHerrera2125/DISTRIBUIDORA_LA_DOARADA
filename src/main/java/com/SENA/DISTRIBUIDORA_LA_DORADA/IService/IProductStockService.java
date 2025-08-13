package com.SENA.DISTRIBUIDORA_LA_DORADA.IService;

import com.SENA.DISTRIBUIDORA_LA_DORADA.DTO.ProductStockDto;

import java.util.List;

public interface IProductStockService {
    List<ProductStockDto> getAllProductsWithStock();
}
