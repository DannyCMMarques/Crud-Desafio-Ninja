package com.crud.demo.config;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;

import com.crud.demo.models.Usuario;
import com.crud.demo.repositories.UsuarioRepository;

class ApplicationConfigurationTest {

    private ApplicationConfiguration config;
    private UsuarioRepository usuarioRepository;

    @BeforeEach
    void setUp() {
        usuarioRepository = mock(UsuarioRepository.class);
        config = new ApplicationConfiguration(usuarioRepository);
    }

    @Test
    @DisplayName("Deve retornar usuário quando existir via userDetailsService")
    void deveRetornarUsuarioQuandoExistirViaUserDetailsService() {
        Usuario user = new Usuario();
        user.setId(5L);
        user.setNome("Teste");
        user.setEmail("teste@ex.com");
        user.setSenha("senha");
        when(usuarioRepository.findByEmail("teste@ex.com"))
            .thenReturn(Optional.of(user));

        UserDetailsService uds = config.userDetailsService();
        var loaded = uds.loadUserByUsername("teste@ex.com");

        assertSame(user, loaded);
        verify(usuarioRepository).findByEmail("teste@ex.com");
    }

    @Test
    @DisplayName("Deve lançar UsernameNotFoundException quando usuário não existir")
    void deveLancarExceptionQuandoUsuarioNaoExistir() {
        when(usuarioRepository.findByEmail(anyString()))
            .thenReturn(Optional.empty());

        UserDetailsService uds = config.userDetailsService();
        assertThrows(UsernameNotFoundException.class,
            () -> uds.loadUserByUsername("inexistente@ex.com"));
    }

    @Test
    @DisplayName("Deve codificar senha com BCrypt e validar matches corretamente")
    void deveCodificarSenhaComBCryptEValidarMatches() {
        BCryptPasswordEncoder encoder = config.passwordEncoder();
        String raw = "minhaSenha";
        String hash = encoder.encode(raw);
        assertTrue(encoder.matches(raw, hash));
    }

    @Test
    @DisplayName("Deve delegar authenticationManager para AuthenticationConfiguration")
    void deveDelegarAuthenticationManagerParaAuthenticationConfiguration() throws Exception {
        AuthenticationConfiguration ac = mock(AuthenticationConfiguration.class);
        AuthenticationManager amMock = mock(AuthenticationManager.class);
        when(ac.getAuthenticationManager()).thenReturn(amMock);

        AuthenticationManager result = config.authenticationManager(ac);
        assertSame(amMock, result);
    }
}
