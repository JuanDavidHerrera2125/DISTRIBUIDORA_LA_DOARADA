package com.SENA.DISTRIBUIDORA_LA_DORADA.Controller;

import com.SENA.DISTRIBUIDORA_LA_DORADA.Entity.LoginRequest;
import com.SENA.DISTRIBUIDORA_LA_DORADA.Entity.RecoverRequest;
import com.SENA.DISTRIBUIDORA_LA_DORADA.Entity.User;
import com.SENA.DISTRIBUIDORA_LA_DORADA.IService.IUserService;
import com.SENA.DISTRIBUIDORA_LA_DORADA.Security.JwtAuthenticationFilter;
import com.SENA.DISTRIBUIDORA_LA_DORADA.Security.JwtTokenUtil;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.web.bind.annotation.*;

import java.util.Map;
import java.util.Optional;

@RestController
@RequestMapping("/auth")
@CrossOrigin(origins = "*")
public class AuthController {

    @Autowired
    private IUserService userService;

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @Autowired
    private PasswordEncoder passwordEncoder;

    @Autowired
    private JwtAuthenticationFilter jwtAuthenticationFilter;

    // ✅ LOGIN — con bcrypt
    @PostMapping("/login")
    public ResponseEntity<?> login(@RequestBody LoginRequest request) {
        // 🔹 ahora buscamos SOLO por email
        Optional<User> userOpt = userService.findByEmail(request.getEmail());

        if (userOpt.isEmpty()) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Usuario no encontrado");
        }

        User user = userOpt.get();

        // 🔹 verificamos la contraseña usando bcrypt
        if (!passwordEncoder.matches(request.getPassword(), user.getPassword())) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body("Contraseña incorrecta");
        }

        // 🔹 generar token JWT
        String token = jwtTokenUtil.generateToken(user.getEmail());

        // 🔹 devolver token + datos usuario
        return ResponseEntity.ok(Map.of(
                "token", token,
                "user", Map.of(
                        "id", user.getId(),
                        "email", user.getEmail(),
                        "role", user.getUserRole().name()
                )
        ));
    }

    // ✅ VALIDAR TOKEN — agregado sin tocar lo demás
    @GetMapping("/validate")
    public ResponseEntity<?> validateToken(@RequestHeader(name = "Authorization") String authHeader) {
        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(Map.of("error", "Token no proporcionado"));
        }

        String token = authHeader.substring(7);

        // 🔹 verificar si el token está en lista negra
        if (jwtAuthenticationFilter.isTokenInvalid(token)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("error", "Token inválido o expirado"));
        }

        // 🔹 verificar validez general
        if (!jwtTokenUtil.validateToken(token)) {
            return ResponseEntity.status(HttpStatus.UNAUTHORIZED).body(Map.of("error", "Token inválido o expirado"));
        }

        // 🔹 obtener email del token
        String email = jwtTokenUtil.getEmailFromToken(token);
        Optional<User> userOpt = userService.findByEmail(email);

        if (userOpt.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body(Map.of("error", "Usuario no encontrado"));
        }

        User user = userOpt.get();

        return ResponseEntity.ok(Map.of(
                "valid", true,
                "user", Map.of(
                        "id", user.getId(),
                        "email", user.getEmail(),
                        "role", user.getUserRole().name()
                )
        ));
    }

    // ✅ RECUPERAR CONTRASEÑA
    @PostMapping("/recover")
    public ResponseEntity<String> recoverPassword(@RequestBody RecoverRequest request) {
        Optional<User> userOpt = userService.findByEmail(request.getEmail());
        if (userOpt.isEmpty()) {
            return ResponseEntity.status(HttpStatus.NOT_FOUND).body("Usuario no encontrado");
        }

        User user = userOpt.get();
        String to = user.getEmail();
        String subject = "Recuperación de contraseña - DISTRIBUIDORA LA DORADA";
        String body = "Tu contraseña es: " + user.getPassword(); // ojo, aquí enviarías el hash

        try {
            userService.sendEmail(to, subject, body);
            return ResponseEntity.ok("Correo enviado");
        } catch (Exception e) {
            return ResponseEntity.status(HttpStatus.INTERNAL_SERVER_ERROR).body("Error enviando correo");
        }
    }

    @PostMapping("/logout")
    public ResponseEntity<?> logout(@RequestHeader(name = "Authorization", required = false) String authHeader) {
        if (authHeader != null && authHeader.startsWith("Bearer ")) {
            String token = authHeader.substring(7);
            // Agregamos el token al blacklist
            jwtAuthenticationFilter.invalidateToken(token);
            return ResponseEntity.ok(Map.of("message", "Sesión cerrada correctamente"));
        } else {
            return ResponseEntity.badRequest().body(Map.of("error", "No se proporcionó token válido"));
        }
    }

}
