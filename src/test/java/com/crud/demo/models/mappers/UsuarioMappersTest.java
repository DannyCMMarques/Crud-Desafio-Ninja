package com.crud.demo.models.mappers;

import static org.junit.jupiter.api.Assertions.*;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Nested;
import org.junit.jupiter.api.Test;

import com.crud.demo.models.Usuario;
import com.crud.demo.models.DTO.RegistroUsuarioDTO;

class UsuarioMapperTest {

    private UsuarioMapper mapper;

    @BeforeEach
    void setUp() {
        mapper = new UsuarioMapper();
    }

    @Nested
    @DisplayName("Mapeamento para DTO")
    class ToDtoTests {

        @Test
        @DisplayName("Deve mapear todos os campos exceto senha")
        void deveMapearTodosCamposExcetoSenha() {
            Usuario usuario = Usuario.builder()
                    .id(1L)
                    .nome("Ana")
                    .email("ana@example.com")
                    .senha("topSecret")
                    .build();

            RegistroUsuarioDTO dto = mapper.toDto(usuario);

            assertNotNull(dto, "DTO não deve ser nulo");
            assertEquals(1L, dto.getId(), "ID deve ser mapeado");
            assertEquals("Ana", dto.getNome(), "Nome deve ser mapeado");
            assertEquals("ana@example.com", dto.getEmail(), "Email deve ser mapeado");
            assertNull(dto.getSenha(), "Senha não deve ser mapeada para o DTO");
        }

        @Test
        @DisplayName("Deve suportar entidade com senha nula")
        void deveSuportarEntidadeComSenhaNula() {
            Usuario usuario = Usuario.builder()
                    .id(2L)
                    .nome("Beto")
                    .email("beto@example.com")
                    .senha(null)
                    .build();

            RegistroUsuarioDTO dto = mapper.toDto(usuario);

            assertNotNull(dto);
            assertEquals(2L, dto.getId());
            assertEquals("Beto", dto.getNome());
            assertEquals("beto@example.com", dto.getEmail());
            assertNull(dto.getSenha(), "Mesmo que a senha da entidade seja nula, o DTO deve retornar null");
        }
    }

    @Nested
    @DisplayName("Mapeamento para Entidade")
    class ToEntityTests {

        @Test
        @DisplayName("Deve mapear todos os campos incluindo senha")
        void deveMapearTodosCamposIncluindoSenha() {
            RegistroUsuarioDTO dto = new RegistroUsuarioDTO();
            dto.setId(10L);
            dto.setNome("Carlos");
            dto.setEmail("carlos@example.com");
            dto.setSenha("senha123");

            Usuario usuario = mapper.toEntity(dto);

            assertNotNull(usuario, "Entidade não deve ser nula");
            assertEquals(10L, usuario.getId(), "ID deve ser mapeado");
            assertEquals("Carlos", usuario.getNome(), "Nome deve ser mapeado");
            assertEquals("carlos@example.com", usuario.getEmail(), "Email deve ser mapeado");
            assertEquals("senha123", usuario.getSenha(), "Senha deve ser mapeada");
        }

        @Test
        @DisplayName("Deve suportar DTO com senha nula")
        void deveSuportarDtoComSenhaNula() {
            RegistroUsuarioDTO dto = new RegistroUsuarioDTO();
            dto.setId(11L);
            dto.setNome("Diana");
            dto.setEmail("diana@example.com");
            dto.setSenha(null);

            Usuario usuario = mapper.toEntity(dto);

            assertNotNull(usuario);
            assertEquals(11L, usuario.getId());
            assertEquals("Diana", usuario.getNome());
            assertEquals("diana@example.com", usuario.getEmail());
            assertNull(usuario.getSenha(), "Quando a senha no DTO for null, o entity deve manter senha null");
        }
    }
}
