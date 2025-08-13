package com.SENA.DISTRIBUIDORA_LA_DORADA.IService;

import com.SENA.DISTRIBUIDORA_LA_DORADA.Entity.SaleDetail;

import java.util.List;
import java.util.Optional;

public interface ISaleDetailService {

    List<SaleDetail> findAll();

    Optional<SaleDetail> findById(Long id);

    SaleDetail save(SaleDetail saleDetail);

    void delete(Long id);

    SaleDetail update(Long id, SaleDetail saleDetail);
}
