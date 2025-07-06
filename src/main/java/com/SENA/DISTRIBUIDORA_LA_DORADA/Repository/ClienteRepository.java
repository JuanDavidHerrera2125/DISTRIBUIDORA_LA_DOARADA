package com.SENA.DISTRIBUIDORA_LA_DORADA.Repository;

import com.SENA.DISTRIBUIDORA_LA_DORADA.Entity.Cliente;
import org.springframework.data.jpa.repository.JpaRepository;

public interface ClienteRepository extends JpaRepository<Cliente, Long> {
}
