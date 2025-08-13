package com.SENA.DISTRIBUIDORA_LA_DORADA.IService;

import com.SENA.DISTRIBUIDORA_LA_DORADA.Exception.CustomMessagingException;
import jakarta.mail.MessagingException;

public interface IEmailService {
    void sendSimpleMessage(String to, String subject, String text) throws MessagingException;

    void sendEmail(String to, String subject, String body) throws CustomMessagingException;
}
