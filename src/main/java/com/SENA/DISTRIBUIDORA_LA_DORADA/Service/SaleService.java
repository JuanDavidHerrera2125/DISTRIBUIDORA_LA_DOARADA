package com.SENA.DISTRIBUIDORA_LA_DORADA.Service;

import com.SENA.DISTRIBUIDORA_LA_DORADA.Entity.Sale;
import com.SENA.DISTRIBUIDORA_LA_DORADA.Entity.SaleDetail;
import com.SENA.DISTRIBUIDORA_LA_DORADA.IService.ISaleService;
import com.SENA.DISTRIBUIDORA_LA_DORADA.Repository.SaleRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

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
        // Guardar venta
        Sale savedSale = saleRepository.save(sale);

        // 🔹 Reducir stock por cada producto vendido
        if (sale.getDetails() != null) {
            for (SaleDetail detail : sale.getDetails()) {
                stockService.decreaseStock(detail.getProduct().getId(), detail.getQuantity());
            }
        }

        return savedSale;
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
}
