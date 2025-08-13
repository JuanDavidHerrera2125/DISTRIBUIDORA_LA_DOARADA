package com.SENA.DISTRIBUIDORA_LA_DORADA.Service;

import com.SENA.DISTRIBUIDORA_LA_DORADA.Entity.Installment;
import com.SENA.DISTRIBUIDORA_LA_DORADA.IService.IInstallmentService;
import com.SENA.DISTRIBUIDORA_LA_DORADA.Repository.InstallmentRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service
public class InstallmentService implements IInstallmentService {

    @Autowired
    private InstallmentRepository installmentRepository;

    @Override
    public List<Installment> findAll() {
        return installmentRepository.findAll();
    }

    @Override
    public Optional<Installment> findById(Long id) {
        return installmentRepository.findById(id);
    }

    @Override
    public List<Installment> getAllInstallments() {
        return installmentRepository.findAll();
    }

    @Override
    public Installment save(Installment installment) {
        return installmentRepository.save(installment);
    }

    @Override
    public void delete(Long id) {
        installmentRepository.deleteById(id);
    }

    @Override
    public void deleteInstallment(Long id) {
        installmentRepository.deleteById(id);
    }

    @Override
    public Installment updateInstallment(Long id, Installment installment) {
        return installmentRepository.findById(id)
                .map(existing -> {
                    existing.setAmount(installment.getAmount());
                    existing.setInstallmentNumber(installment.getInstallmentNumber());
                    existing.setScheduledDate(installment.getScheduledDate());
                    existing.setStatus(installment.getStatus());
                    existing.setSale(installment.getSale());
                    return installmentRepository.save(existing);
                })
                .orElseThrow(() -> new RuntimeException("Cuota no encontrada con id: " + id));
    }
}
