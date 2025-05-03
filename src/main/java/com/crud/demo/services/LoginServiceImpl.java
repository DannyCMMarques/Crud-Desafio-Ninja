package com.crud.demo.services;

import java.util.Date;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Service;

import com.crud.demo.models.DTO.login.LoginDTO;
import com.crud.demo.models.DTO.login.LoginResponseDTO;
import com.crud.demo.security.Service.JWTService;
import com.crud.demo.services.contratos.LoginService;
import com.crud.demo.validators.UsuarioValidator;

import io.jsonwebtoken.Claims;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class LoginServiceImpl implements LoginService {

    private static final Logger log = LoggerFactory.getLogger(LoginService.class);

    private final AuthenticationManager authenticationManager;
    private final UsuarioValidator usuarioValidator;
    private final JWTService jwtService;



    @Override
    public String autentificar(LoginDTO loginDTO) {
        log.debug("[autentificar] Tentando autenticar o usuário: {}", loginDTO.getEmail());

        Authentication authentication = authenticationManager.authenticate(
                new UsernamePasswordAuthenticationToken(
                        loginDTO.getEmail(),
                        loginDTO.getSenha()));

        SecurityContextHolder.getContext().setAuthentication(authentication);

        log.info("[autentificar] Usuário {} autenticado com sucesso", loginDTO.getEmail());

        String token = jwtService.gerarToken((UserDetails) authentication.getPrincipal());
        log.debug("[autentificar] Token gerado para o usuário {}: {}", loginDTO.getEmail(), token);

        return token;
    }

    @Override
    public LoginResponseDTO gerarLoginResponse(String token) {
        log.debug("[gerarLoginResponse] Gerando resposta do login para o token.");

        String sub = jwtService.extrairUsername(token);
        String role = "USER";
        String createdAt = jwtService.extrairClaim(token, Claims::getIssuedAt).toString();
        Date exp = jwtService.extrairExpiracao(token);
        long iat = jwtService.extrairClaim(token, Claims::getIssuedAt).getTime() / 1000;

        log.info("[gerarLoginResponse] Resposta do login gerada com sucesso para o token do usuário {}", sub);

        return LoginResponseDTO.builder()
        .token(token)
        .sub(sub)
        .role(role)
        .createdAt(createdAt)
        .exp(exp)
        .iat(iat)
        .build();
        }
}
