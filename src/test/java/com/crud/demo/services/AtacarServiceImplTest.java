package com.crud.demo.services;

import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.times;
import static org.mockito.Mockito.verify;
import org.mockito.junit.jupiter.MockitoExtension;

import com.crud.demo.events.AtaqueEvent;
import com.crud.demo.events.EstatisticaDoJogadorEvent;
import com.crud.demo.models.Jutsu;
import com.crud.demo.models.Personagem;
import com.crud.demo.models.DTO.jutsu.JutsuResponseDTO;

@ExtendWith(MockitoExtension.class)
class AtacarServiceImplTest {

    @Mock
    private ChakrasServiceImpl chakraService;

    @InjectMocks
    private AtacarServiceImpl atacarService;

    private JutsuResponseDTO jutsuResponseDTO;
    private Jutsu jutsu;
    @Test
    void deveChamarAtualizarEstatisticaQuandoAtacar() {
    jutsuResponseDTO = JutsuResponseDTO.builder()
                .id(3L)
                .tipo("tipo JutsuResponseDTO")
                .build();

        jutsu = Jutsu.builder()
                .id(4L)
                .tipo("tipo Jutsu")
                .build();

        Map<String, Jutsu> jutsusDisponiveis = new HashMap<>();
        jutsusDisponiveis.put("Jutsu1", jutsu);
        jutsusDisponiveis.put("Jutsu2", jutsu);

        Map<String, JutsuResponseDTO> historicoDeJutsus = new HashMap<>();
        historicoDeJutsus.put("Jutsu1", jutsuResponseDTO);
        historicoDeJutsus.put("Jutsu2", jutsuResponseDTO);

        Personagem personagem = new Personagem();

        EstatisticaDoJogadorEvent estatistica = EstatisticaDoJogadorEvent.builder()
                .personagem(personagem)
                .historicoDeJutsus(historicoDeJutsus)
                .valor_chakra(100)
                .vidas(3.00)
                .jutsusDisponiveis(jutsusDisponiveis)
                .build();

        Long idBatalha = 1L;

        AtaqueEvent ataqueEvent = new AtaqueEvent(
                idBatalha,
                estatistica,
                estatistica,
                jutsuResponseDTO);

        atacarService.atacar(ataqueEvent);

        verify(chakraService, times(1)).atualizarEstatistica(ataqueEvent);
    }
}
