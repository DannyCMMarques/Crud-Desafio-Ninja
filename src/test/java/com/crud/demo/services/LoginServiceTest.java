package com.crud.demo.services;

import java.util.Date;
import java.util.function.Function;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertSame;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentMatcher;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.argThat;
import static org.mockito.ArgumentMatchers.eq;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.Authentication;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;

import com.crud.demo.models.DTO.LoginDTO;
import com.crud.demo.models.DTO.LoginResponseDTO;
import com.crud.demo.security.Service.JWTService;
import com.crud.demo.validators.UsuarioValidator;

import io.jsonwebtoken.Claims;

@ExtendWith(MockitoExtension.class)
class LoginServiceTest {

    @Mock
    private AuthenticationManager authenticationManager;

    @Mock
    private UsuarioValidator usuarioValidator;

    @Mock
    private JWTService jwtService;

    @InjectMocks
    private LoginService loginService;

    @BeforeEach
    void setUp() {
        SecurityContextHolder.clearContext();
    }

    @Test
    @DisplayName("Deve autenticar com sucesso")
    void testAutentificar_Success() {
        LoginDTO dto = new LoginDTO();
        dto.setEmail("user@example.com");
        dto.setSenha("password123");

        UserDetails userDetails = mock(UserDetails.class);
        Authentication auth = mock(Authentication.class);

        when(authenticationManager.authenticate(argThat(
                (ArgumentMatcher<UsernamePasswordAuthenticationToken>) token -> "user@example.com"
                        .equals(token.getName()) &&
                        "password123".equals(token.getCredentials()))))
                .thenReturn(auth);

        when(auth.getPrincipal()).thenReturn(userDetails);

        String fakeToken = "jwt-token-xyz";
        when(jwtService.gerarToken(userDetails)).thenReturn(fakeToken);

        String token = loginService.autentificar(dto);

        assertEquals(fakeToken, token, "Deve retornar o token gerado pelo JWTService");
        assertSame(auth, SecurityContextHolder.getContext().getAuthentication());
        verify(authenticationManager).authenticate(any(UsernamePasswordAuthenticationToken.class));
        verify(jwtService).gerarToken(userDetails);
    }

    @Test
    @DisplayName("Deve gerar resposta de login com sucesso")
    void deveGerarRespostaDeLoginComSucesso() {
        String token = "any-token";

        String expectedUser = "user@example.com";
        when(jwtService.extrairUsername(token)).thenReturn(expectedUser);

        Date issuedAt = new Date(1_000L * 60);
        Date expiration = new Date(1_000L * 120);

        when(jwtService.extrairClaim(eq(token), any(Function.class))).thenAnswer(invocation -> {
            @SuppressWarnings("unchecked")
            Function<Claims, ?> resolver = invocation.getArgument(1);
            Claims claims = mock(Claims.class);
            when(claims.getIssuedAt()).thenReturn(issuedAt);
            return resolver.apply(claims);
        });

        when(jwtService.extrairExpiracao(token)).thenReturn(expiration);

        LoginResponseDTO resp = loginService.gerarLoginResponse(token);

        assertEquals(token, resp.getToken());
        assertEquals("USER", resp.getRole());
        assertEquals(issuedAt.toString(), resp.getCreatedAt());
        assertEquals(expiration, resp.getExp());
        assertEquals(issuedAt.getTime() / 1000, resp.getIat());
    }
}
