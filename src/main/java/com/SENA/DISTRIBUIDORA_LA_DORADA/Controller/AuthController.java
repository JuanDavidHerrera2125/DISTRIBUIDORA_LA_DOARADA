package com.SENA.DISTRIBUIDORA_LA_DORADA.Controller;

import com.SENA.DISTRIBUIDORA_LA_DORADA.Entity.LoginRequest;
import com.SENA.DISTRIBUIDORA_LA_DORADA.Entity.RecoverRequest;
import com.SENA.DISTRIBUIDORA_LA_DORADA.Entity.User;
import com.SENA.DISTRIBUIDORA_LA_DORADA.IService.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/auth")
public class AuthController {

    @Autowired
    private IUserService userService;

    // LOGIN
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request) {
        Optional<User> user = userService.findByEmailAndPassword(request.getEmail(), request.getPassword());
        if (user.isPresent()) {
            return ResponseEntity.ok("Login exitoso");
        } else {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Credenciales inválidas");
        }
    }

    // RECUPERAR CONTRASEÑA
    @PostMapping("/recover")
    public ResponseEntity<String> recoverPassword(@RequestBody RecoverRequest request) {
        Optional<User> userOpt = userService.findByEmail(request.getEmail());
        if (userOpt.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Usuario no encontrado");
        }

        User user = userOpt.get();
        String to = user.getEmail();
        String subject = "Recuperación de contraseña - DISTRIBUIDORA LA DORADA";
        String body = "Tu contraseña es: " + user.getPassword();

        try {
            userService.sendEmail(to, subject, body);
            return ResponseEntity.ok("Correo enviado");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error enviando correo");
        }
    }
}
