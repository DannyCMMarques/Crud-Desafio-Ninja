package com.crud.demo.models.DTO;

import com.crud.demo.models.enuns.CategoriaEspecialidadeEnum;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class JutsuDTO {

    @Schema(description = "ID do jutsu", example = "1")
    private Long id;

    @Schema(description = "Nome/tipo do jutsu", example = "Rasengan")
    private String tipo;

    @Schema(description = "Dano máximo que o jutsu pode causar", example = "50")
    private Integer dano;

    @Schema(description = "Quantidade de chakra consumida ao usar o jutsu", example = "25")
    private Integer consumo_de_chakra;

    @Schema(description = "Categoria do jutsu (TAIJUTSU, NINJUTSU ou GENJUTSU)", example = "NINJUTSU")
    private CategoriaEspecialidadeEnum categoria;
}
