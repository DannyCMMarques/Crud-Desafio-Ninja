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

import com.crud.demo.models.DTO.usuario.UsuarioRequestDTO;
import com.crud.demo.models.DTO.usuario.UsuarioResponseDTO;
import com.crud.demo.services.contratos.UsuarioService;
import com.crud.demo.utils.UriLocationUtils;

import io.swagger.v3.oas.annotations.Operation;
import io.swagger.v3.oas.annotations.Parameter;
import io.swagger.v3.oas.annotations.responses.ApiResponse;
import io.swagger.v3.oas.annotations.responses.ApiResponses;
import io.swagger.v3.oas.annotations.tags.Tag;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;
@RestController
@RequestMapping("/api/v1/usuarios")
@RequiredArgsConstructor
@Tag(name = "Usuarios", description = "Endpoints para operações com usuários")
public class UsuarioController {

        private final UsuarioService usuarioService;
        private static final Logger log = LoggerFactory.getLogger(UsuarioController.class);

        @PostMapping("/registro")
        @Operation(summary = "Cadastrar um novo usuário", description = "Cria um novo usuário no sistema")
        @ApiResponses({
                        @ApiResponse(responseCode = "201", description = "Usuário criado com sucesso"),
                        @ApiResponse(responseCode = "400", description = "Dados inválidos fornecidos")
        })
        public ResponseEntity<UsuarioResponseDTO> cadastrarUsuario(@Valid @RequestBody UsuarioRequestDTO usuarioDTO) {
                UsuarioResponseDTO saved = usuarioService.cadastrarUsuario(usuarioDTO);
                log.info("Usuário criado com sucesso, id={}", saved.getId());
                URI location = UriLocationUtils
                                .criarLocationUri("/api/v1/usuarios/{id}", saved.getId());
                return ResponseEntity.created(location).body(saved);
        }

        @GetMapping("/{id}")
        @Operation(summary = "Buscar usuário por ID", description = "Recupera os dados de um usuário baseado no ID")
        @ApiResponses({
                        @ApiResponse(responseCode = "200", description = "Usuário encontrado com sucesso"),
                        @ApiResponse(responseCode = "404", description = "Usuário não encontrado")
        })
        public ResponseEntity<UsuarioResponseDTO> buscarUsuarioPorId(@PathVariable Long id) {
                UsuarioResponseDTO usuarioDTO = usuarioService.buscarUsuarioPorId(id);
                log.debug("Dados do usuário id={}: {}", id, usuarioDTO);
                return ResponseEntity.ok(usuarioDTO);
        }

        @PutMapping("/{id}")
        @Operation(summary = "Atualizar um usuário existente", description = "Atualiza os dados de um usuário pelo ID")
        @ApiResponses({
                        @ApiResponse(responseCode = "200", description = "Usuário atualizado com sucesso"),
                        @ApiResponse(responseCode = "400", description = "Dados inválidos fornecidos"),
                        @ApiResponse(responseCode = "404", description = "Usuário não encontrado")
        })
        public ResponseEntity<UsuarioResponseDTO> atualizarUsuario(@Valid @PathVariable Long id,
                        @RequestBody UsuarioRequestDTO usuarioDTO) {
                UsuarioResponseDTO usuarioAtualizado = usuarioService.atualizarUsuario(id, usuarioDTO);
                log.info("Usuário id={} atualizado com sucesso", usuarioAtualizado.getId());
                return ResponseEntity.ok(usuarioAtualizado);
        }

        @DeleteMapping("/{id}")
        @Operation(summary = "Excluir um usuário", description = "Exclui um usuário do sistema com base no ID")
        @ApiResponses({
                        @ApiResponse(responseCode = "204", description = "Usuário excluído com sucesso"),
                        @ApiResponse(responseCode = "404", description = "Usuário não encontrado")
        })
        public ResponseEntity<Void> deleteUsuario(@PathVariable Long id) {
                usuarioService.deleteById(id);
                log.info("Usuário id={} excluído com sucesso", id);
                return ResponseEntity.noContent().build();
        }

        @GetMapping
        @Operation(summary = "Listar ou filtrar usuários", description = "Recupera uma lista de usuários com filtragem, paginação e ordenação")
        @ApiResponses({
                        @ApiResponse(responseCode = "200", description = "Lista de usuários retornada com sucesso"),
                        @ApiResponse(responseCode = "400", description = "Requisição inválida")
        })
        public ResponseEntity<Page<UsuarioResponseDTO>> exibirTodosUsuarios(
                        @Parameter(description = "Número da página") @RequestParam(defaultValue = "0") int page,
                        @Parameter(description = "Tamanho da página") @RequestParam(defaultValue = "10") int size,
                        @Parameter(description = "Campo para ordenar") @RequestParam(defaultValue = "nome") String sortBy,
                        @Parameter(description = "Direção da ordenação (asc ou desc)") @RequestParam(defaultValue = "asc") String direction) {

               
                Page<UsuarioResponseDTO> usuarios = usuarioService.exibirTodosUsuarios(sortBy, direction, page, size);
                log.info("Retornando {} usuários na página {}", usuarios.getNumberOfElements(), page);
                return ResponseEntity.ok(usuarios);
        }
}
