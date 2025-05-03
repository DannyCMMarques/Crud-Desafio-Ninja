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

import com.crud.demo.models.DTO.batalha.BatalhaRequestDTO;
import com.crud.demo.models.DTO.batalha.BatalhaResponseDTO;
import com.crud.demo.services.contratos.BatalhaService;
import com.crud.demo.utils.UriLocationUtils;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("api/v1/batalha")
@RequiredArgsConstructor
@Tag(name = "Batalha", description = "Endpoints para operações com Batalha")
public class BatalhaController {
        private static final Logger log = LoggerFactory.getLogger(BatalhaController.class);

        private final BatalhaService batalhaService;

        @PostMapping
        @Operation(summary = "Cadastrar uma Batalha")
        @ApiResponses({
                        @ApiResponse(responseCode = "201", description = "Batalha criado com sucesso"),
                        @ApiResponse(responseCode = "400", description = "Dados inválidos")
        })

        public ResponseEntity<BatalhaResponseDTO> cadastrarBatalha(@Valid @RequestBody BatalhaRequestDTO request) {
                BatalhaResponseDTO batalhaCriada = batalhaService.criarBatalha(request);
                log.info("Batalha criada com sucesso, id={}", batalhaCriada.getId());

                URI location = UriLocationUtils
                                .criarLocationUri("api/v1/batalha", batalhaCriada.getId());
                return ResponseEntity.created(location).body(batalhaCriada);
        }

        @DeleteMapping("{id}")
        @Operation(summary = "Excluir uma batalha por ID")
        @ApiResponses({
                        @ApiResponse(responseCode = "204", description = "Batalha excluído com sucesso"),
                        @ApiResponse(responseCode = "404", description = "Batalha não encontrado")
        })
        public ResponseEntity<Void> deletar(@PathVariable long id) {
                log.info("Requisição de DELETE para batalha id={}", id);
                batalhaService.deletarBatalha(id);
                return ResponseEntity.noContent().build();
        }

        @PutMapping("{id}")
        @Operation(summary = "Atualizar uma batalha existente")
        @ApiResponses({
                        @ApiResponse(responseCode = "200", description = "Batalha atualizado com sucesso"),
                        @ApiResponse(responseCode = "400", description = "Dados inválidos"),
                        @ApiResponse(responseCode = "404", description = "Batalha não encontrado")
        })
        public ResponseEntity<BatalhaResponseDTO> atualizar(@PathVariable long id, @Valid @RequestBody BatalhaResponseDTO dto) {
                log.info("Requisição de UPDATE para batalha id={}, dados={}", id, dto);
                BatalhaResponseDTO atualizado = batalhaService.atualizarBatalha(id, dto);
                return ResponseEntity.ok(atualizado);
        }

        @GetMapping("{id}")
        @Operation(summary = "Buscar batalha por ID")
        @ApiResponses({
                        @ApiResponse(responseCode = "200", description = "Batalha encontrado com sucesso"),
                        @ApiResponse(responseCode = "404", description = "Batalha não encontrado")
        })
        public ResponseEntity<BatalhaResponseDTO> buscarPorId(@PathVariable long id) {
                log.info("Requisição de GET por id={}", id);
                BatalhaResponseDTO batalha = batalhaService.getBatalhaById(id);
                return ResponseEntity.ok(batalha);
        }

        @GetMapping
        @Operation(summary = "Listar ou filtrar batalha", description = "Retorna uma lista paginada de batalhas com filtros opcionais.")
        @ApiResponses({
                        @ApiResponse(responseCode = "200", description = "Lista de batalhas retornada com sucesso"),
                        @ApiResponse(responseCode = "400", description = "Requisição inválida")
        })
        public ResponseEntity<Page<BatalhaResponseDTO>> exibirBatalhas(
                        @Parameter(description = "Número da página") @RequestParam(defaultValue = "0") int page,
                        @Parameter(description = "Tamanho da página") @RequestParam(defaultValue = "10") int size,
                        @Parameter(description = "Campo para ordenar") @RequestParam(defaultValue = "nome") String sortBy,
                        @Parameter(description = "Direção da ordenação (asc ou desc)") @RequestParam(defaultValue = "asc") String direction) {

                Page<BatalhaResponseDTO> batalhas = batalhaService.exibirTodasAsBatalhas(page, size, sortBy, direction);
                log.debug("Total de batalhas encontrados: {}", batalhas.getTotalElements());
                return ResponseEntity.ok(batalhas);

        }
}
