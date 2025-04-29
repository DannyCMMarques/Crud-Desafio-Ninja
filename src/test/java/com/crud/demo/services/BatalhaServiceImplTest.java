package com.crud.demo.services;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import java.time.LocalDateTime;
import java.util.List;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.ArgumentCaptor;
import org.mockito.Captor;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import com.crud.demo.models.Batalha;
import com.crud.demo.models.ParticipanteBatalha;
import com.crud.demo.models.Usuario;
import com.crud.demo.models.DTO.BatalhaDTO;
import com.crud.demo.models.DTO.BatalhaRequestDTO;
import com.crud.demo.models.DTO.ParticipanteBatalhaDTO;
import com.crud.demo.models.enuns.StatusEnum;
import com.crud.demo.models.mappers.BatalhaMapper;
import com.crud.demo.models.mappers.ParticipanteBatalhaMapper;
import com.crud.demo.models.mappers.PersonagemMapper;
import com.crud.demo.repositories.BatalhaRepository;
import com.crud.demo.validators.UsuarioValidator;

@ExtendWith(MockitoExtension.class)
class BatalhaServiceImplTest {

    @Mock
    private BatalhaRepository batalhaRepository;
    @Mock
    private BatalhaMapper batalhaMapper;
    @Mock
    private ParticipanteBatalhaMapper participanteBatalhaMapper;
    @Mock
    private UsuarioService usuarioService;
    @Mock
    private PersonagemMapper personagemMapper;
    @Mock
    private UsuarioValidator usuarioValidator;

    @InjectMocks
    private BatalhaServiceImpl service;

    private BatalhaRequestDTO requestDTO;
    private BatalhaDTO batalhaDTO;
    private Batalha batalhaEntity;
    private Pageable pageable;

    @BeforeEach
    void setUp() {
        requestDTO = new BatalhaRequestDTO();
        requestDTO.setCriadoEm(LocalDateTime.now());
        requestDTO.setFinalizadoEm(null);
        requestDTO.setStatus(StatusEnum.NAO_INICIADA);

        batalhaDTO = new BatalhaDTO();
        batalhaDTO.setId(1L);
        batalhaDTO.setCriadoEm(requestDTO.getCriadoEm());
        batalhaDTO.setFinalizadoEm(null);
        batalhaDTO.setStatus(StatusEnum.NAO_INICIADA);
        batalhaDTO.setParticipantesBatalha(List.of());

        batalhaEntity = new Batalha();
        batalhaEntity.setId(1L);
        batalhaEntity.setCriadoEm(requestDTO.getCriadoEm());
        batalhaEntity.setFinalizadoEm(null);
        batalhaEntity.setStatus(StatusEnum.NAO_INICIADA);
        batalhaEntity.setParticipantes(List.of());
        int size = 10;
        int page = 1;
        String sort = "nome";
        pageable = PageRequest.of(page, size, Sort.by(sort));

    }

    @Test
    @DisplayName("criarBatalha  Deve salvar batalha e retornar DTO")
    void deveCriarBatalha() {
        when(batalhaMapper.fromRequest(requestDTO, List.of()))
                .thenReturn(batalhaDTO);
        when(batalhaMapper.toEntity(batalhaDTO))
                .thenReturn(batalhaEntity);
        when(batalhaRepository.save(batalhaEntity))
                .thenReturn(batalhaEntity);
        when(batalhaMapper.toDto(batalhaEntity))
                .thenReturn(batalhaDTO);

        BatalhaDTO resultado = service.criarBatalha(requestDTO);

        assertThat(resultado).isEqualTo(batalhaDTO);
        verify(batalhaRepository).save(batalhaEntity);
    }

    @Test
    @DisplayName("deletarBatalha  Deve validar existência e deletar")
    void deveDeletarBatalha() {
        when(batalhaMapper.toDto(batalhaEntity)).thenReturn(batalhaDTO);
        when(batalhaRepository.findById(1L)).thenReturn(java.util.Optional.of(batalhaEntity));

        service.deletarBatalha(1L);

        verify(batalhaRepository).deleteById(1L);
    }

    
    @Test
    @DisplayName("getBatalhaById deve retornar batalha mapeada")
    void deveBuscarBatalhaPorId() {
        Long id = 1L;

        when(batalhaRepository.findById(id))
                .thenReturn(Optional.of(batalhaEntity));

        when(batalhaMapper.toDto(batalhaEntity))
                .thenReturn(batalhaDTO);

        BatalhaDTO resultado = service.getBatalhaById(id);

        assertThat(resultado).isEqualTo(batalhaDTO);
    }



    @Captor
    private ArgumentCaptor<Pageable> pageableCaptor;

    @Test
    @DisplayName("exibirTodasAsBatalhas deve devolver página mapeada")
    void deveListarBatalhasPaginado() {
        Page<Batalha> pageEntities = new PageImpl<>(List.of(batalhaEntity));
        when(batalhaRepository.findAll(any(Pageable.class)))
                .thenReturn(pageEntities);
        when(batalhaMapper.toDto(batalhaEntity))
                .thenReturn(batalhaDTO);

        int page = 1;
        int size = 10;
        String sortBy = "id";
        String direction = "asc";

        Page<BatalhaDTO> resultado = service.exibirTodasAsBatalhas(page, size, sortBy, direction);

        verify(batalhaRepository).findAll(pageableCaptor.capture());
        Pageable pageableUsed = pageableCaptor.getValue();

        assertThat(pageableUsed.getPageNumber()).isEqualTo(1);
        assertThat(pageableUsed.getPageSize()).isEqualTo(10);
        assertThat(pageableUsed.getSort().getOrderFor("id").getDirection())
                .isEqualTo(Sort.Direction.ASC);

        assertThat(resultado.getContent()).hasSize(1);
        assertThat(resultado.getContent().get(0).getId()).isEqualTo(1L);
    }

}
