package com.SENA.DISTRIBUIDORA_LA_DORADA.Controller;

import com.SENA.DISTRIBUIDORA_LA_DORADA.DTO.UserCreateDto;
import com.SENA.DISTRIBUIDORA_LA_DORADA.Entity.User;
import com.SENA.DISTRIBUIDORA_LA_DORADA.IService.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Map;

@CrossOrigin(origins = "*")
@RestController
@RequestMapping("/users")
public class UserController {

    @Autowired
    private IUserService userService;

    @PostMapping
    public ResponseEntity<?> createUser(@RequestBody UserCreateDto userDto) {
        try {
            User createdUser = userService.createUser(userDto);
            return ResponseEntity.ok(Map.of(
                    "message", "Usuario creado exitosamente",
                    "user", Map.of(
                            "id", createdUser.getId(),
                            "userName", createdUser.getUserName(),
                            "email", createdUser.getEmail(),
                            "role", createdUser.getUserRole().name()
                    )
            ));
        } catch (Exception e) {
            return ResponseEntity.badRequest().body("Error al crear usuario: " + e.getMessage());
        }
    }
}