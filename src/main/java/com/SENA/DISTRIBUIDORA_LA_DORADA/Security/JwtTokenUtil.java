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

    // 🔹 Llave secreta
    private Key getSigningKey() {
        return Keys.hmacShaKeyFor(Decoders.BASE64.decode(secret));
    }

    // 🔹 Generar token
    public String generateToken(String subject) {
        return Jwts.builder()
                .setSubject(subject)
                .setIssuedAt(new Date())
                .setExpiration(new Date(System.currentTimeMillis() + expiration))
                .signWith(getSigningKey(), SignatureAlgorithm.HS512)
                .compact();
    }

    // 🔹 Obtener todos los claims
    public Claims getAllClaimsFromToken(String token) {
        return Jwts.parserBuilder()
                .setSigningKey(getSigningKey())
                .build()
                .parseClaimsJws(token)
                .getBody();
    }

    // 🔹 Obtener email/usuario del token
    public String getUsernameFromToken(String token) {
        return getAllClaimsFromToken(token).getSubject();
    }

    // 🔹 Verificar si el token expiró
    public boolean isTokenExpired(String token) {
        return getAllClaimsFromToken(token).getExpiration().before(new Date());
    }

    // Valida un token de forma general: no expirado y formato correcto
    public boolean validateToken(String token) {
        try {
            return !isTokenExpired(token) && getUsernameFromToken(token) != null;
        } catch (Exception e) {
            return false;
        }
    }

    // 🔹 Obtener email del token (nuevo para /auth/validate)
    public String getEmailFromToken(String token) {
        return getUsernameFromToken(token);
    }
}
