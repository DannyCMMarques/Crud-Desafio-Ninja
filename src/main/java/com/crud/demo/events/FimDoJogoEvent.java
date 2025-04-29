package com.crud.demo.events;

import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class FimDoJogoEvent {

    private Long idGanhador;
    private Long idBatalha;
}
