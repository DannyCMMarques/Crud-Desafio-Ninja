package com.crud.demo.services;

import static org.assertj.core.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import java.util.HashMap;
import java.util.Map;
import java.util.Optional;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

import com.crud.demo.Exceptions.jutsusExceptions.JutsuNaoEncontradoException;
import com.crud.demo.events.EstatisticaDoJogadorEvent;
import com.crud.demo.models.Jutsu;
import com.crud.demo.models.Personagem;
import com.crud.demo.models.DTO.jutsu.JutsuResponseDTO;
import com.crud.demo.models.enuns.CategoriaEspecialidadeEnum;
import com.crud.demo.models.mappers.JutsuMapper;
import com.crud.demo.repositories.JutsuRepository;

@ExtendWith(MockitoExtension.class)
class JutsuServiceImplTest {

    @Mock
    private JutsuRepository jutsuRepository;

    @Mock
    private JutsuMapper jutsuMapper;

    @InjectMocks
    private JutsuServiceImpl jutsuService;

    private EstatisticaDoJogadorEvent estatistica;
    private Personagem personagem;
    private Map<String, Jutsu> jutsus;
    private Map<String, JutsuResponseDTO> jutsuResponseMap;
    private Jutsu jutsuEntity;
    private JutsuResponseDTO jutsuResponseDTO;

    @BeforeEach
    void setUp() {
        personagem = Personagem.builder()
                .id(1L)
                .nome("Naruto")
                .especialidade(CategoriaEspecialidadeEnum.NINJUTSU)
                .build();

        estatistica = EstatisticaDoJogadorEvent.builder()
                .personagem(personagem)
                .historicoDeJutsus(new HashMap<>())
                .build();

        jutsus = new HashMap<>();
        jutsus.put("Rasengan", Jutsu.builder().id(1L).tipo("Rasengan").build());
        jutsus.put("Chidori", Jutsu.builder().id(2L).tipo("Chidori").build());

        jutsuResponseMap = new HashMap<>();
        jutsuResponseMap.put("Rasengan", JutsuResponseDTO.builder().id(1L).tipo("Rasengan").build());
        jutsuResponseMap.put("Chidori", JutsuResponseDTO.builder().id(2L).tipo("Chidori").build());

        jutsuEntity = Jutsu.builder()
                .id(1L)
                .tipo("Rasengan")
                .build();

        jutsuResponseDTO = JutsuResponseDTO.builder()
                .id(1L)
                .tipo("Rasengan")
                .build();
    }

    @Test
    @DisplayName("Deve obter jutsus disponíveis removendo já utilizados")
    void deveObterJutsusDisponiveis() {

        when(jutsuRepository.findMapByCategoria(CategoriaEspecialidadeEnum.NINJUTSU)).thenReturn(jutsus);
        when(jutsuMapper.mapToDto(anyMap())).thenReturn(jutsuResponseMap);

        estatistica.getHistoricoDeJutsus().put("Rasengan", JutsuResponseDTO.builder().id(1L).tipo("Rasengan").build());

        Map<String, JutsuResponseDTO> disponiveis = jutsuService.obterJutsusDisponiveis(estatistica);

        assertThat(disponiveis).containsKey("Chidori");
        assertThat(disponiveis).doesNotContainKey("Rasengan");
    }

    @Test
    @DisplayName("Deve adicionar jutsu ao histórico")
    void deveAdicionarHistorico() {
        JutsuResponseDTO jutsu = JutsuResponseDTO.builder().id(1L).tipo("Rasengan").build();

        jutsuService.adicionarHistorico(estatistica, jutsu);

        assertThat(estatistica.getHistoricoDeJutsus()).containsKey("Rasengan");
        assertThat(estatistica.getHistoricoDeJutsus().get("Rasengan")).isEqualTo(jutsu);
    }

    @Test
    @DisplayName("Deve retornar jutsu quando encontrado por ID")
    void deveRetornarJutsuPorId() {
        when(jutsuRepository.findById(1L)).thenReturn(Optional.of(jutsuEntity));
        when(jutsuMapper.toDto(jutsuEntity)).thenReturn(jutsuResponseDTO);
        JutsuResponseDTO resultado = jutsuService.getJutsuById(1L);

        assertThat(resultado).isEqualTo(jutsuResponseDTO);
    }

    @Test
    @DisplayName("Deve lançar exceção ao não encontrar jutsu por ID")
    void deveLancarExcecaoQuandoNaoEncontrarJutsuPorId() {
        when(jutsuRepository.findById(1L)).thenReturn(Optional.empty());

        assertThatThrownBy(() -> jutsuService.getJutsuById(1L))
                .isInstanceOf(JutsuNaoEncontradoException.class);
    }

    @Test
    @DisplayName("Deve retornar jutsu quando encontrado por tipo")
    void deveRetornarJutsuPorTipo() {
        Jutsu jutsu = Jutsu.builder().id(1L).tipo("Rasengan").build();
        when(jutsuRepository.findByTipo("Rasengan")).thenReturn(Optional.of(jutsu));
        when(jutsuMapper.toDto(jutsu)).thenReturn(jutsuResponseDTO);
        JutsuResponseDTO resultado = jutsuService.getJutsuByTipo("Rasengan");

        assertThat(resultado).isEqualTo(jutsuResponseDTO);
    }

    @Test
    @DisplayName("Deve lançar exceção ao não encontrar jutsu por tipo")
    void deveLancarExcecaoQuandoNaoEncontrarJutsuPorTipo() {
        when(jutsuRepository.findByTipo("Chidori")).thenReturn(Optional.empty());

        assertThatThrownBy(() -> jutsuService.getJutsuByTipo("Chidori"))
                .isInstanceOf(JutsuNaoEncontradoException.class);
    }
}
