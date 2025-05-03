package com.crud.demo.events;

import java.util.HashMap;
import java.util.Map;

import com.crud.demo.models.Jutsu;
import com.crud.demo.models.Personagem;
import com.crud.demo.models.DTO.jutsu.JutsuResponseDTO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class EstatisticaDoJogadorEvent {

    private Personagem personagem;
    private Map<String, JutsuResponseDTO> historicoDeJutsus = new HashMap<>();
    private Integer valor_chakra;
    private Double vidas;
    private Map<String, Jutsu> jutsusDisponiveis;
}
