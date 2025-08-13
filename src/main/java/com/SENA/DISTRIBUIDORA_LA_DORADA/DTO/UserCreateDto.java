package com.SENA.DISTRIBUIDORA_LA_DORADA.DTO;

import com.SENA.DISTRIBUIDORA_LA_DORADA.Enums.UserRole;
import lombok.Data;

@Data
public class UserCreateDto {
    private String userName;
    private String email;
    private String password;
    private UserRole userRole;
}