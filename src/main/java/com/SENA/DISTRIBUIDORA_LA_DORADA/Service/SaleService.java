package com.SENA.DISTRIBUIDORA_LA_DORADA.Service;

import com.SENA.DISTRIBUIDORA_LA_DORADA.Entity.Sale;
import com.SENA.DISTRIBUIDORA_LA_DORADA.Entity.SaleDetail;
import com.SENA.DISTRIBUIDORA_LA_DORADA.IService.ISaleService;
import com.SENA.DISTRIBUIDORA_LA_DORADA.Repository.SaleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import java.util.List;
import java.util.Optional;

@Service
public class SaleService implements ISaleService {

    @Autowired
    private SaleRepository saleRepository;

    @Autowired
    private StockService stockService; // 🔹 Inyección para manejar stock

    @Override
    public List<Sale> findAll() {
        return saleRepository.findAll();
    }

    @Override
    public Optional<Sale> findById(Long id) {
        return saleRepository.findById(id);
    }

    @Override
    public Sale save(Sale sale) {
        return saleRepository.save(sale); // ✅ Solo guarda, sin tocar stock
    }

    @Override
    public void delete(Long id) {
        saleRepository.deleteById(id);
    }

    @Override
    public Sale updateSale(Long id, Sale sale) {
        return saleRepository.findById(id).map(existing -> {
            existing.setClient(sale.getClient());
            existing.setDate(sale.getDate());
            existing.setPaymentType(sale.getPaymentType());
            existing.setTotal(sale.getTotal());
            existing.setPaymentFrequency(sale.getPaymentFrequency());
            existing.setDetails(sale.getDetails());
            existing.setInstallments(sale.getInstallments());
            existing.setDelivery(sale.getDelivery());
            return saleRepository.save(existing);
        }).orElseThrow(() -> new RuntimeException("Venta no encontrada con id: " + id));
    }

    // 🔹 Nuevo método agregado para traer Sale con detalles
    @Override
    public Optional<Sale> findByIdWithDetails(Long id) {
        return saleRepository.findSaleWithDetails(id);
    }

    @Transactional // 👈 Transacción en el servicio (mejor práctica)
    public void cancelSale(Long id) {
        Sale sale = findByIdWithDetails(id)
                .orElseThrow(() -> new RuntimeException("Venta no encontrada"));

        if ("CANCELLED".equalsIgnoreCase(sale.getStatus())) {
            throw new RuntimeException("La venta ya fue cancelada anteriormente");
        }

        for (SaleDetail detail : sale.getDetails()) {
            stockService.increaseStock(detail.getProduct().getId(), detail.getQuantity());
        }

        sale.setStatus("CANCELLED");
        save(sale); // 👈 Guarda la venta actualizada
    }

}
