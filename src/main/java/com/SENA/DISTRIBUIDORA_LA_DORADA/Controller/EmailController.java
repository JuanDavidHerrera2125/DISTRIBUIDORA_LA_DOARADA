package com.SENA.DISTRIBUIDORA_LA_DORADA.Controller;

import com.SENA.DISTRIBUIDORA_LA_DORADA.DTO.EmailRequest;
import com.SENA.DISTRIBUIDORA_LA_DORADA.Service.EmailService;
import com.SENA.DISTRIBUIDORA_LA_DORADA.Exception.CustomMessagingException;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.*;

@RestController
@RequestMapping("/email")
@CrossOrigin(origins = "*")
public class EmailController {

    @Autowired
    private EmailService emailService;

    @PostMapping("/send")
    public String sendEmail(@RequestBody EmailRequest emailRequest) {
        try {
            emailService.sendEmail(emailRequest.getTo(), emailRequest.getSubject(), emailRequest.getBody());
            return "Correo enviado correctamente a " + emailRequest.getTo();
        } catch (CustomMessagingException e) {
            e.printStackTrace();
            return "Error al enviar el correo: " + e.getMessage();
        }
    }
}
