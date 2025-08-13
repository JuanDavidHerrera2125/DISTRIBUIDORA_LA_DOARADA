package com.SENA.DISTRIBUIDORA_LA_DORADA.Controller;

import com.SENA.DISTRIBUIDORA_LA_DORADA.Entity.PasswordResetToken;
import com.SENA.DISTRIBUIDORA_LA_DORADA.Entity.User;
import com.SENA.DISTRIBUIDORA_LA_DORADA.IService.IEmailService;
import com.SENA.DISTRIBUIDORA_LA_DORADA.IService.IPasswordResetTokenService;
import com.SENA.DISTRIBUIDORA_LA_DORADA.IService.IUserService;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

import java.util.Optional;

@RestController
@RequestMapping("/password")
@CrossOrigin(origins = "*")
public class PasswordResetController {

    @Autowired
    private IPasswordResetTokenService tokenService;

    @Autowired
    private IUserService userService;

    @Autowired
    private IEmailService emailService;

    // 1. Solicitud de recuperación de contraseña
    @PostMapping("/request-reset")
    public String requestReset(@RequestParam String email) {
        Optional<User> userOpt = userService.findByEmail(email);
        if (userOpt.isEmpty()) {
            return "No user registered with this email.";
        }

        PasswordResetToken token = tokenService.createToken(email);

        // Aquí se construye el correo
        String link = "http://localhost:4200/reset-password?token=" + token.getToken();
        String subject = "Password Reset Request";
        String body = "Hello,\n\nClick the following link to reset your password:\n" + link +
                "\n\nThis link will expire in 30 minutes.";

        try {
            emailService.sendEmail(email, subject, body);
            return "Password reset link sent to email.";
        } catch (Exception e) {
            return "Error sending email: " + e.getMessage();
        }
    }

    // 2. Confirmar token válido (opcional)
    @GetMapping("/validate-token")
    public String validateToken(@RequestParam String token) {
        boolean valid = tokenService.isValidToken(token);
        return valid ? "Token is valid." : "Invalid or expired token.";
    }

    // 3. Establecer nueva contraseña
    @PostMapping("/reset-password")
    public String resetPassword(@RequestParam String token, @RequestParam String newPassword) {
        Optional<PasswordResetToken> optionalToken = tokenService.getByToken(token);

        if (optionalToken.isEmpty()) {
            return "Invalid token.";
        }

        PasswordResetToken resetToken = optionalToken.get();

        if (!tokenService.isValidToken(token)) {
            return "Token expired.";
        }

        Optional<User> userOpt = userService.findByEmail(resetToken.getEmail());
        if (userOpt.isEmpty()) {
            return "User not found.";
        }

        User user = userOpt.get();
        user.setPassword(newPassword); // Recuerda: aquí podrías encriptar si decides agregar eso después
        userService.update(user.getId(), user);

        tokenService.invalidateToken(resetToken.getEmail());

        return "Password successfully updated.";
    }
}
