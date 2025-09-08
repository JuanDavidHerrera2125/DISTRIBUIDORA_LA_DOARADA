package com.SENA.DISTRIBUIDORA_LA_DORADA.Repository;

import com.SENA.DISTRIBUIDORA_LA_DORADA.Entity.Client;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface ClientRepository extends JpaRepository<Client , Long> {

    Client findByPhone(String phone);

    List<Client> findByNameContainingIgnoreCaseOrPhoneContaining(String name, String phone);

}
