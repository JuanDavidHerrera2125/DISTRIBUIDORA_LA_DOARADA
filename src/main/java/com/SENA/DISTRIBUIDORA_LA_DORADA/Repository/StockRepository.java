package com.SENA.DISTRIBUIDORA_LA_DORADA.Repository;

import com.SENA.DISTRIBUIDORA_LA_DORADA.Entity.Product;
import com.SENA.DISTRIBUIDORA_LA_DORADA.Entity.Stock;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Optional;

public interface StockRepository extends JpaRepository<Stock, Long> {

    Optional<Stock> findByProduct(Product product);

    Optional<Stock> findByProduct_Id(Long productId);

    List<Stock> findByProductId(Long productId);
}
