package com.crud.demo.services;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;

import com.crud.demo.models.ParticipanteBatalha;
import com.crud.demo.models.Usuario;
import com.crud.demo.models.DTO.ParticipanteBatalhaDTO;
import com.crud.demo.models.mappers.ParticipanteBatalhaMapper;
import com.crud.demo.repositories.ParticipanteBatalhaRepository;
import com.crud.demo.validators.ParticipanteBatalhaValidator;
import com.crud.demo.validators.PersonagemValidator;
import com.crud.demo.validators.UsuarioValidator;

@ExtendWith(MockitoExtension.class)
class ParticipanteBatalhaServiceImplTest {

    @Mock
    private ParticipanteBatalhaRepository participanteRepo;
    @Mock
    private ParticipanteBatalhaValidator participanteValidator;
    @Mock
    private ParticipanteBatalhaMapper participanteMapper;
    @Mock
    private UsuarioValidator usuarioValidator;
    @Mock
    private PersonagemValidator personagemValidator;

    @InjectMocks
    private ParticipanteBatalhaServiceImpl service;

    private ParticipanteBatalhaDTO dto;
    private ParticipanteBatalha entity;
    private Usuario usuario;

    @BeforeEach
    void setUp() {
        dto = ParticipanteBatalhaDTO.builder()
                .id(1L)
                .nomeUsuario("naruto")
                .playerOrder(1)
                .vencedor(false)
                .build();

        usuario = new Usuario();
        usuario.setId(11L);

        entity = new ParticipanteBatalha();
        entity.setId(1L);
        entity.setUsuario(usuario);
        entity.setPlayerOrder(1);
        entity.setVencedor(false);
    }


    @Test
    @DisplayName("criarParticipanteBatalha – Deve salvar e devolver DTO")
    void deveCriarParticipante() {
        when(usuarioValidator.validarExistencia("naruto"))
                .thenReturn(usuario);
        when(participanteMapper.toEntity(dto, usuario))
                .thenReturn(entity);
        when(participanteRepo.save(entity))
                .thenReturn(entity);
        when(participanteMapper.toDto(entity))
                .thenReturn(dto);

        ParticipanteBatalhaDTO resultado = service.criarParticipanteBatalha(dto);

        assertThat(resultado).isEqualTo(dto);
        verify(participanteRepo).save(entity);
    }


    @Test
    @DisplayName("deletarParticipanteBatalha – Deve validar e deletar")
    void deveDeletarParticipante() {
        when(participanteValidator.verificarParticipanteBatalhaDTO(1L))
                .thenReturn(entity);

        service.deletarParticipanteBatalha(1L);

        verify(participanteValidator).verificarParticipanteBatalhaDTO(1L);
        verify(participanteRepo).deleteById(1L);
    }

    @Test
    @DisplayName("getParticipanteBatalhaById – Deve retornar DTO correto")
    void deveBuscarParticipantePorId() {
        when(participanteValidator.verificarParticipanteBatalhaDTO(1L))
                .thenReturn(entity);
        when(participanteMapper.toDto(entity))
                .thenReturn(dto);

        ParticipanteBatalhaDTO resultado = service.getParticipanteBatalhaById(1L);

        assertThat(resultado).isEqualTo(dto);
        verify(participanteValidator).verificarParticipanteBatalhaDTO(1L);
    }

    @Test
    @DisplayName("atualizarParticipanteBatalha – Deve atualizar campos e retornar DTO")
    void deveAtualizarParticipante() {
        ParticipanteBatalhaDTO dtoAtualizado = ParticipanteBatalhaDTO.builder()
                .id(1L)
                .playerOrder(2)
                .vencedor(true)
                .build();

        ParticipanteBatalha entityAtualizado = new ParticipanteBatalha();
        entityAtualizado.setId(1L);
        entityAtualizado.setPlayerOrder(2);
        entityAtualizado.setVencedor(true);

        when(participanteValidator.verificarParticipanteBatalhaDTO(1L))
                .thenReturn(entity);
        when(participanteRepo.save(entity))
                .thenReturn(entityAtualizado);
        when(participanteMapper.toDto(entityAtualizado))
                .thenReturn(dtoAtualizado);

        ParticipanteBatalhaDTO resultado = service.atualizarParticipanteBatalha(1L, dtoAtualizado);

        assertThat(resultado.getPlayerOrder()).isEqualTo(2);
        assertThat(resultado.getVencedor()).isTrue();
    }

  
    @Test
    @DisplayName("listarTodosParticipantes – Deve retornar página de DTOs")
    void deveListarParticipantes() {
        Page<ParticipanteBatalha> pageEntities = new PageImpl<>(List.of(entity));
        when(participanteRepo.findAll(any(PageRequest.class)))
                .thenReturn(pageEntities);
        when(participanteMapper.toDto(entity))
                .thenReturn(dto);

        Page<ParticipanteBatalhaDTO> resultado = service.listarTodosParticipantes(PageRequest.of(0, 10));

        assertThat(resultado.getContent()).hasSize(1)
                .extracting(ParticipanteBatalhaDTO::getId)
                .containsExactly(1L);
    }
}
