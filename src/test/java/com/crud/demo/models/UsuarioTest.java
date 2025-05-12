package com.crud.demo.models;

import static org.junit.jupiter.api.Assertions.*;

import java.util.Collection;
import java.util.Date;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class UsuarioTest {

    @Test
    @DisplayName("Deve preencher todos os campos corretamente usando o builder")
    void devePreencherTodosOsCamposCorretamenteUsandoBuilder() {
        Date now = new Date();
        Usuario u = new Usuario();
        u.setId(1L);
        u.setNome("Teste");
        u.setEmail("teste@example.com");
        u.setSenha("segredo");
        u.setCreatedAt(now);
        u.setUpdatedAt(now);

        assertEquals(1L, u.getId());
        assertEquals("Teste", u.getNome());
        assertEquals("teste@example.com", u.getEmail());
        assertEquals("segredo", u.getSenha());
        assertEquals(now, u.getCreatedAt());
        assertEquals(now, u.getUpdatedAt());
    }

    @Test
    @DisplayName("Deve implementar UserDetails com username, password, authorities e flags padrão")
    void deveImplementarUserDetailsComUsernameSenhaAuthoritiesEFlagsPadrao() {
        Usuario u = new Usuario();
        u.setEmail("user@dominio.com");
        u.setSenha("12345");

        assertEquals("user@dominio.com", u.getUsername());
        assertEquals("12345", u.getPassword());

        Collection<?> auth = u.getAuthorities();
        assertNotNull(auth);
        assertTrue(auth.isEmpty(), "Lista de authorities deve estar vazia");

        assertTrue(u.isAccountNonExpired(), "Conta não expirada");
        assertTrue(u.isAccountNonLocked(), "Conta não bloqueada");
        assertTrue(u.isCredentialsNonExpired(), "Credenciais não expiradas");
        assertTrue(u.isEnabled(), "Conta habilitada");
    }
}
