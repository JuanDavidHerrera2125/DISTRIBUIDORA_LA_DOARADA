package com.SENA.DISTRIBUIDORA_LA_DORADA.IService;

import com.SENA.DISTRIBUIDORA_LA_DORADA.DTO.ProductStockDto;
import com.SENA.DISTRIBUIDORA_LA_DORADA.DTO.StockDto;
import com.SENA.DISTRIBUIDORA_LA_DORADA.Entity.Product;
import com.SENA.DISTRIBUIDORA_LA_DORADA.Entity.Stock;
import jakarta.transaction.Transactional;

import java.util.List;
import java.util.Optional;

public interface IStockService {

    List<Stock> findAll();

    Optional<Stock> findById (Long id);

    Stock save (Stock stock);

    void delete (Long id);

    Optional<Stock> findByProductId(Long productId);
    ;

    Stock updateStock(Long id, Stock stock);


    Stock saveFromDto(StockDto dto);

    List<ProductStockDto> getGroupedProductStock();

    Optional<Stock> findByProduct(Product product);

    @Transactional
    void saveOrUpdateStock(Product product, int stockToAdd);

    @Transactional
    void decreaseStock(Long productId, int quantity);
}
