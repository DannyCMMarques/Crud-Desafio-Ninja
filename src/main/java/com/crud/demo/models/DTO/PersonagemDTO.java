package com.crud.demo.models.DTO;

import java.util.List;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Builder
@NoArgsConstructor
@AllArgsConstructor
@Schema(description = "Representa um personagem do universo Naruto")
public class PersonagemDTO {

    @Schema(description = "ID gerado automaticamente", example = "1", accessMode = Schema.AccessMode.READ_ONLY)
    private Long id;

    @NotNull(message = "Nome é obrigatório")
    @Schema(description = "Nome do personagem", example = "Naruto")
    private String nome;

    @NotNull(message = "Idade é obrigatória")
    @Schema(description = "Idade do personagem", example = "33")
    private Long idade;

    @NotNull(message = "Aldeia é obrigatória")
    @Schema(description = "Aldeia do personagem", example = "Konoha")
    private String aldeia;

    @NotNull(message = "Insira pelo menos um jutsu")
    @Schema(description = "Lista de jutsus do personagem")
    private List<JutsuDTO> jutsus;

    @NotNull(message = "Chakra é obrigatório")
    @Schema(description = "Quantidade de chakra do personagem", example = "1000")
    private Long chakra;
}
