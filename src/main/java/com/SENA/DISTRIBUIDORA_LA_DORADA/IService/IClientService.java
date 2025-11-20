package com.SENA.DISTRIBUIDORA_LA_DORADA.IService;

import com.SENA.DISTRIBUIDORA_LA_DORADA.DTO.SaleRequestDto;
import com.SENA.DISTRIBUIDORA_LA_DORADA.Entity.Client;

import java.util.List;
import java.util.Optional;

public interface IClientService {


    List<Client> getAllClient();

    Client save (Client client);

    Optional<Client> findById (Long id);

    void delete (Long id);

    Client updateClient(Long id, Client client);

    Client findByPhone(String phone);

    Client findByPhoneOrSave(String phone, SaleRequestDto request);

    long countAllClients();
}
