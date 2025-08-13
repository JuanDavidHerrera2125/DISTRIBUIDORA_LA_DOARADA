package com.SENA.DISTRIBUIDORA_LA_DORADA.IService;

import com.SENA.DISTRIBUIDORA_LA_DORADA.Entity.Delivery;

import java.util.List;
import java.util.Optional;

public interface IDeliveryService {

    List<Delivery> findAll();

    Optional<Delivery> findById(Long id);

    Delivery save (Delivery delivery);

    void delete (Long id);

    Delivery updateDelivery(Long id, Delivery delivery);
}
