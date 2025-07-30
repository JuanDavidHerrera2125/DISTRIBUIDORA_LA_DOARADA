package com.SENA.DISTRIBUIDORA_LA_DORADA.IService;

import com.SENA.DISTRIBUIDORA_LA_DORADA.Entity.PasswordResetToken;

import java.util.Optional;

public interface IPasswordResetToken {

    PasswordResetToken createToken(String email);

    Optional<PasswordResetToken> getByToken (String token);

    Optional<PasswordResetToken> getByEmail (String email);

    Boolean isValidToken (String Boolean);

    void invalidate(String email);
}
