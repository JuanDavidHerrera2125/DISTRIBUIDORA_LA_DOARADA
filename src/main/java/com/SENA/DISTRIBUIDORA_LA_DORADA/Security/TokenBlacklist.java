package com.SENA.DISTRIBUIDORA_LA_DORADA.Security;

import org.springframework.stereotype.Component;

import java.util.Collections;
import java.util.HashSet;
import java.util.Set;

@Component
public class TokenBlacklist {

    // 🔹 Conjunto que almacena tokens inválidos
    private final Set<String> blacklistedTokens = Collections.synchronizedSet(new HashSet<>());

    // 🔹 Agregar token a la blacklist
    public void add(String token) {
        blacklistedTokens.add(token);
    }

    // 🔹 Verificar si un token está en la blacklist
    public boolean isBlacklisted(String token) {
        return blacklistedTokens.contains(token);
    }

    // 🔹 Opcional: eliminar token de la blacklist
    public void remove(String token) {
        blacklistedTokens.remove(token);
    }

    // 🔹 Limpiar todos los tokens (opcional)
    public void clear() {
        blacklistedTokens.clear();
    }
}
