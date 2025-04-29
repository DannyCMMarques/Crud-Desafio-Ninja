package com.crud.demo.models.mappers;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.time.LocalDateTime;
import java.util.Collections;
import java.util.HashMap;
import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import com.crud.demo.models.Jutsu;
import com.crud.demo.models.Personagem;
import com.crud.demo.models.DTO.JutsuDTO;
import com.crud.demo.models.DTO.PersonagemDTO;
import com.crud.demo.models.enuns.CategoriaEspecialidadeEnum;

class PersonagemMapperTest {

    private JutsuMapper jutsuMapper;
    private PersonagemMapper personagemMapper;

    @BeforeEach
    void setUp() {
        jutsuMapper = mock(JutsuMapper.class);
        personagemMapper = new PersonagemMapper(jutsuMapper);
    }

    @Test
    @DisplayName("toEntity - deve converter PersonagemDTO em Personagem corretamente")
    void testToEntity() {
        JutsuDTO jutsuDTO = JutsuDTO.builder().tipo("Katon").build();
        Jutsu jutsu = Jutsu.builder().tipo("Katon").build();

        Map<String, JutsuDTO> jutsusDtoMap = new HashMap<>();
        jutsusDtoMap.put("ofensivo", jutsuDTO);

        PersonagemDTO dto = PersonagemDTO.builder()
                .id(1L)
                .nome("Naruto")
                .idade(17L)
                .aldeia("Konoha")
                .especialidade(CategoriaEspecialidadeEnum.NINJUTSU)
                .jutsus(jutsusDtoMap)
                .build();

        when(jutsuMapper.toEntity(jutsuDTO)).thenReturn(jutsu);

        Personagem personagem = personagemMapper.toEntity(dto);

        assertEquals(1L, personagem.getId());
        assertEquals("Naruto", personagem.getNome());
        assertEquals(17L, personagem.getIdade());
        assertEquals("Konoha", personagem.getAldeia());
        assertEquals(CategoriaEspecialidadeEnum.NINJUTSU, personagem.getEspecialidade());
        assertNotNull(personagem.getJutsus());
        assertEquals(jutsu, personagem.getJutsus().get("ofensivo"));

        verify(jutsuMapper).toEntity(jutsuDTO);
    }

    @Test
    @DisplayName("toDto - deve converter Personagem em PersonagemDTO corretamente")
    void testToDto() {
        Jutsu jutsu = Jutsu.builder().tipo("Suiton").build();
        JutsuDTO jutsuDTO = JutsuDTO.builder().tipo("Suiton").build();

        Map<String, Jutsu> jutsuMap = new HashMap<>();
        jutsuMap.put("defensivo", jutsu);

        LocalDateTime dataCriacao = LocalDateTime.now();

        Personagem personagem = Personagem.builder()
                .id(2L)
                .nome("Sasuke")
                .idade(18L)
                .aldeia("Konoha")
                .chakra(100)
                .vida(5.00)
                .especialidade(CategoriaEspecialidadeEnum.GENJUTSU)
                .jutsus(jutsuMap)
                .dataCriacao(dataCriacao)
                .build();

        when(jutsuMapper.toDto(jutsu)).thenReturn(jutsuDTO);

        PersonagemDTO dto = personagemMapper.toDto(personagem);

        assertEquals(2L, dto.getId());
        assertEquals("Sasuke", dto.getNome());
        assertEquals(18L, dto.getIdade());
        assertEquals("Konoha", dto.getAldeia());
        assertEquals(100, dto.getChakra());
        assertEquals(5.00, dto.getVida());
        assertEquals(CategoriaEspecialidadeEnum.GENJUTSU, dto.getEspecialidade());
        assertEquals(jutsuDTO, dto.getJutsus().get("defensivo"));
        assertEquals(dataCriacao, dto.getDataCriacao());

        verify(jutsuMapper).toDto(jutsu);
    }

    @Test
    @DisplayName("preencherDados - deve copiar os dados de origem para destino")
    void testPreencherDados() {
        Personagem origem = Personagem.builder()
                .id(5L)
                .nome("Kakashi")
                .idade(30L)
                .aldeia("Konoha")
                .especialidade(CategoriaEspecialidadeEnum.TAIJUTSU)
                .dataCriacao(LocalDateTime.now())
                .jutsus(Collections.emptyMap())
                .build();

        Personagem destino = new Personagem();

        personagemMapper.preencherDados(destino, origem);

        assertEquals(origem.getId(), destino.getId());
        assertEquals(origem.getNome(), destino.getNome());
        assertEquals(origem.getIdade(), destino.getIdade());
        assertEquals(origem.getAldeia(), destino.getAldeia());
        assertEquals(origem.getEspecialidade(), destino.getEspecialidade());
        assertEquals(origem.getDataCriacao(), destino.getDataCriacao());
        assertEquals(origem.getJutsus(), destino.getJutsus());
    }
}
