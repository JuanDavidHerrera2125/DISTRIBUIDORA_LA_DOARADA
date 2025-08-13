package com.SENA.DISTRIBUIDORA_LA_DORADA.Service;

import com.SENA.DISTRIBUIDORA_LA_DORADA.DTO.SaleRequestDto;
import com.SENA.DISTRIBUIDORA_LA_DORADA.Entity.Client;
import com.SENA.DISTRIBUIDORA_LA_DORADA.IService.IClientService;
import com.SENA.DISTRIBUIDORA_LA_DORADA.Repository.ClientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

@Service

public class ClientService implements IClientService {

    @Autowired
    private ClientRepository clientRepository;

    @Override
    public List<Client> getAllClient() {
        return clientRepository.findAll();
    }

    @Override
    public Client save(Client client) {
        return clientRepository.save(client);
    }

    @Override
    public Optional<Client> findById(Long id) {
        return clientRepository.findById(id);
    }

    @Override
    public void delete(Long id) {
        clientRepository.deleteById(id);
    }

    @Override
    public Client updateClient(Long id, Client client){
        return clientRepository.findById(id)
                .map(existingClient -> {

                    existingClient.setName(client.getName());
                    existingClient.setAddress(client.getAddress());
                    existingClient.setPhone(client.getPhone());
                    existingClient.setEmail(client.getEmail());
                    return clientRepository.save(existingClient);
                })
                .orElseThrow(() -> new RuntimeException("Cliente no encontrado con id: " + id));



    }

    @Override
    public Client findByPhone(String phone) {
        return clientRepository.findByPhone(phone);
    }

    @Override
    public Client findByPhoneOrSave(String phone, SaleRequestDto request) {
        // Buscar cliente por teléfono
        Optional<Client> existing = Optional.ofNullable(clientRepository.findByPhone(phone));

        if (existing.isPresent()) {
            return existing.get(); // Si existe, devolverlo
        }

        // Si no existe, crear uno nuevo
        Client newClient = new Client();
        newClient.setName(request.getClientName());
        newClient.setPhone(request.getClientPhone());
        newClient.setAddress(request.getClientAddress());
        newClient.setActive(true); // o el valor por defecto

        return clientRepository.save(newClient);
    }

}
