package com.SENA.DISTRIBUIDORA_LA_DORADA.IService;

import com.SENA.DISTRIBUIDORA_LA_DORADA.Entity.Product;
import com.SENA.DISTRIBUIDORA_LA_DORADA.Entity.Stock;

import java.util.List;
import java.util.Optional;

public interface IProductService {

    List<Product> findAll();

    Optional<Product> findById(Long id);

    Optional<Stock> getStockByProductId(Long productId);

    Optional<Stock> findByProductId(Long productId);

    List<Product> getAllProduct();

    Product save(Product product);

    void delete(Long id);

    Product updateProduct(Long id, Product product);

    Optional<Product> findByNameAndUnitPrice(String name, Double unitPrice);

    Optional<Product> findByNameAndModel(String name, String model);
}
