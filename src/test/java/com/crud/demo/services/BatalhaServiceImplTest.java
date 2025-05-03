package com.crud.demo.services;

import static org.assertj.core.api.Assertions.assertThat;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;

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
import org.mockito.Mockito;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;

import com.crud.demo.models.Batalha;
import com.crud.demo.models.DTO.batalha.BatalhaRequestDTO;
import com.crud.demo.models.DTO.batalha.BatalhaResponseDTO;
import com.crud.demo.models.enuns.StatusEnum;
import com.crud.demo.models.mappers.BatalhaMapper;
import com.crud.demo.repositories.BatalhaRepository;
import com.crud.demo.services.contratos.UsuarioService;
import com.crud.demo.validators.UsuarioValidator;

@ExtendWith(MockitoExtension.class)
class BatalhaServiceImplTest {

    @Mock private BatalhaRepository batalhaRepository;
    @Mock private BatalhaMapper batalhaMapper;
    @Mock private UsuarioService usuarioService;
    @Mock private UsuarioValidator usuarioValidator;

    @InjectMocks
    private BatalhaServiceImpl service;

    private BatalhaRequestDTO requestDTO;
    private BatalhaResponseDTO batalhaDTO; 
    private Batalha batalhaEntity;

    @Captor
    private ArgumentCaptor<Pageable> pageableCaptor;

    @BeforeEach
    void setUp() {

        requestDTO = new BatalhaRequestDTO();
        requestDTO.setCriadoEm(LocalDateTime.now());
        requestDTO.setFinalizadoEm(null);
        requestDTO.setStatus(StatusEnum.NAO_INICIADA);

        batalhaDTO = Mockito.mock(BatalhaResponseDTO.class);

        batalhaEntity = new Batalha();
        batalhaEntity.setId(1L);
        batalhaEntity.setCriadoEm(requestDTO.getCriadoEm());
        batalhaEntity.setFinalizadoEm(null);
        batalhaEntity.setStatus(StatusEnum.NAO_INICIADA);
        batalhaEntity.setParticipantes(List.of());
    }


    @Test
    @DisplayName("criarBatalha – deve salvar a batalha e retornar o DTO")
    void deveCriarBatalha() {
        when(batalhaMapper.toEntity(requestDTO)).thenReturn(batalhaEntity);
        when(batalhaRepository.save(batalhaEntity)).thenReturn(batalhaEntity);
        when(batalhaMapper.toDto(batalhaEntity)).thenReturn(batalhaDTO);

        BatalhaResponseDTO resultado = service.criarBatalha(requestDTO);

        assertThat(resultado).isSameAs(batalhaDTO);
        verify(batalhaRepository).save(batalhaEntity);
    }



    @Test
    @DisplayName("deletarBatalha – deve validar a existência e deletar")
    void deveDeletarBatalha() {
        when(batalhaRepository.findById(1L)).thenReturn(Optional.of(batalhaEntity));

        service.deletarBatalha(1L);

        verify(batalhaRepository).deleteById(1L);
    }



    @Test
    @DisplayName("getBatalhaById – deve retornar batalha mapeada")
    void deveBuscarBatalhaPorId() {
        Long id = 1L;

        when(batalhaRepository.findById(id)).thenReturn(Optional.of(batalhaEntity));
        when(batalhaMapper.toDto(batalhaEntity)).thenReturn(batalhaDTO);

        BatalhaResponseDTO resultado = service.getBatalhaById(id);

        assertThat(resultado).isSameAs(batalhaDTO);
    }


    @Test
    @DisplayName("exibirTodasAsBatalhas – deve devolver página mapeada")
    void deveListarBatalhasPaginado() {

      
        Page<Batalha> pageEntities = new PageImpl<>(List.of(batalhaEntity));

        when(batalhaRepository.findAll(any(Pageable.class))).thenReturn(pageEntities);
        when(batalhaMapper.toDto(batalhaEntity)).thenReturn(batalhaDTO);

        int page = 1;
        int size = 10;
        String sortBy = "id";
        String direction = "asc";

        Page<BatalhaResponseDTO> resultado =
                service.exibirTodasAsBatalhas(page, size, sortBy, direction);

        verify(batalhaRepository).findAll(pageableCaptor.capture());
        Pageable pageableUsed = pageableCaptor.getValue();

        assertThat(pageableUsed.getPageNumber()).isEqualTo(page);
        assertThat(pageableUsed.getPageSize()).isEqualTo(size);
        assertThat(pageableUsed.getSort().getOrderFor(sortBy).getDirection())
              .isEqualTo(Sort.Direction.ASC);

        assertThat(resultado.getContent()).containsExactly(batalhaDTO);
    }
}
