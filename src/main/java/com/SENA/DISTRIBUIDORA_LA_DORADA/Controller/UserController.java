package com.SENA.DISTRIBUIDORA_LA_DORADA.Controller;

import com.SENA.DISTRIBUIDORA_LA_DORADA.DTO.UserCreateDto;
import com.SENA.DISTRIBUIDORA_LA_DORADA.Entity.User;
import com.SENA.DISTRIBUIDORA_LA_DORADA.IService.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;
import java.util.Map;

@RestController
@RequestMapping("/users")
@CrossOrigin(origins = "*")
public class UserController {

    @Autowired
    private IUserService userService;

    // Crear usuario
    @PostMapping
    public ResponseEntity<?> createUser(@RequestBody UserCreateDto dto) {
        try {
            User user = userService.createUser(dto);
            return ResponseEntity.ok(Map.of(
                    "message", "Usuario creado correctamente",
                    "user", Map.of(
                            "id", user.getId(),
                            "email", user.getEmail(),
                            "userName", user.getUserName(),
                            "role", user.getUserRole().name()
                    )
            ));
        } catch (Exception e) {
            return ResponseEntity.status(400).body(Map.of(
                    "message", "Error al crear usuario: " + e.getMessage()
            ));
        }
    }

    // Listar todos los usuarios
    @GetMapping
    public ResponseEntity<List<User>> getUsers() {
        return ResponseEntity.ok(userService.findAll());
    }

    // Actualizar usuario
    @PutMapping("/{id}")
    public ResponseEntity<?> updateUser(@PathVariable Long id, @RequestBody UserCreateDto dto) {
        User updated = userService.update(id, dto);
        if (updated == null) return ResponseEntity.notFound().build();
        return ResponseEntity.ok(updated);
    }

    // Eliminar usuario
    @DeleteMapping("/{id}")
    public ResponseEntity<?> deleteUser(@PathVariable Long id) {
        userService.delete(id);
        return ResponseEntity.ok(Map.of("message", "Usuario eliminado correctamente"));
    }
}
