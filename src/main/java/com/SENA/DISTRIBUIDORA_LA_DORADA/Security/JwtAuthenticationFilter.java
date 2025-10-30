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
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.AuthorityUtils;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import java.io.IOException;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

@Component
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    @Autowired
    private JwtTokenUtil jwtTokenUtil;

    @Autowired
    private IUserService userService;

    private final Set<String> tokenBlacklist = new HashSet<>();

    public void invalidateToken(String token) {
        tokenBlacklist.add(token);
    }

    public boolean isTokenInvalid(String token) {
        return tokenBlacklist.contains(token);
    }

    @Override
    protected void doFilterInternal(HttpServletRequest request,
                                    HttpServletResponse response,
                                    FilterChain filterChain) throws ServletException, IOException {

        String path = request.getRequestURI();

        // ✅ EXCLUIR RUTAS PÚBLICAS (login, logout, etc.)
        if (path.startsWith("/api/auth") || path.startsWith("/auth")) {
            filterChain.doFilter(request, response);
            return;
        }

        String header = request.getHeader("Authorization");

        // ✅ PROCESAR TOKEN PARA RUTAS PROTEGIDAS
        if (header != null && header.startsWith("Bearer ")) {
            String token = header.substring(7);

            // Verificar si el token está en la lista negra
            if (tokenBlacklist.contains(token)) {
                response.setStatus(HttpStatus.UNAUTHORIZED.value());
                response.getWriter().write("Token inválido. Inicie sesión nuevamente.");
                return;
            }

            try {
                // ✅ VALIDAR EL TOKEN COMPLETAMENTE
                if (jwtTokenUtil.validateToken(token)) {
                    String email = jwtTokenUtil.getUsernameFromToken(token);

                    // ✅ BUSCAR USUARIO Y ESTABLECER AUTENTICACIÓN
                    User usuario = userService.findByEmail(email).orElse(null);

                    if (usuario != null && SecurityContextHolder.getContext().getAuthentication() == null) {
                        // ✅ Cargar roles del usuario con prefijo ROLE_ (importante para Spring Security)
                        List<GrantedAuthority> authorities = AuthorityUtils.createAuthorityList(
                                "ROLE_" + usuario.getUserRole().name()
                        );

                        // ✅ ESTABLECER LA AUTENTICACIÓN EN EL CONTEXTO DE SEGURIDAD
                        UsernamePasswordAuthenticationToken authToken =
                                new UsernamePasswordAuthenticationToken(usuario, null, authorities);
                        SecurityContextHolder.getContext().setAuthentication(authToken);
                    }
                }

            } catch (Exception e) {
                // Token inválido - no establecer autenticación
                SecurityContextHolder.clearContext();
            }
        }

        // ✅ CONTINUAR CON LA CADENA DE FILTROS
        filterChain.doFilter(request, response);
    }
}
