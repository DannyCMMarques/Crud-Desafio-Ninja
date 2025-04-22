package com.crud.demo.services;


import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.MockitoAnnotations;
import org.springframework.security.crypto.password.PasswordEncoder;

import com.crud.demo.models.Usuario;
import com.crud.demo.models.DTO.RegistroUsuarioDTO;
import com.crud.demo.models.mappers.UsuarioMapper;
import com.crud.demo.repositories.UsuarioRepository;
import com.crud.demo.utils.TestDataFactoryUsuario;
import com.crud.demo.validators.UsuarioValidator;

class UsuarioServiceTest {
@InjectMocks
    private UsuarioService usuarioService;

    @Mock
    private UsuarioRepository usuarioRepository;

    @Mock
    private UsuarioValidator usuarioValidator;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private UsuarioMapper usuarioMapper;


    private RegistroUsuarioDTO usuarioDTO;
    private Usuario usuarioEntity;
    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
         usuarioDTO = TestDataFactoryUsuario.criarUsuarioDTO();
         usuarioEntity = TestDataFactoryUsuario.criarUsuarioEntity();
    }

    @Test
        @DisplayName("Deve cadastrar usuário com sucesso")
    void deveCadastrarUsuarioComSucesso() {


        doNothing().when(usuarioValidator).validarCadastro(usuarioDTO.getEmail());
        when(usuarioMapper.toEntity(usuarioDTO)).thenReturn(usuarioEntity);
        when(passwordEncoder.encode(usuarioEntity.getSenha())).thenReturn("senhaCriptografada");
        when(usuarioRepository.save(usuarioEntity)).thenReturn(usuarioEntity);
        when(usuarioMapper.toDto(usuarioEntity)).thenReturn(usuarioDTO);

        RegistroUsuarioDTO usuarioCriado = usuarioService.cadastrarUsuario(usuarioDTO);

        assertNotNull(usuarioCriado);
        assertEquals(usuarioDTO.getNome(), usuarioCriado.getNome());
        assertEquals(usuarioDTO.getEmail(), usuarioCriado.getEmail());
        verify(usuarioRepository, times(1)).save(usuarioEntity);
    }
    @Test
    @DisplayName("Deve buscar usuário por ID com sucesso")
    void deveBuscarUsuarioPorIdComSucesso() {

        when(usuarioValidator.validarExistencia(usuarioEntity.getId())).thenReturn(usuarioEntity);
        when(usuarioMapper.toDto(usuarioEntity)).thenReturn(usuarioDTO);

        RegistroUsuarioDTO usuarioBuscado = usuarioService.buscarUsuarioPorId(usuarioEntity.getId());

        assertNotNull(usuarioBuscado);
        assertEquals(usuarioDTO.getNome(), usuarioBuscado.getNome());
        verify(usuarioValidator, times(1)).validarExistencia(usuarioEntity.getId());
    }

    @Test
    @DisplayName("Deve deletar usuário com sucesso")
    void deveDeletarUsuarioComSucesso() {

        when(usuarioValidator.validarExistencia(usuarioEntity.getId())).thenReturn(usuarioEntity);

        usuarioService.deleteById(usuarioEntity.getId());

        verify(usuarioRepository, times(1)).deleteById(usuarioEntity.getId());
    }

    @Test
    @DisplayName("Deve atualizar usuário com sucesso")
    void deveAtualizarUsuarioComSucesso() {

        RegistroUsuarioDTO usuarioAtualizadoDTO = TestDataFactoryUsuario.criarUsuarioDTO();

        when(usuarioValidator.validarExistencia(usuarioEntity.getId())).thenReturn(usuarioEntity);
        when(usuarioMapper.toDto(usuarioEntity)).thenReturn(usuarioAtualizadoDTO);
        when(usuarioRepository.save(usuarioEntity)).thenReturn(usuarioEntity);
        when(passwordEncoder.encode(usuarioEntity.getSenha())).thenReturn("senhaCriptografada");

        RegistroUsuarioDTO usuarioAtualizado = usuarioService.atualizarUsuario(usuarioEntity.getId(), usuarioDTO);

        assertNotNull(usuarioAtualizado);
        assertEquals(usuarioDTO.getNome(), usuarioAtualizado.getNome());
        assertEquals(usuarioDTO.getEmail(), usuarioAtualizado.getEmail());
        verify(usuarioRepository, times(1)).save(usuarioEntity);
    }
}
