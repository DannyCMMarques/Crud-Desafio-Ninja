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

import com.crud.demo.models.DTO.personagem.PersonagemRequestDTO;
import com.crud.demo.models.DTO.personagem.PersonagemResponseDTO;
import com.crud.demo.models.enuns.CategoriaEspecialidadeEnum;
import com.crud.demo.services.contratos.PersonagemService;
import com.crud.demo.utils.UriLocationUtils;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("api/v1/personagens")
@RequiredArgsConstructor
@Tag(name = "Personagens", description = "Endpoints para operações com personagens inspirado no universo Naruto")
public class PersonagemController {

        private static final Logger log = LoggerFactory.getLogger(PersonagemController.class);

        private final PersonagemService personagemService;

        @PostMapping
        @Operation(summary = "Cadastrar um novo personagem")
        @ApiResponses({
                        @ApiResponse(responseCode = "201", description = "Personagem criado com sucesso"),
                        @ApiResponse(responseCode = "400", description = "Dados inválidos")
        })
        public ResponseEntity<PersonagemResponseDTO> cadastrar(@Valid @RequestBody PersonagemRequestDTO dto) {
                PersonagemResponseDTO personagemCriado = personagemService.criarPersonagem(dto);
                log.info("Personagem criado com sucesso, id={}", personagemCriado.getId());

                URI location = UriLocationUtils
                                .criarLocationUri("/api/v1/personagens/{id}", personagemCriado.getId());
                return ResponseEntity.created(location).body(personagemCriado);
        }

        @DeleteMapping("{id}")
        @Operation(summary = "Excluir um personagem por ID")
        @ApiResponses({
                        @ApiResponse(responseCode = "204", description = "Personagem excluído com sucesso"),
                        @ApiResponse(responseCode = "404", description = "Personagem não encontrado")
        })
        public ResponseEntity<Void> deletar(@PathVariable long id) {
                log.info("Requisição de DELETE para personagem id={}", id);
                personagemService.deletarPersonagem(id);
                return ResponseEntity.noContent().build();
        }

        @PutMapping("{id}")
        @Operation(summary = "Atualizar um personagem existente")
        @ApiResponses({
                        @ApiResponse(responseCode = "200", description = "Personagem atualizado com sucesso"),
                        @ApiResponse(responseCode = "400", description = "Dados inválidos"),
                        @ApiResponse(responseCode = "404", description = "Personagem não encontrado")
        })
        public ResponseEntity<PersonagemResponseDTO> atualizar(@PathVariable long id,
                        @Valid @RequestBody PersonagemRequestDTO dto) {
                log.info("Requisição de UPDATE para personagem id={}, dados={}", id, dto);
                PersonagemResponseDTO atualizado = personagemService.atualizarPersonagem(id, dto);
                return ResponseEntity.ok(atualizado);
        }

        @GetMapping("{id}")
        @Operation(summary = "Buscar personagem por ID")
        @ApiResponses({
                        @ApiResponse(responseCode = "200", description = "Personagem encontrado com sucesso"),
                        @ApiResponse(responseCode = "404", description = "Personagem não encontrado")
        })
        public ResponseEntity<PersonagemResponseDTO> buscarPorId(@PathVariable long id) {
                log.info("Requisição de GET por id={}", id);
                PersonagemResponseDTO personagem = personagemService.buscarPersonagemPorId(id);
                return ResponseEntity.ok(personagem);
        }

        @GetMapping
        @Operation(summary = "Listar ou filtrar personagens", description = "Retorna uma lista paginada de personagens com filtros opcionais.")
        @ApiResponses({
                        @ApiResponse(responseCode = "200", description = "Lista de personagens retornada com sucesso"),
                        @ApiResponse(responseCode = "400", description = "Requisição inválida")
        })
        public ResponseEntity<Page<PersonagemResponseDTO>> filtrarPersonagens(
                        @Parameter(description = "Filtrar por nome") @RequestParam(required = false) String nome,
                        @Parameter(description = "Filtrar por idade exata") @RequestParam(required = false) Long idade,
                        @Parameter(description = "Filtrar por idade mínima") @RequestParam(required = false) Long idadeMin,
                        @Parameter(description = "Filtrar por idade máxima") @RequestParam(required = false) Long idadeMax,
                        @Parameter(description = "Filtrar por aldeia") @RequestParam(required = false) String aldeia,
                        @Parameter(description = "Filtrar por chakra") @RequestParam(required = false) CategoriaEspecialidadeEnum especialidade,
                        @Parameter(description = "Número da página") @RequestParam(defaultValue = "0") int page,
                        @Parameter(description = "Tamanho da página") @RequestParam(defaultValue = "10") int size,
                        @Parameter(description = "Campo para ordenar") @RequestParam(defaultValue = "nome") String sortBy,
                        @Parameter(description = "Direção da ordenação (asc ou desc)") @RequestParam(defaultValue = "asc") String direction) {

                Page<PersonagemResponseDTO> personagens = personagemService.filtrarPersonagens(nome, idade, idadeMin,
                                idadeMax,
                                aldeia, page, size, direction, sortBy, especialidade);
                log.debug("Total de personagens encontrados: {}", personagens.getTotalElements());
                if (personagens.isEmpty()) {
                        log.info("Nenhum personagem encontrado para os filtros informados.");
                        return ResponseEntity.noContent().build();
                }

                return ResponseEntity.ok(personagens);

        }
}
