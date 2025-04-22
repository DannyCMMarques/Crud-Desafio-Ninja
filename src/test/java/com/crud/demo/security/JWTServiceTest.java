package com.crud.demo.security;

import java.util.Date;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import static org.junit.jupiter.api.Assertions.assertTrue;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.security.core.userdetails.User;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.test.util.ReflectionTestUtils;

import com.crud.demo.security.Service.JWTService;

public class JWTServiceTest {

    private JWTService jwtService;
    private static final long DEFAULT_EXPIRATION_MS = 10_000L;
    private static final long SHORT_EXPIRATION_MS = 1_000L;

    @BeforeEach
    void setUp() {
        jwtService = new JWTService();
        ReflectionTestUtils.setField(jwtService, "secretKey", "MDEyMzQ1Njc4OTAxMjM0NTY3ODkwMTIzNDU2Nzg5MDE=");
        ReflectionTestUtils.setField(jwtService, "jwtExpiration", DEFAULT_EXPIRATION_MS);
    }

    @Test
        @DisplayName("Deve gerar token, extrair claims e validar token com sucesso")
    void deveGerarTokenExtrairClaimsEValidarComSucesso() {
        UserDetails user = User.withUsername("testUser")
                .password("password")
                .roles("USER")
                .build();

        String token = jwtService.gerarToken(user);

        String username = jwtService.extrairUsername(token);
        assertEquals("testUser", username, "O nome de usuário extraído deve corresponder");

        String role = jwtService.extrairRole(token);
        assertEquals("USER", role, "A role extraída deve ser 'USER'");

        Date createdAt = jwtService.extrairCreatedAt(token);
        assertNotNull(createdAt, "A data de criação deve ser definida");

        Date expiration = jwtService.extrairExpiracao(token);
        assertTrue(expiration.after(new Date()), "A data de expiração deve estar no futuro");

        boolean isValid = jwtService.isTokenValido(token, user);
        assertTrue(isValid, "O token recém-gerado deve ser válido");
    }

    @Test
    @DisplayName("Deve retornar tempo de expiração configurado corretamente")
    void deveRetornarTempoExpiracaoConfiguradoComSucesso() {
        assertEquals(DEFAULT_EXPIRATION_MS, jwtService.getTempoExpiracao(),
                "O tempo de expiração deve corresponder ao valor injetado");
    }
}