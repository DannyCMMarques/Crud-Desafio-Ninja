package com.crud.demo.services;

import static org.assertj.core.api.Assertions.*;
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

import com.crud.demo.models.Batalha;
import com.crud.demo.models.ParticipanteBatalha;
import com.crud.demo.models.Personagem;
import com.crud.demo.models.Usuario;
import com.crud.demo.models.DTO.participanteBatalha.ParticipanteBatalhaRequestDTO;
import com.crud.demo.models.DTO.participanteBatalha.ParticipanteBatalhaResponseDTO;
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

        private ParticipanteBatalhaResponseDTO dto;
        private ParticipanteBatalha entity;
        private Usuario usuario;
        private ParticipanteBatalhaRequestDTO requestDTO;
        private Personagem naruto;
        private Batalha batalha = new Batalha();

        @BeforeEach
        void setUp() {

                requestDTO = new ParticipanteBatalhaRequestDTO(1L, "Joao", "naruto", 1, false);
                dto = ParticipanteBatalhaResponseDTO.builder()
                                .id(1L)
                                .batalha(1L)
                                .personagem("naruto")
                                .nomeUsuario("Joao")
                                .playerOrder(1)
                                .vencedor(false)
                                .build();

                usuario = new Usuario();
                usuario.setId(11L);

                naruto = new Personagem();
                entity = new ParticipanteBatalha();

                entity.setId(1L);
                entity.setBatalha(batalha);
                entity.setPersonagem(naruto);
                entity.setUsuario(usuario);
                entity.setPlayerOrder(1);
                entity.setVencedor(false);
        }

        @Test
        @DisplayName("criarParticipanteBatalha – Deve salvar e devolver DTO")
        void deveCriarParticipante() {
                when(usuarioValidator.validarExistencia("Joao"))
                                .thenReturn(usuario);

                when(participanteMapper.toEntity(requestDTO))
                                .thenReturn(entity);
                when(participanteRepo.save(entity))
                                .thenReturn(entity);
                when(participanteMapper.toDto(entity))
                                .thenReturn(dto);

                ParticipanteBatalhaResponseDTO resultado = service.criarParticipanteBatalha(requestDTO);

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

                ParticipanteBatalhaResponseDTO resultado = service.getParticipanteBatalhaById(1L);

                assertThat(resultado).isEqualTo(dto);
                verify(participanteValidator).verificarParticipanteBatalhaDTO(1L);
        }

        @Test
        @DisplayName("atualizarParticipanteBatalha – Deve atualizar campos e retornar DTO")
        void deveAtualizarParticipante() {
                when(participanteValidator.verificarParticipanteBatalhaDTO(1L))
                                .thenReturn(entity);

                ParticipanteBatalhaResponseDTO dtoOriginal = dto;
                ParticipanteBatalhaResponseDTO dtoAtualizado = ParticipanteBatalhaResponseDTO.builder()
                                .id(1L).batalha(1L).personagem("naruto").nomeUsuario("Joao")
                                .playerOrder(2).vencedor(true)
                                .build();

                when(participanteMapper.toDto(entity))
                                .thenReturn(dtoOriginal)
                                .thenReturn(dtoAtualizado);

                when(participanteMapper.toEntity(dtoOriginal))
                                .thenReturn(entity);

                when(participanteRepo.save(entity))
                                .thenReturn(entity);

                requestDTO.setPlayerOrder(2);
                requestDTO.setVencedor(true);

                ParticipanteBatalhaResponseDTO resultado = service.atualizarParticipanteBatalha(1L, requestDTO);

                assertThat(resultado.getPlayerOrder()).isEqualTo(2);
                assertThat(resultado.getVencedor()).isTrue();
                verify(participanteRepo).save(entity);
        }

        @Test
        @DisplayName("listarTodosParticipantes – Deve retornar página de DTOs")
        void deveListarParticipantes() {
                Page<ParticipanteBatalha> pageEntities = new PageImpl<>(List.of(entity));
                when(participanteRepo.findAll(any(PageRequest.class)))
                                .thenReturn(pageEntities);
                when(participanteMapper.toDto(entity))
                                .thenReturn(dto);

                Page<ParticipanteBatalhaResponseDTO> resultado = service.listarTodosParticipantes("asc", "id", 0, 10);

                assertThat(resultado.getContent()).hasSize(1)
                                .extracting(ParticipanteBatalhaResponseDTO::getId)
                                .containsExactly(1L);
        }
}
