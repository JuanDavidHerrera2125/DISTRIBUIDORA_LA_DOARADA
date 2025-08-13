package com.SENA.DISTRIBUIDORA_LA_DORADA.Entity;

import com.SENA.DISTRIBUIDORA_LA_DORADA.Enums.UserRole;
import jakarta.persistence.*;
import lombok.*;

@Entity
@Table( name = "users")
@Getter
@Setter
@NoArgsConstructor
@AllArgsConstructor
@Builder

public class User {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;


    @Column(name = "user_name")
    private String userName;

    private String email;

    private String password;

    @Enumerated(EnumType.STRING)
    @Column(name = "user_role")
    private UserRole userRole; // ✅ Nombre claro y estándar
}
