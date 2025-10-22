package com.SENA.DISTRIBUIDORA_LA_DORADA.Config;

import com.SENA.DISTRIBUIDORA_LA_DORADA.Security.JwtAuthenticationFilter;
import com.SENA.DISTRIBUIDORA_LA_DORADA.Security.JwtLogoutHandler;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.web.authentication.logout.LogoutHandler;

@Configuration
public class SecurityBeansConfig {

    @Bean
    public LogoutHandler logoutHandler(JwtAuthenticationFilter jwtAuthenticationFilter) {
        return new JwtLogoutHandler(jwtAuthenticationFilter);
    }
}