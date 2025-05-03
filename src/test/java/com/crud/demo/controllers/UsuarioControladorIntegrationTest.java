package com.crud.demo.controllers;

import static org.hamcrest.Matchers.endsWith;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.Mockito.*;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.*;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.*;

import java.io.IOException;
import java.util.List;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.autoconfigure.security.servlet.SecurityAutoConfiguration;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.autoconfigure.web.servlet.WebMvcTest;
import org.springframework.boot.test.mock.mockito.MockBean;
import org.springframework.context.annotation.Import;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import com.crud.demo.config.ApplicationConfiguration;
import com.crud.demo.models.Usuario;
import com.crud.demo.models.DTO.usuario.UsuarioRequestDTO;
import com.crud.demo.models.DTO.usuario.UsuarioResponseDTO;
import com.crud.demo.models.mappers.UsuarioMapper;
import com.crud.demo.security.JwtAuthenticationFilter;
import com.crud.demo.security.SecurityConfig;
import com.crud.demo.security.Service.JWTService;
import com.crud.demo.services.UsuarioServiceImpl;
import com.crud.demo.utils.UriLocationUtils;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebMvcTest(controllers = UsuarioController.class, excludeAutoConfiguration = SecurityAutoConfiguration.class)
@Import(UriLocationUtils.class)
@AutoConfigureMockMvc(addFilters = false)
@ActiveProfiles("test")
public class UsuarioControladorIntegrationTest {

        @Autowired
        private MockMvc mockMvc;

        @Autowired
        private ObjectMapper objectMapper;

        @MockBean
        private JwtAuthenticationFilter jwtAuthenticationFilter;

        @MockBean
        private UsuarioServiceImpl usuarioService;

        @MockBean
        private UsuarioMapper usuarioMapper;

        @MockBean
        private SecurityConfig securityConfig;

        @MockBean
        private JWTService jwtService;

        @MockBean
        private ApplicationConfiguration applicationConfig;
        private Usuario usuario;
        private UsuarioRequestDTO usuarioRequestDTO;
        private UsuarioResponseDTO usuarioResponseDTO;

        @BeforeEach
        void setUp() throws ServletException, IOException {
                doAnswer(inv -> {
                        HttpServletRequest req = inv.getArgument(0);
                        HttpServletResponse res = inv.getArgument(1);
                        FilterChain chain = inv.getArgument(2);
                        chain.doFilter(req, res);
                        return null;
                }).when(jwtAuthenticationFilter)
                                .doFilterInternal(any(), any(), any());
                usuarioRequestDTO = new UsuarioRequestDTO(
                                "João da Silva", "joao@example.com", "senha123");
                usuario = new Usuario();
                usuario.setId(1L);
                usuario.setNome("João da Silva");
                usuario.setEmail("joao@example.com");

                usuarioResponseDTO = UsuarioResponseDTO.builder()
                                .id(1L)
                                .nome("João da Silva")
                                .email("joao@example.com")
                                .build();
                Pageable pageable = Pageable.ofSize(10);
                when(usuarioMapper.toEntity(usuarioRequestDTO))
                                .thenReturn(usuario);

                when(usuarioMapper.toDto(usuario))
                                .thenReturn(usuarioResponseDTO);

                when(usuarioService.atualizarUsuario(1L, usuarioRequestDTO))
                                .thenReturn(usuarioResponseDTO);
                when(usuarioService.cadastrarUsuario(usuarioRequestDTO))
                                .thenReturn(usuarioResponseDTO);

                when(usuarioService.buscarUsuarioPorId(1L))
                                .thenReturn(usuarioResponseDTO);

                when(usuarioService.exibirTodosUsuarios("nome", "asc", 0, 10))
                                .thenReturn(new PageImpl<>(List.of(usuarioResponseDTO), pageable, 1));

                when(usuarioService.cadastrarUsuario(usuarioRequestDTO))
                                .thenReturn(usuarioResponseDTO);

        }

        @Test
        @DisplayName("Deve cadastrar usuário com sucesso e retornar header Location")
        void deveCadastrarUsuarioComSucesso() throws Exception {
                mockMvc.perform(post("/api/v1/usuarios/registro")
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(usuarioRequestDTO)))
                                .andExpect(status().isCreated())
                                .andExpect(header().string(HttpHeaders.LOCATION,
                                                endsWith("/api/v1/usuarios/1")));
        }

        @Test
        @DisplayName("Deve buscar usuário por ID com sucesso e retornar JSON")
        @WithMockUser(roles = "USER")
        void deveBuscarUsuarioPorIdComSucesso() throws Exception {
                when(usuarioService.buscarUsuarioPorId(1L))
                                .thenReturn(usuarioResponseDTO);

                mockMvc.perform(get("/api/v1/usuarios/{id}", 1L)
                                .contentType(MediaType.APPLICATION_JSON))
                                .andExpect(status().isOk())
                                .andExpect(jsonPath("$.nome").value("João da Silva"))
                                .andExpect(jsonPath("$.email").value("joao@example.com"));

                verify(usuarioService, times(1)).buscarUsuarioPorId(1L);
        }

        @Test
        @DisplayName("Deve atualizar usuário com sucesso e retornar JSON atualizado")
        @WithMockUser(roles = "USER")
        void deveAtualizarUsuarioComSucesso() throws Exception {

                mockMvc.perform(put("/api/v1/usuarios/{id}", 1L)
                                .contentType(MediaType.APPLICATION_JSON)
                                .content(objectMapper.writeValueAsString(usuarioRequestDTO)))
                                .andExpect(status().isOk())
                                .andExpect(jsonPath("$.id").value(1L));

                verify(usuarioService, times(1))
                                .atualizarUsuario(eq(1L), any(UsuarioRequestDTO.class));
        }

        @Test
        @DisplayName("Deve deletar usuário com sucesso e retornar 204")
        @WithMockUser(roles = "USER")
        void deveDeletarUsuarioComSucesso() throws Exception {
                doNothing().when(usuarioService).deleteById(1L);

                mockMvc.perform(delete("/api/v1/usuarios/{id}", 1L))
                                .andExpect(status().isNoContent());

                verify(usuarioService, times(1)).deleteById(1L);
        }

        @Test
        @DisplayName("Deve exibir todos os usuários com sucesso e retornar lista com 1 elemento")
        @WithMockUser(roles = "USER")
        void deveExibirTodosUsuariosComSucesso() throws Exception {

                mockMvc.perform(get("/api/v1/usuarios")
                                .param("page", "0")
                                .param("size", "10")
                                .param("sortBy", "nome")
                                .param("direction", "asc"))
                                .andExpect(status().isOk())
                                .andExpect(jsonPath("$.content.length()").value(1));
        }

}
