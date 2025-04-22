package com.crud.demo.validators;


import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.Optional;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.crud.demo.Exceptions.usuarioException.EmailJaCadastradoException;
import com.crud.demo.Exceptions.usuarioException.UsuarioNaoEncontradoException;
import com.crud.demo.models.Usuario;
import com.crud.demo.repositories.UsuarioRepository;

@ExtendWith(MockitoExtension.class)
class UsuarioValidatorTest {

    @Mock
    private UsuarioRepository usuarioRepository;

    @InjectMocks
    private UsuarioValidator validator;

    @Test
    @DisplayName("Deve validar cadastro sem erro quando email não existir")
    void deveValidarCadastroSemErroQuandoEmailNaoExistir() {
        String email = "joao@example.com";
        when(usuarioRepository.findByEmail(email))
            .thenReturn(Optional.empty());

        assertDoesNotThrow(() -> validator.validarCadastro(email));
        verify(usuarioRepository).findByEmail(email);
    }

    @Test
    @DisplayName("Deve lançar EmailJaCadastradoException quando email já estiver cadastrado")
    void deveLancarExceptionQuandoEmailJaCadastrado() {
        String email = "maria@example.com";
        Usuario existente = new Usuario();
        existente.setEmail(email);
        when(usuarioRepository.findByEmail(email))
            .thenReturn(Optional.of(existente));

        EmailJaCadastradoException ex = assertThrows(
            EmailJaCadastradoException.class,
            () -> validator.validarCadastro(email)
        );
        assertTrue(ex.getMessage().contains(email));
        verify(usuarioRepository).findByEmail(email);
    }

    @Test
    @DisplayName("Deve retornar usuário quando validar existência por ID existente")
    void deveRetornarUsuarioQuandoIdExistir() {
        Long id = 2L;
        Usuario u = new Usuario();
        u.setId(id);
        when(usuarioRepository.findById(id))
            .thenReturn(Optional.of(u));

        Usuario resultado = validator.validarExistencia(id);

        assertNotNull(resultado);
        assertEquals(id, resultado.getId());
        verify(usuarioRepository).findById(id);
    }

    @Test
    @DisplayName("Deve lançar UsuarioNaoEncontradoException quando ID não existir")
    void deveLancarExceptionQuandoIdNaoExistir() {
        Long id = 99L;
        when(usuarioRepository.findById(id))
            .thenReturn(Optional.empty());

        assertThrows(
            UsuarioNaoEncontradoException.class,
            () -> validator.validarExistencia(id)
        );
        verify(usuarioRepository).findById(id);
    }

    @Test
    @DisplayName("Deve retornar usuário quando validar existência de login por email existente")
    void deveRetornarUsuarioQuandoEmailExistir() {
        String email = "ana@example.com";
        Usuario u = new Usuario();
        u.setEmail(email);
        when(usuarioRepository.findByEmail(email))
            .thenReturn(Optional.of(u));

        Usuario resultado = validator.validarExistenciaLogin(email);

        assertNotNull(resultado);
        assertEquals(email, resultado.getEmail());
        verify(usuarioRepository).findByEmail(email);
    }

    @Test
    @DisplayName("Deve lançar UsuarioNaoEncontradoException quando validar login por email inexistente")
    void deveLancarExceptionQuandoEmailNaoExistir() {
        String email = "inexistente@example.com";
        when(usuarioRepository.findByEmail(email))
            .thenReturn(Optional.empty());

        assertThrows(
            UsuarioNaoEncontradoException.class,
            () -> validator.validarExistenciaLogin(email)
        );
        verify(usuarioRepository).findByEmail(email);
    }
}
