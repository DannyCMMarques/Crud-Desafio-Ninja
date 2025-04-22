package com.crud.demo.controllers;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.crud.demo.models.DTO.LoginDTO;
import com.crud.demo.models.DTO.LoginResponseDTO;
import com.crud.demo.services.LoginService;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/api/v1/login")
@RequiredArgsConstructor
@Tag(name = "Login dos Usuários", description = "Operações de autenticação de usuários")
public class LoginController {

    private static final Logger log = LoggerFactory.getLogger(LoginController.class);

    private final LoginService loginService;

    @PostMapping
    @Operation(summary = "Autenticar um usuário", description = "Realiza a autenticação do usuário e retorna o token JWT")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Login realizado com sucesso, retorna o token JWT"),
            @ApiResponse(responseCode = "400", description = "Credenciais inválidas fornecidas")
    })
    public ResponseEntity<LoginResponseDTO> login(
            @Parameter(description = "Credenciais de login (email e senha)") @RequestBody LoginDTO loginDTO) {

        log.debug("[login] Tentando autenticar o usuário: {}", loginDTO.getEmail());

        String token;
        try {
            token = loginService.autentificar(loginDTO);
            log.info("[login] Usuário {} autenticado com sucesso.", loginDTO.getEmail());
        } catch (Exception e) {
            log.error("[login] Falha ao autenticar o usuário {}: {}", loginDTO.getEmail(), e.getMessage());
            return ResponseEntity.badRequest().build();
        }

        LoginResponseDTO loginResponse = loginService.gerarLoginResponse(token);

        log.debug("[login] Token gerado com sucesso para o usuário {}", loginDTO.getEmail());

        return ResponseEntity.ok(loginResponse);
    }

}
