package com.crud.demo.models.DTO.personagem;

import java.time.LocalDateTime;
import java.util.Map;

import com.crud.demo.models.DTO.jutsu.JutsuResponseDTO;
import com.crud.demo.models.enuns.CategoriaEspecialidadeEnum;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
public class PersonagemRequestDTO {

    @NotNull(message = "Nome é obrigatório")
    @Schema(description = "Nome do personagem", example = "Naruto Uzumaki")
    private String nome;

    @NotNull(message = "Idade é obrigatória")
    @Schema(description = "Idade do personagem", example = "17")
    private Long idade;

    @NotNull(message = "Aldeia é obrigatória")
    @Schema(description = "Aldeia do personagem", example = "Konoha")
    private String aldeia;

    @NotNull(message = "Chakra é obrigatório")
    @Schema(description = "Quantidade de chakra do personagem", example = "100")
    private Integer chakra;

    @NotNull(message = "Vida é obrigatória")
    @Schema(description = "Quantidade de vida do personagem", example = "5")
    private Double vida;

    @NotNull(message = "Especialidade é obrigatória")
    @Schema(description = "Especialidade do personagem (TAIJUTSU, NINJUTSU ou GENJUTSU)", example = "NINJUTSU")
    private CategoriaEspecialidadeEnum especialidade;

    @Schema(description = "Map de jutsus onde a chave é o nome do jutsu e o valor é o DTO correspondente")
    private Map<String, JutsuResponseDTO> jutsus;

    @Schema(description = "Data de criação do registro", accessMode = Schema.AccessMode.READ_ONLY)
    private LocalDateTime dataCriacao;
}
