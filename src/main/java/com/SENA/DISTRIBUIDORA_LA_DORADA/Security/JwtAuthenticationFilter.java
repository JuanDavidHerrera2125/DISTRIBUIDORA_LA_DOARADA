package com.SENA.DISTRIBUIDORA_LA_DORADA.Security;

import com.SENA.DISTRIBUIDORA_LA_DORADA.Entity.User;
import com.SENA.DISTRIBUIDORA_LA_DORADA.IService.IUserService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.HashSet;
import java.util.Set;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @Autowired
    private IUserService userService;

    // 🔹 Lista negra de tokens para invalidar sesiones cerradas
    private final Set<String> tokenBlacklist = new HashSet<>();

    // 🔹 Agrega un token a la lista negra (logout)
    public void invalidateToken(String token) {
        tokenBlacklist.add(token);
    }

    // 🔹 Verifica si el token ya fue invalidado
    public boolean isTokenInvalid(String token) {
        return tokenBlacklist.contains(token);
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        String header = request.getHeader("Authorization");

        if (header != null && header.startsWith("Bearer ")) {
            String token = header.substring(7);

            if (tokenBlacklist.contains(token)) {
                response.setStatus(HttpStatus.UNAUTHORIZED.value());
                response.getWriter().write("Token inválido. Inicie sesión nuevamente.");
                return;
            }

            try {
                String email = jwtTokenUtil.getUsernameFromToken(token);

                if (!jwtTokenUtil.isTokenExpired(token)
                        && SecurityContextHolder.getContext().getAuthentication() == null) {

                    User usuario = userService.findByEmail(email).orElse(null);

                    if (usuario != null) {
                        UsernamePasswordAuthenticationToken authToken =
                                new UsernamePasswordAuthenticationToken(usuario, null, null);
                        SecurityContextHolder.getContext().setAuthentication(authToken);
                    }
                }

            } catch (Exception e) {
                response.setStatus(HttpStatus.UNAUTHORIZED.value());
                response.getWriter().write("Token inválido o expirado");
                return;
            }
        }

        filterChain.doFilter(request, response);
    }
}
