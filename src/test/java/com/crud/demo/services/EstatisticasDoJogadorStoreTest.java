package com.crud.demo.services;

import static org.assertj.core.api.Assertions.assertThat;
import static org.junit.jupiter.api.Assertions.assertThrows;

import java.util.List;
import java.util.Map;

import com.crud.demo.events.EstatisticaDoJogadorEvent;
import com.crud.demo.models.Personagem;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

class EstatisticasDoJogadorStoreTest {

    private EstatisticasDoJogadorStore store;

    @BeforeEach
    void setUp() {
        store = new EstatisticasDoJogadorStore();
    }

    @Test
    @DisplayName("iniciarBatalha – deve criar mapa interno com estatísticas")
    void deveIniciarBatalhaComEstatisticas() {
        EstatisticaDoJogadorEvent estatistica = criarEstatistica(1L);

        Map<Long, Map<Long, EstatisticaDoJogadorEvent>> result = store.iniciarBatalha(100L, List.of(estatistica));

        assertThat(result).isNotEmpty();
        assertThat(result.get(100L)).isNotEmpty();
        assertThat(result.get(100L).get(1L)).isEqualTo(estatistica);
    }

    @Test
    @DisplayName("getEstatistica – deve retornar estatística específica")
    void deveRetornarEstatisticaEspecifica() {
        EstatisticaDoJogadorEvent estatistica = criarEstatistica(2L);
        store.iniciarBatalha(200L, List.of(estatistica));

        EstatisticaDoJogadorEvent resultado = store.getEstatistica(200L, 2L);

        assertThat(resultado).isEqualTo(estatistica);
    }

    @Test
    @DisplayName("getEstatisticas – deve retornar lista completa")
    void deveRetornarListaDeEstatisticas() {
        EstatisticaDoJogadorEvent estatistica1 = criarEstatistica(3L);
        EstatisticaDoJogadorEvent estatistica2 = criarEstatistica(4L);
        store.iniciarBatalha(300L, List.of(estatistica1, estatistica2));

        List<EstatisticaDoJogadorEvent> estatisticas = store.getEstatisticas(300L);

        assertThat(estatisticas)
                .containsExactlyInAnyOrder(estatistica1, estatistica2);
    }

    @Test
    @DisplayName("updateEstatistica – deve substituir valor antigo")
    void deveAtualizarEstatistica() {
        EstatisticaDoJogadorEvent estatistica = criarEstatistica(5L);
        store.iniciarBatalha(400L, List.of(estatistica));

        EstatisticaDoJogadorEvent novaEstatistica = criarEstatistica(5L);
        novaEstatistica.setValor_chakra(50);

        store.updateEstatistica(400L, novaEstatistica);

        EstatisticaDoJogadorEvent atualizado = store.getEstatistica(400L, 5L);
        assertThat(atualizado.getValor_chakra()).isEqualTo(50);
    }

    @Test
    @DisplayName("updateEstatisticas – deve atualizar lista inteira")
    void deveAtualizarListaDeEstatisticas() {
        EstatisticaDoJogadorEvent estatistica1 = criarEstatistica(6L);
        EstatisticaDoJogadorEvent estatistica2 = criarEstatistica(7L);
        store.iniciarBatalha(500L, List.of(estatistica1, estatistica2));

        EstatisticaDoJogadorEvent nova1 = criarEstatistica(6L);
        nova1.setValor_chakra(80);

        EstatisticaDoJogadorEvent nova2 = criarEstatistica(7L);
        nova2.setValor_chakra(90);

        store.updateEstatisticas(500L, List.of(nova1, nova2));

        assertThat(store.getEstatistica(500L, 6L).getValor_chakra()).isEqualTo(80);
        assertThat(store.getEstatistica(500L, 7L).getValor_chakra()).isEqualTo(90);
    }

    @Test
    @DisplayName("getEstatistica – deve lançar exceção se batalha não existe")
    void deveLancarExcecaoQuandoBatalhaNaoEncontrada() {
        IllegalArgumentException ex = assertThrows(
                IllegalArgumentException.class,
                () -> store.getEstatistica(999L, 1L));

        assertThat(ex.getMessage()).contains("Estatísticas não iniciadas para battleId=999");
    }

    @Test
    @DisplayName("updateEstatistica – deve lançar exceção se battleId não existe")
    void deveLancarExcecaoAoAtualizarBatalhaInexistente() {
        EstatisticaDoJogadorEvent estatistica = criarEstatistica(10L);

        IllegalArgumentException ex = assertThrows(
                IllegalArgumentException.class,
                () -> store.updateEstatistica(888L, estatistica));

        assertThat(ex.getMessage()).contains("BattleId não encontrado: 888");
    }

    private EstatisticaDoJogadorEvent criarEstatistica(Long personagemId) {
        Personagem personagem = new Personagem();
        personagem.setId(personagemId);

        EstatisticaDoJogadorEvent e = new EstatisticaDoJogadorEvent();
        e.setPersonagem(personagem);
        e.setValor_chakra(100);
        e.setVidas(5.0);
        return e;
    }
}
