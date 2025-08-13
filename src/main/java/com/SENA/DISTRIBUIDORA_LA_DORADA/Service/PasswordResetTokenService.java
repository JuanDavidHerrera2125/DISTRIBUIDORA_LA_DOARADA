package com.SENA.DISTRIBUIDORA_LA_DORADA.Service;


import com.SENA.DISTRIBUIDORA_LA_DORADA.Entity.PasswordResetToken;
import com.SENA.DISTRIBUIDORA_LA_DORADA.IService.IPasswordResetTokenService;
import com.SENA.DISTRIBUIDORA_LA_DORADA.Repository.PasswordResetTokenRepository;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import java.time.LocalDateTime;
import java.util.Optional;
import java.util.UUID;

@Service
public class PasswordResetTokenService implements IPasswordResetTokenService {

    private static final int EXPIRATION_MINUTES = 30;

    @Autowired
    private PasswordResetTokenRepository tokenRepository;


    //crear token nuevo
    @Override
    public PasswordResetToken createToken(String email) {

        // eliminar token si ya existe

        tokenRepository.deleteByEmail(email);

        //crear token nuevo

        String token = UUID.randomUUID().toString();
        LocalDateTime expiration = LocalDateTime.now().plusMinutes(EXPIRATION_MINUTES);

        PasswordResetToken passwordResetToken = new PasswordResetToken();
        passwordResetToken.setEmail(email);
        passwordResetToken.setToken(token);
        passwordResetToken.setExpirationDate(expiration);

        return tokenRepository.save(passwordResetToken);

    }

    @Override
    public Optional<PasswordResetToken> getByToken(String token) {
        return tokenRepository.findByToken(token);
    }

    @Override
    public Optional<PasswordResetToken> getByEmail(String email) {
        return tokenRepository.findByEmail(email);
    }

    @Override
    public Boolean isValidToken(String token) {
        Optional<PasswordResetToken> optional = tokenRepository.findByToken(token);
        if (optional.isEmpty()){
            return false;
        }

    PasswordResetToken passwordResetToken = optional.get();
        return passwordResetToken.getExpirationDate().isAfter(LocalDateTime.now());
    }
    @Override
    public void invalidate(String email) {
        tokenRepository.deleteByEmail(email);

    }

    @Override
    public void invalidateToken(String email) {
        tokenRepository.deleteByEmail(email);
    }
}
