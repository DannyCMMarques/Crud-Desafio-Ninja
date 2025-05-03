package com.crud.demo.controllers;

import java.net.URI;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.Page;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.DeleteMapping;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.PutMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.RestController;

import com.crud.demo.models.DTO.participanteBatalha.ParticipanteBatalhaRequestDTO;
import com.crud.demo.models.DTO.participanteBatalha.ParticipanteBatalhaResponseDTO;
import com.crud.demo.services.contratos.ParticipanteBatalhaService;
import com.crud.demo.utils.UriLocationUtils;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("api/v1/participante-batalha")
@RequiredArgsConstructor
@Tag(name = "Participante Batalha", description = "Endpoints para operações com Participantes de Batalha")
public class ParticipanteBatalhaController {

    private static final Logger log = LoggerFactory.getLogger(ParticipanteBatalhaController.class);

    private final ParticipanteBatalhaService participanteBatalhaService;

    @PostMapping
    @Operation(summary = "Cadastrar um Participante de Batalha")
    @ApiResponses({
            @ApiResponse(responseCode = "201", description = "Participante criado com sucesso"),
            @ApiResponse(responseCode = "400", description = "Dados inválidos")
    })
    public ResponseEntity<ParticipanteBatalhaResponseDTO> cadastrarParticipante(
            @Valid @RequestBody ParticipanteBatalhaRequestDTO dto) {
        ParticipanteBatalhaResponseDTO participanteCriado = participanteBatalhaService.criarParticipanteBatalha(dto);
        log.info("Requisição de POST para criar participante de batalha, dados={}", dto);
        log.info("Participante de batalha criado com sucesso, id={}", participanteCriado.getId());

        URI location = UriLocationUtils.criarLocationUri("api/v1/participante-batalha", participanteCriado.getId());
        return ResponseEntity.created(location).body(participanteCriado);
    }

    @DeleteMapping("{id}")
    @Operation(summary = "Excluir um Participante de Batalha por ID")
    @ApiResponses({
            @ApiResponse(responseCode = "204", description = "Participante excluído com sucesso"),
            @ApiResponse(responseCode = "404", description = "Participante não encontrado")
    })
    public ResponseEntity<Void> deletar(@PathVariable long id) {
        log.info("Requisição de DELETE para participante de batalha id={}", id);
        participanteBatalhaService.deletarParticipanteBatalha(id);
        return ResponseEntity.noContent().build();
    }

    @PutMapping("{id}")
    @Operation(summary = "Atualizar um Participante de Batalha existente")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Participante atualizado com sucesso"),
            @ApiResponse(responseCode = "400", description = "Dados inválidos"),
            @ApiResponse(responseCode = "404", description = "Participante não encontrado")
    })
    public ResponseEntity<ParticipanteBatalhaResponseDTO> atualizar(@PathVariable long id,
            @Valid @RequestBody ParticipanteBatalhaRequestDTO dto) {
        log.info("Requisição de UPDATE para participante id={}, dados={}", id, dto);
        ParticipanteBatalhaResponseDTO atualizado = participanteBatalhaService.atualizarParticipanteBatalha(id, dto);
        log.info("Participante de batalha atualizado com sucesso, id={}", atualizado.getId());
        return ResponseEntity.ok(atualizado);
    }

    @GetMapping("{id}")
    @Operation(summary = "Buscar Participante de Batalha por ID")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Participante encontrado com sucesso"),
            @ApiResponse(responseCode = "404", description = "Participante não encontrado")
    })
    public ResponseEntity<ParticipanteBatalhaResponseDTO> buscarPorId(@PathVariable long id) {
        log.info("Requisição de GET para participante de batalha id={}", id);
        ParticipanteBatalhaResponseDTO participante = participanteBatalhaService.getParticipanteBatalhaById(id);
        return ResponseEntity.ok(participante);
    }

    @GetMapping
    @Operation(summary = "Listar ou filtrar Participantes de Batalha", description = "Retorna uma lista paginada de participantes com filtros opcionais.")
    @ApiResponses({
            @ApiResponse(responseCode = "200", description = "Lista de participantes retornada com sucesso"),
            @ApiResponse(responseCode = "400", description = "Requisição inválida")
    })
    public ResponseEntity<Page<ParticipanteBatalhaResponseDTO>> listarParticipantes(
            @Parameter(description = "Número da página") @RequestParam(defaultValue = "0") int page,
            @Parameter(description = "Tamanho da página") @RequestParam(defaultValue = "10") int size,
            @Parameter(description = "Campo para ordenar") @RequestParam(defaultValue = "nomeUsuario") String sortBy,
            @Parameter(description = "Direção da ordenação (asc ou desc)") @RequestParam(defaultValue = "asc") String direction) {

        Page<ParticipanteBatalhaResponseDTO> participantes = participanteBatalhaService.listarTodosParticipantes(sortBy, direction, page, size);
        log.debug("Total de participantes encontrados: {}", participantes.getTotalElements());

        if (participantes.isEmpty()) {
            log.info("Nenhum participante encontrado para os filtros informados.");
            return ResponseEntity.noContent().build();
        }

        return ResponseEntity.ok(participantes);
    }
}
