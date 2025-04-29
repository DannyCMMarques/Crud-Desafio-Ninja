package com.crud.demo.services.acaoService;

import static org.assertj.core.api.Assertions.*;
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
import com.crud.demo.models.DTO.JutsuDTO;
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
    }
    @Test
    @DisplayName("Deve obter jutsus disponíveis removendo já utilizados")
    void deveObterJutsusDisponiveis() {
        Map<String, Jutsu> jutsus = new HashMap<>();
        jutsus.put("Rasengan", Jutsu.builder().id(1L).tipo("Rasengan").build());
        jutsus.put("Chidori", Jutsu.builder().id(2L).tipo("Chidori").build());

        Map<String, JutsuDTO> jutsusDto = new HashMap<>();
        jutsusDto.put("Rasengan", JutsuDTO.builder().id(1L).tipo("Rasengan").build());
        jutsusDto.put("Chidori", JutsuDTO.builder().id(2L).tipo("Chidori").build());

        when(jutsuRepository.findMapByCategoria(CategoriaEspecialidadeEnum.NINJUTSU)).thenReturn(jutsus);
        when(jutsuMapper.toDtoMap(jutsus)).thenReturn(jutsusDto);

        estatistica.getHistoricoDeJutsus().put("Rasengan", JutsuDTO.builder().id(1L).tipo("Rasengan").build());

        Map<String, JutsuDTO> disponiveis = jutsuService.obterJutsusDisponiveis(estatistica);

        assertThat(disponiveis).containsKey("Chidori");
        assertThat(disponiveis).doesNotContainKey("Rasengan");
    }

    @Test
    @DisplayName("Deve adicionar jutsu ao histórico")
    void deveAdicionarHistorico() {
        JutsuDTO jutsu = JutsuDTO.builder().id(1L).tipo("Rasengan").build();

        jutsuService.adicionarHistorico(estatistica, jutsu);

        assertThat(estatistica.getHistoricoDeJutsus()).containsKey("Rasengan");
        assertThat(estatistica.getHistoricoDeJutsus().get("Rasengan")).isEqualTo(jutsu);
    }

    @Test
    @DisplayName("Deve retornar jutsu quando encontrado por ID")
    void deveRetornarJutsuPorId() {
        Jutsu jutsu = Jutsu.builder().id(1L).tipo("Rasengan").build();
        when(jutsuRepository.findById(1L)).thenReturn(Optional.of(jutsu));

        Jutsu resultado = jutsuService.getJutsuById(1L);

        // Assert
        assertThat(resultado).isEqualTo(jutsu);
    }

    @Test
    @DisplayName("Deve lançar exceção ao não encontrar jutsu por ID")
    void deveLancarExcecaoQuandoNaoEncontrarJutsuPorId() {
        // Arrange
        when(jutsuRepository.findById(1L)).thenReturn(Optional.empty());

        // Act & Assert
        assertThatThrownBy(() -> jutsuService.getJutsuById(1L))
            .isInstanceOf(JutsuNaoEncontradoException.class);
    }

    @Test
    @DisplayName("Deve retornar jutsu quando encontrado por tipo")
    void deveRetornarJutsuPorTipo() {
        Jutsu jutsu = Jutsu.builder().id(1L).tipo("Rasengan").build();
        when(jutsuRepository.findByTipo("Rasengan")).thenReturn(Optional.of(jutsu));

        Jutsu resultado = jutsuService.getJutsuByTipo("Rasengan");

        assertThat(resultado).isEqualTo(jutsu);
    }

    @Test
    @DisplayName("Deve lançar exceção ao não encontrar jutsu por tipo")
    void deveLancarExcecaoQuandoNaoEncontrarJutsuPorTipo() {
        when(jutsuRepository.findByTipo("Chidori")).thenReturn(Optional.empty());

        assertThatThrownBy(() -> jutsuService.getJutsuByTipo("Chidori"))
            .isInstanceOf(JutsuNaoEncontradoException.class);
    }
}
