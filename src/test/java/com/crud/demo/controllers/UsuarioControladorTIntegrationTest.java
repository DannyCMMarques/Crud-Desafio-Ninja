package com.crud.demo.controllers;

import static org.hamcrest.Matchers.endsWith;
import static org.mockito.ArgumentMatchers.*;
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
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.Pageable;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.security.test.context.support.WithMockUser;
import org.springframework.test.context.ActiveProfiles;
import org.springframework.test.web.servlet.MockMvc;

import com.crud.demo.config.ApplicationConfiguration;
import com.crud.demo.models.Usuario;
import com.crud.demo.models.DTO.RegistroUsuarioDTO;
import com.crud.demo.models.mappers.UsuarioMapper;
import com.crud.demo.security.JwtAuthenticationFilter;
import com.crud.demo.security.SecurityConfig;
import com.crud.demo.security.Service.JWTService;
import com.crud.demo.services.UsuarioService;
import com.crud.demo.utils.UriLocationUtils;
import com.fasterxml.jackson.databind.ObjectMapper;

import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;

@WebMvcTest(controllers = UsuarioControlador.class, excludeAutoConfiguration = SecurityAutoConfiguration.class)
@Import(UriLocationUtils.class)
@AutoConfigureMockMvc(addFilters = false)
@ActiveProfiles("test")
public class UsuarioControladorTIntegrationTest {

    @Autowired
    private MockMvc mockMvc;

    @Autowired
    private ObjectMapper objectMapper;

    @MockBean
    private JwtAuthenticationFilter jwtAuthenticationFilter;

    @MockBean
    private UsuarioService usuarioService;

    @MockBean
    private UsuarioMapper usuarioMapper;

    @MockBean
    private SecurityConfig securityConfig;

    @MockBean
    private JWTService jwtService;

    @MockBean
    private ApplicationConfiguration applicationConfig;

    private RegistroUsuarioDTO usuarioDTO;

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

        usuarioDTO = new RegistroUsuarioDTO();
        usuarioDTO.setId(1L);
        usuarioDTO.setNome("João da Silva");
        usuarioDTO.setEmail("joao@example.com");
        usuarioDTO.setSenha("senha123");

        when(usuarioMapper.toEntity(any(RegistroUsuarioDTO.class)))
                .thenReturn(new Usuario());
        when(usuarioMapper.toDto(any(Usuario.class)))
                .thenReturn(usuarioDTO);
    }

    @Test
    @DisplayName("Deve cadastrar usuário com sucesso e retornar header Location")
    void deveCadastrarUsuarioComSucesso() throws Exception {
        when(usuarioService.cadastrarUsuario(any(RegistroUsuarioDTO.class)))
                .thenReturn(usuarioDTO);

        mockMvc.perform(post("/api/v1/usuarios/registro")
                .contentType(MediaType.APPLICATION_JSON)
                .content(objectMapper.writeValueAsString(usuarioDTO)))
                .andExpect(status().isCreated())
                .andExpect(header().string(
                        HttpHeaders.LOCATION,
                        endsWith("/api/v1/usuarios/1")));
    }

    @Test
    @DisplayName("Deve buscar usuário por ID com sucesso e retornar JSON")
    @WithMockUser(roles = "USER")
    void deveBuscarUsuarioPorIdComSucesso() throws Exception {
        when(usuarioService.buscarUsuarioPorId(1L))
                .thenReturn(usuarioDTO);

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
        String payload = """
                {
                  "nome": "João Atualizado",
                  "email": "joao_atualizado@example.com",
                  "senha": "novaSenha"
                }
                """;

        RegistroUsuarioDTO updatedDto = new RegistroUsuarioDTO();
        updatedDto.setId(1L);
        updatedDto.setNome("João Atualizado");
        updatedDto.setEmail("joao_atualizado@example.com");
        updatedDto.setSenha("novaSenha");

        when(usuarioService.atualizarUsuario(any(Long.class), any(RegistroUsuarioDTO.class)))
                .thenReturn(updatedDto);

        mockMvc.perform(put("/api/v1/usuarios/{id}", 1L)
                .contentType(MediaType.APPLICATION_JSON)
                .content(payload))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.nome").value("João Atualizado"))
                .andExpect(jsonPath("$.email").value("joao_atualizado@example.com"));

        verify(usuarioService, times(1))
                .atualizarUsuario(eq(1L), any(RegistroUsuarioDTO.class));
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
    @DisplayName("Deve exibir todos os usuários com sucesso e retornar lista vazia")
    @WithMockUser(roles = "USER")
    void deveExibirTodosUsuariosComSucesso() throws Exception {
        Page<RegistroUsuarioDTO> emptyPage = new PageImpl<>(List.of());
        when(usuarioService.exibirTodosUsuarios(any(Pageable.class)))
                .thenReturn(emptyPage);

        mockMvc.perform(get("/api/v1/usuarios")
                .param("page", "0")
                .param("size", "10"))
                .andExpect(status().isOk())
                .andExpect(jsonPath("$.content").isEmpty());

        verify(usuarioService, times(1))
                .exibirTodosUsuarios(any(Pageable.class));
    }
}
