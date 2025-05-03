package com.crud.demo.events;

import com.crud.demo.models.DTO.jutsu.JutsuResponseDTO;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;

@Data
@AllArgsConstructor
@Builder
public class AtaqueEvent {

    private Long idBatalha;

    private EstatisticaDoJogadorEvent atacante;

    private EstatisticaDoJogadorEvent defensor;

    private JutsuResponseDTO jutsuAtaque;

}
