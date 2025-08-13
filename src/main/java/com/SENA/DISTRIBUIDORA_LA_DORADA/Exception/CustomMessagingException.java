package com.SENA.DISTRIBUIDORA_LA_DORADA.Exception;

/**
 * Excepción personalizada para errores en el envío de correos.
 */
public class CustomMessagingException extends Exception {

    // Constructor con solo mensaje
    public CustomMessagingException(String message) {
        super(message);
    }

    // Constructor con mensaje y causa (excepción original)
    public CustomMessagingException(String message, Throwable cause) {
        super(message, cause);
    }
}
