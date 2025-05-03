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
import com.crud.demo.models.DTO.usuario.UsuarioRequestDTO;
import com.crud.demo.models.DTO.usuario.UsuarioResponseDTO;
import com.crud.demo.models.mappers.UsuarioMapper;
import com.crud.demo.repositories.UsuarioRepository;
import com.crud.demo.utils.TestDataFactoryUsuario;
import com.crud.demo.validators.UsuarioValidator;

class UsuarioServiceTest {
    @InjectMocks
    private UsuarioServiceImpl usuarioService;

    @Mock
    private UsuarioRepository usuarioRepository;

    @Mock
    private UsuarioValidator usuarioValidator;

    @Mock
    private PasswordEncoder passwordEncoder;

    @Mock
    private UsuarioMapper usuarioMapper;

    private UsuarioRequestDTO usuarioDTO;
    private Usuario usuarioEntity;
    private UsuarioResponseDTO usuarioResponseDTO;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        usuarioEntity = TestDataFactoryUsuario.criarUsuarioEntity();
        usuarioEntity.setId(1L);

        usuarioResponseDTO = UsuarioResponseDTO.builder()
                .id(usuarioEntity.getId())
                .nome(usuarioEntity.getNome())
                .email(usuarioEntity.getEmail())
                .build();
        usuarioDTO = new UsuarioRequestDTO("João da Silva", "joao@example.com", "senha123");
    }

    @Test
    @DisplayName("Deve cadastrar usuário com sucesso")
    void deveCadastrarUsuarioComSucesso() {

        doNothing().when(usuarioValidator).validarCadastro(usuarioDTO.getEmail());
        when(usuarioMapper.toEntity(usuarioDTO)).thenReturn(usuarioEntity);
        when(passwordEncoder.encode(usuarioEntity.getSenha())).thenReturn("senhaCriptografada");
        when(usuarioRepository.save(usuarioEntity)).thenReturn(usuarioEntity);
        when(usuarioMapper.toDto(usuarioEntity)).thenReturn(usuarioResponseDTO);

        UsuarioResponseDTO usuarioCriado = usuarioService.cadastrarUsuario(usuarioDTO);

        assertNotNull(usuarioCriado);
        assertEquals(usuarioDTO.getNome(), usuarioCriado.getNome());
        assertEquals(usuarioDTO.getEmail(), usuarioCriado.getEmail());
        verify(usuarioRepository, times(1)).save(usuarioEntity);
    }

    @Test
    @DisplayName("Deve buscar usuário por ID com sucesso")
    void deveBuscarUsuarioPorIdComSucesso() {
        when(usuarioValidator.validarExistencia(1L)).thenReturn(usuarioEntity);

        when(usuarioMapper.toDto(usuarioEntity)).thenReturn(usuarioResponseDTO);
        UsuarioResponseDTO usuarioBuscado = usuarioService.buscarUsuarioPorId(1L);

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

        Long id = 1L;
        usuarioEntity.setId(id);

        when(usuarioValidator.validarExistencia(id))
                .thenReturn(usuarioEntity);

        doNothing().when(usuarioValidator)
                .validarCadastro(usuarioDTO.getEmail());

        when(usuarioMapper.toEntity(usuarioDTO))
                .thenReturn(usuarioEntity);

        when(passwordEncoder.encode(usuarioDTO.getSenha()))
                .thenReturn("senhaCriptografada");

        when(usuarioRepository.save(usuarioEntity))
                .thenReturn(usuarioEntity);

        when(usuarioMapper.toDto(usuarioEntity))
                .thenReturn(usuarioResponseDTO);

        UsuarioResponseDTO usuarioAtualizado = usuarioService.atualizarUsuario(id, usuarioDTO);

        assertNotNull(usuarioAtualizado);
        assertEquals(usuarioDTO.getNome(), usuarioAtualizado.getNome());
        assertEquals(usuarioDTO.getEmail(), usuarioAtualizado.getEmail());

        verify(usuarioMapper).toEntity(usuarioDTO);
        verify(usuarioRepository).save(usuarioEntity);
    }

}
