package com.SENA.DISTRIBUIDORA_LA_DORADA.Repository;

import com.SENA.DISTRIBUIDORA_LA_DORADA.Entity.Installment;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

@Repository
public interface InstallmentRepository extends JpaRepository<Installment, Long> {
    // No hace falta declarar métodos CRUD básicos aquí
}
