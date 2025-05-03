package com.crud.demo.models.DTO.jutsu;

import com.crud.demo.models.enuns.CategoriaEspecialidadeEnum;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.Builder;
import lombok.Value;

@Value
@Builder
public class JutsuRequestDTO {

    @Schema(description = "Nome/tipo do jutsu", example = "Rasengan")
    private String tipo;

    @Schema(description = "Dano máximo que o jutsu pode causar", example = "50")
    private Integer dano;

    @Schema(description = "Quantidade de chakra consumida ao usar o jutsu", example = "25")
    private Integer consumo_de_chakra;

    @Schema(description = "Categoria do jutsu (TAIJUTSU, NINJUTSU ou GENJUTSU)", example = "NINJUTSU")
    private CategoriaEspecialidadeEnum categoria;
}
