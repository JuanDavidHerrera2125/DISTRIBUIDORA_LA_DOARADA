package com.SENA.DISTRIBUIDORA_LA_DORADA.DTO;

import com.SENA.DISTRIBUIDORA_LA_DORADA.Enums.UserRole;

public class UserCreateDto {
    private String userName;
    private String email;
    private String password;
    private UserRole userRole;

    public String getUserName() { return userName; }
    public void setUserName(String userName) { this.userName = userName; }

    public String getEmail() { return email; }
    public void setEmail(String email) { this.email = email; }

    public String getPassword() { return password; }
    public void setPassword(String password) { this.password = password; }

    public UserRole getUserRole() { return userRole; }
    public void setUserRole(UserRole userRole) { this.userRole = userRole; }
}
