package com.crud.demo.models.DTO;

import java.util.List;

import io.swagger.v3.oas.annotations.media.Schema;
import jakarta.validation.constraints.NotNull;
import lombok.Data;

@Data
@Schema(description = "Representa uma pessoa com dados pessoais e lista de endereços")

public class PersonagemDTO {

    @Schema(description = "ID gerado automaticamente", example = "1", accessMode = Schema.AccessMode.READ_ONLY)
    private long id;

    @NotNull(message = "Nome é obrigatório")
    @Schema(description = "Nome do pessoa", example = "Naruto")
    private String nome;

    @NotNull(message = "Idade é obrigatória")
    @Schema(description = "Idade da pessoa", example = "33")
    private long idade;
    @NotNull(message = "Aldeia é obrigatória")
    @Schema(description = "Aldeia da pessoa", example = "Konoha")
    private String aldeia;

    @NotNull(message = "Insira um jutsus")
    @Schema(description = "Lista de jutsus do personagem", example = "[\"Rasengan\", \"Kage Bunshin no Jutsu\"]")
    private List<String> jutsus;

    @NotNull(message = "Chakra é obrigatório")
    @Schema(description = "Quantidade de chakra do personagem", example = "1000")
    private long chakra;

}
