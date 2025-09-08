package com.SENA.DISTRIBUIDORA_LA_DORADA.Repository;

import com.SENA.DISTRIBUIDORA_LA_DORADA.Entity.Sale;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;

import java.util.Optional;

public interface SaleRepository extends JpaRepository<Sale, Long> {

    // 🔹 JPQL para traer Sale con sus detalles y evitar error "No property 'withDetails'"
    @Query("SELECT s FROM Sale s LEFT JOIN FETCH s.details WHERE s.id = :id")
    Optional<Sale> findSaleWithDetails(@Param("id") Long id);



}
