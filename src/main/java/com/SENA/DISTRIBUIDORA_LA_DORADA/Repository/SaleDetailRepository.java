package com.SENA.DISTRIBUIDORA_LA_DORADA.Repository;

import com.SENA.DISTRIBUIDORA_LA_DORADA.Entity.SaleDetail;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface SaleDetailRepository extends JpaRepository<SaleDetail, Long> {
    // Aquí puedes añadir métodos personalizados si necesitas
}
