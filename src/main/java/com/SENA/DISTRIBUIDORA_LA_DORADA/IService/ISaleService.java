package com.SENA.DISTRIBUIDORA_LA_DORADA.IService;

import com.SENA.DISTRIBUIDORA_LA_DORADA.Entity.Sale;

import java.util.List;
import java.util.Optional;

public interface ISaleService {

    List<Sale> findAll();

    Optional<Sale> findById(Long id);

    Sale save(Sale sale);

    void delete(Long id);

    Sale updateSale(Long id, Sale sale);

    // 🔹 Nuevo método agregado en la interfaz
    Optional<Sale> findByIdWithDetails(Long id);


}
