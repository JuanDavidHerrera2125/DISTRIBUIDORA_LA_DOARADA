package com.SENA.DISTRIBUIDORA_LA_DORADA.Repository;

import com.SENA.DISTRIBUIDORA_LA_DORADA.Entity.Product;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.Optional;

public interface ProductRepository extends JpaRepository<Product, Long> {
    // Aquí solo métodos abstractos si quieres, por ejemplo:
    // Optional<Product> findByName(String name);

    // Repository
    Optional<Product> findByNameAndUnitPrice(String name, Double unitPrice);

    Optional<Product> findByName(String name);

    Optional<Product> findByNameAndModel(String name, String model);

    // ✅ Cuenta productos con active = true
    long countByActiveTrue();

    // ✅ Cuenta productos que tienen stock asociado con currentStock > 0
    @Query("SELECT COUNT(p) FROM Product p WHERE p.stock IS NOT NULL AND p.stock.currentStock > 0")
    long countStockAvailable();
}
