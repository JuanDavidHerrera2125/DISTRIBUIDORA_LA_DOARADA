package com.SENA.DISTRIBUIDORA_LA_DORADA.IService;

import com.SENA.DISTRIBUIDORA_LA_DORADA.Entity.Installment;

import java.util.List;
import java.util.Optional;

public interface IInstallmentService {

    List<Installment> findAll();

    Optional<Installment> findById(Long id); // ✅ CORREGIDO

    List<Installment> getAllInstallments();

    Installment save(Installment installment);

    void delete(Long id);

    Installment updateInstallment(Long id, Installment installment);

    void deleteInstallment(Long id);
}
