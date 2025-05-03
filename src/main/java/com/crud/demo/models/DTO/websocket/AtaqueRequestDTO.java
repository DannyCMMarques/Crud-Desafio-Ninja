package com.crud.demo.models.DTO.websocket;

import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import lombok.Data;

@Data
@JsonIgnoreProperties(ignoreUnknown = true)
public class AtaqueRequestDTO {

    private Long idAtacante;
    private Long idDefensor;
    private String jutsuEscolhido;
}
