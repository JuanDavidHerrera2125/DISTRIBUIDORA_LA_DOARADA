package com.SENA.DISTRIBUIDORA_LA_DORADA.Controller;

import com.SENA.DISTRIBUIDORA_LA_DORADA.Entity.Client;
import com.SENA.DISTRIBUIDORA_LA_DORADA.IService.IClientService;
import com.SENA.DISTRIBUIDORA_LA_DORADA.Repository.ClientRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Optional;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/clients")
public class ClientController {

    @Autowired
    private IClientService clientService;

    @Autowired
    private ClientRepository clientRepository;

    // Obtener todos los clientes
    @GetMapping("all")
    public ResponseEntity<List<Client>> getAllClients() {
        List<Client> clients = clientService.getAllClient();
        return ResponseEntity.ok(clients);
    }
    // Crear un nuevo cliente
    @PostMapping
    public ResponseEntity<Client> createClient(@RequestBody Client client) {
        Client savedClient = clientService.save(client);
        return ResponseEntity.ok(savedClient);
    }

    // Obtener un cliente por ID
    @GetMapping("/{id}")
    public ResponseEntity<Client> getClientById(@PathVariable Long id) {
        Optional<Client> client = clientService.findById(id);
        return client.map(ResponseEntity::ok)
                .orElseGet(() -> ResponseEntity.notFound().build());
    }

    // Actualizar un cliente por ID
    @PutMapping("/{id}")
    public ResponseEntity<Client> updateClient(@PathVariable Long id, @RequestBody Client client) {
        try {
            Client updatedClient = clientService.updateClient(id, client);
            return ResponseEntity.ok(updatedClient);
        } catch (RuntimeException e) {
            return ResponseEntity.notFound().build();
        }
    }

    // Eliminar un cliente por ID
    @DeleteMapping("/{id}")
    public ResponseEntity<Void> deleteClient(@PathVariable Long id) {
        clientService.delete(id);
        return ResponseEntity.noContent().build();
    }

    @GetMapping("/search")
    public List<Client> searchClients(@RequestParam String query) {
        return clientRepository.findByNameContainingIgnoreCaseOrPhoneContaining(query, query);
    }

}
