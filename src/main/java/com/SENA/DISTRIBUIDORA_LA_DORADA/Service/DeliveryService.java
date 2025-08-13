package com.SENA.DISTRIBUIDORA_LA_DORADA.Service;


import com.SENA.DISTRIBUIDORA_LA_DORADA.Entity.Delivery;
import com.SENA.DISTRIBUIDORA_LA_DORADA.IService.IDeliveryService;
import com.SENA.DISTRIBUIDORA_LA_DORADA.Repository.DeliveryRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class DeliveryService implements IDeliveryService {

    @Autowired DeliveryRepository deliveryRepository;

    @Override
    public List<Delivery> findAll() {
        return deliveryRepository.findAll();
    }

    @Override
    public Optional<Delivery> findById(Long id) {
        return deliveryRepository.findById(id);
    }

    @Override
    public Delivery save(Delivery delivery) {
        return deliveryRepository.save(delivery);
    }

    @Override
    public void delete(Long deliveryId) {

    }

    @Override
    public Delivery updateDelivery(Long id, Delivery delivery){

        return deliveryRepository.findById(id)
                .map(existingDelivery ->{

                    existingDelivery.setNotes(delivery.getNotes());
                    return deliveryRepository.save(existingDelivery);
                        })
                .orElseThrow(() -> new RuntimeException("Delivery no encontrado con id: " + id));
    }
}
