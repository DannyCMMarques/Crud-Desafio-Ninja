package com.crud.demo.security;

import java.io.IOException;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.lang.NonNull;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.web.authentication.WebAuthenticationDetailsSource;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.crud.demo.Exceptions.usuarioException.UsuarioNaoAutorizadoException;
import com.crud.demo.security.Service.JWTService;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class JwtAuthenticationFilter extends OncePerRequestFilter {

    private static final Logger log = LoggerFactory.getLogger(JwtAuthenticationFilter.class); 

    private final JWTService jwtService;
    private final UserDetailsService userDetailsService;

    @Override
    public void doFilterInternal(
            @NonNull HttpServletRequest request,
            @NonNull HttpServletResponse response,
            @NonNull FilterChain filterChain) throws ServletException, IOException {
        final String authHeader = request.getHeader("Authorization");

        log.debug("[doFilterInternal] Cabeçalho Authorization: {}", authHeader);

        if (authHeader == null || !authHeader.startsWith("Bearer ")) {
            log.warn("[doFilterInternal] Token Bearer ausente ou incorreto.");
            filterChain.doFilter(request, response);
            return;
        }

        try {
            final String jwt = authHeader.substring(7);
            log.debug("[doFilterInternal] JWT extraído: {}", jwt);

            final String userEmail = jwtService.extrairUsername(jwt);
            log.debug("[doFilterInternal] E-mail extraído do token: {}", userEmail);

            Authentication authentication = SecurityContextHolder.getContext().getAuthentication();
            log.debug("[doFilterInternal] Autenticação existente: {}", authentication);

            if (userEmail != null && authentication == null) {
                log.debug("[doFilterInternal] Autenticação não encontrada. Carregando os detalhes do usuário.");
                UserDetails userDetails = this.userDetailsService.loadUserByUsername(userEmail);

                if (jwtService.isTokenValido(jwt, userDetails)) {
                    log.debug("[doFilterInternal] Token válido. Realizando autenticação.");

                    UsernamePasswordAuthenticationToken authToken = new UsernamePasswordAuthenticationToken(
                            userDetails,
                            null,
                            userDetails.getAuthorities());

                    authToken.setDetails(new WebAuthenticationDetailsSource().buildDetails(request));

                    SecurityContextHolder.getContext().setAuthentication(authToken);
                } else {
                    log.warn("[doFilterInternal] Token inválido para o usuário: {}", userEmail);
                }
            }

            filterChain.doFilter(request, response);

        } catch (Exception exception) {
            log.error("[doFilterInternal] Erro ao processar o token: {}", exception.getMessage(), exception);
            throw new UsuarioNaoAutorizadoException();
        }
    }
}
