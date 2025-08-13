package com.SENA.DISTRIBUIDORA_LA_DORADA.Service;

import com.SENA.DISTRIBUIDORA_LA_DORADA.Entity.SaleDetail;
import com.SENA.DISTRIBUIDORA_LA_DORADA.IService.ISaleDetailService;
import com.SENA.DISTRIBUIDORA_LA_DORADA.Repository.SaleDetailRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class SaleDetailService implements ISaleDetailService {

    @Autowired
    private SaleDetailRepository saleDetailRepository;

    @Override
    public List<SaleDetail> findAll() {
        return saleDetailRepository.findAll();
    }

    @Override
    public Optional<SaleDetail> findById(Long id) {
        return saleDetailRepository.findById(id);
    }

    @Override
    public SaleDetail save(SaleDetail saleDetail) {
        return saleDetailRepository.save(saleDetail);
    }

    @Override
    public void delete(Long id) {
        saleDetailRepository.deleteById(id);
    }

    @Override
    public SaleDetail update(Long id, SaleDetail saleDetail) {
        return saleDetailRepository.findById(id)
                .map(existing -> {
                    existing.setQuantity(saleDetail.getQuantity());
                    existing.setUnitPrice(saleDetail.getUnitPrice());
                    existing.setSubtotal(saleDetail.getSubtotal());
                    existing.setSale(saleDetail.getSale());
                    existing.setProduct(saleDetail.getProduct());
                    return saleDetailRepository.save(existing);
                })
                .orElseThrow(() -> new RuntimeException("SaleDetail no encontrado con ID: " + id));
    }
}
