package com.crud.demo.repositories;

import com.crud.demo.models.Jutsu;
import com.crud.demo.models.enuns.CategoriaEspecialidadeEnum;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;

import java.util.*;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.mockito.Mockito.*;

class JutsuRepositoryTest {

    private JutsuRepository jutsuRepository;

    @BeforeEach
    void setUp() {
        jutsuRepository = mock(JutsuRepository.class, CALLS_REAL_METHODS);
    }

    @Test
    @DisplayName("Deve retornar um Map de tipo para Jutsu ao buscar por categoria")
    void deveRetornarMapPorCategoria() {
        CategoriaEspecialidadeEnum categoria = CategoriaEspecialidadeEnum.NINJUTSU;

        Jutsu jutsu1 = new Jutsu();
        jutsu1.setTipo("Fogo");

        Jutsu jutsu2 = new Jutsu();
        jutsu2.setTipo("Agua");

        List<Jutsu> jutsus = Arrays.asList(jutsu1, jutsu2);

        when(jutsuRepository.findByCategoria(categoria)).thenReturn(jutsus);

        Map<String, Jutsu> resultado = jutsuRepository.findMapByCategoria(categoria);

        assertEquals(2, resultado.size());
        assertEquals(jutsu1, resultado.get("Fogo"));
        assertEquals(jutsu2, resultado.get("Agua"));

        verify(jutsuRepository).findByCategoria(categoria);
    }

    @Test
    @DisplayName("Deve retornar um Map vazio se não houver Jutsus na categoria")
    void deveRetornarMapVazioSeNaoHouverJutsus() {
        CategoriaEspecialidadeEnum categoria = CategoriaEspecialidadeEnum.GENJUTSU;

        when(jutsuRepository.findByCategoria(categoria)).thenReturn(Collections.emptyList());

        Map<String, Jutsu> resultado = jutsuRepository.findMapByCategoria(categoria);

        assertEquals(0, resultado.size());

        verify(jutsuRepository).findByCategoria(categoria);
    }
}
