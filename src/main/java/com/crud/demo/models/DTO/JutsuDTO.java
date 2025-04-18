package com.crud.demo.models.DTO;

import io.swagger.v3.oas.annotations.media.Schema;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor
@AllArgsConstructor
public class JutsuDTO {

    @Schema(description = "ID do jutsu", example = "1")
    private Long id;

    @Schema(description = "Tipo do jutsu", example = "Ninjutsu")
    private String tipo;
}
