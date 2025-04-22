package com.crud.demo.controllers;

import java.util.HashMap;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import com.crud.demo.services.AcoesServiceImpl;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("api/v1/personagens/acoes")
@RequiredArgsConstructor
@Tag(name = "Ações dos Personagens", description = "Endpoints para realizar ações (atacar/defender) dos personagens")
public class AcoesControllador {
    private static final Logger log = LoggerFactory.getLogger(AcoesControllador.class);

    private final AcoesServiceImpl acoesService;

    @GetMapping("/{id}/atacar")
    @Operation(summary = "Atacar o personagem", description = "Este endpoint permite que o personagem ataque utilizando um jutsu baseado no tipo de ninja.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Ação de ataque realizada com sucesso"),
            @ApiResponse(responseCode = "404", description = "Personagem não encontrado"),
            @ApiResponse(responseCode = "400", description = "Requisição inválida")
    })
    public ResponseEntity<Map<String, String>> atacar(
            @PathVariable Long id) {

        String resultado = acoesService.atacar(id);
        log.debug("Resultado do ataque para id={}: {}", id, resultado);
        Map<String, String> response = new HashMap<>();
        response.put("message", resultado);
        
        return ResponseEntity.ok(response);
    }

    @GetMapping("/{id}/defender")
    @Operation(summary = "Defender o personagem", description = "Este endpoint permite que o personagem defenda utilizando o jutsu de desvio baseado no tipo de ninja.")
    @ApiResponses(value = {
            @ApiResponse(responseCode = "200", description = "Ação de defesa realizada com sucesso"),
            @ApiResponse(responseCode = "404", description = "Personagem não encontrado"),
            @ApiResponse(responseCode = "400", description = "Requisição inválida")
    })
    public ResponseEntity<Map<String, String>> defender(
            @PathVariable Long id) {

        String resultado = acoesService.defender(id);
        log.debug("Resultado da defesa para id={}: {}", id, resultado);
        Map<String, String> response = new HashMap<>();
        response.put("message", resultado);
        return ResponseEntity.ok(response);
    }
}
