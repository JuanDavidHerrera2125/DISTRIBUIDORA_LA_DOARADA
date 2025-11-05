package com.SENA.DISTRIBUIDORA_LA_DORADA.Security;

import io.jsonwebtoken.Claims;
import io.jsonwebtoken.Jwts;
import io.jsonwebtoken.SignatureAlgorithm;
import io.jsonwebtoken.io.Decoders;
import io.jsonwebtoken.security.Keys;
import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Component;

import java.security.Key;
import java.util.Date;

@Component
public class JwtTokenUtil {

    @Value("${jwt.secret:mySecretKey}")
    private String secret;

    @Value("${jwt.expiration:86400000}")
    private Long expiration;

    // 🔐 Genera la clave de firma desde el secreto (debe ser Base64 y ≥ 256 bits)
    private Key getSigningKey() {
        // Si usas "mySecretKey" como valor por defecto, NO es Base64 válido → lo corregimos
        if ("mySecretKey".equals(secret)) {
            // Usamos una clave segura por defecto en Base64 (256 bits = 32 bytes → 44 chars en Base64)
            secret = "bXlTZWNyZXRLZXlmb3JEaXN0cmlidXRvcmExMjM0NTY3ODk="; // "mySecretKeyforDistributo..." en Base64
        }
        return Keys.hmacShaKeyFor(Decoders.BASE64.decode(secret));
    }

    // 🎫 Generar token SOLO con subject (para compatibilidad)
    public String generateToken(String subject) {
        return generateToken(subject, "USER"); // Rol por defecto
    }

    // 🎫 Generar token con subject Y rol (¡este es el que usarás en login!)
    public String generateToken(String subject, String role) {
        return Jwts.builder()
                .setSubject(subject)
                .claim("role", role) // 👈 Incluye el rol en el token
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + expiration))
                .signWith(getSigningKey(), SignatureAlgorithm.HS512)
                .compact();
    }

    // 🔍 Obtener todos los claims del token
    public Claims getAllClaimsFromToken(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    // 👤 Obtener email/username del token
    public String getUsernameFromToken(String token) {
        return getAllClaimsFromToken(token).getSubject();
    }

    // 👤 Alias para claridad
    public String getEmailFromToken(String token) {
        return getUsernameFromToken(token);
    }

    // 🎖️ Obtener rol del token
    public String getRoleFromToken(String token) {
        Claims claims = getAllClaimsFromToken(token);
        return (String) claims.get("role"); // Retorna "ADMIN", "USER", etc.
    }

    // ⏳ Verificar si el token está expirado
    public boolean isTokenExpired(String token) {
        return getAllClaimsFromToken(token).getExpiration().before(new Date());
    }

    // ✅ Validar token (no expirado y con subject)
    public boolean validateToken(String token) {
        try {
            String subject = getUsernameFromToken(token);
            return subject != null && !isTokenExpired(token);
        } catch (Exception e) {
            return false;
        }
    }
}