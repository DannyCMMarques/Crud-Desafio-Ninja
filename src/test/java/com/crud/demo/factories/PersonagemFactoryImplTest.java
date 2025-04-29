package com.crud.demo.factories;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;


import com.crud.demo.models.Personagem;
import com.crud.demo.models.DTO.PersonagemDTO;
import com.crud.demo.models.enuns.CategoriaEspecialidadeEnum;
import com.crud.demo.models.mappers.PersonagemMapper;
import com.crud.demo.models.tiposPersonagens.NinjaDeGenjutsu;
import com.crud.demo.models.tiposPersonagens.NinjaDeNinjutsu;
import com.crud.demo.models.tiposPersonagens.NinjaDeTaijutsu;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;

class PersonagemFactoryImplTest {

    private PersonagemMapper personagemMapper;
    private PersonagemFactoryImpl personagemFactory;

    @BeforeEach
    void setUp() {
        personagemMapper = mock(PersonagemMapper.class);
        personagemFactory = new PersonagemFactoryImpl(personagemMapper);
    }

    @Test
    void deveConstruirPersonagemTaijutsu() {
        PersonagemDTO dto = new PersonagemDTO();
        Personagem personagemEntity = new Personagem();
        personagemEntity.setEspecialidade(CategoriaEspecialidadeEnum.TAIJUTSU);

        when(personagemMapper.toEntity(dto)).thenReturn(personagemEntity);

        Personagem resultado = personagemFactory.construirTipoPersonagem(dto);

        assertTrue(resultado instanceof NinjaDeTaijutsu);
        verify(personagemMapper).preencherDados(any(NinjaDeTaijutsu.class), eq(personagemEntity));
    }

    @Test
    void deveConstruirPersonagemNinjutsu() {
        PersonagemDTO dto = new PersonagemDTO();
        Personagem personagemEntity = new Personagem();
        personagemEntity.setEspecialidade(CategoriaEspecialidadeEnum.NINJUTSU);

        when(personagemMapper.toEntity(dto)).thenReturn(personagemEntity);

        Personagem resultado = personagemFactory.construirTipoPersonagem(dto);

        assertTrue(resultado instanceof NinjaDeNinjutsu);
        verify(personagemMapper).preencherDados(any(NinjaDeNinjutsu.class), eq(personagemEntity));
    }

    @Test
    void deveConstruirPersonagemGenjutsu() {
        PersonagemDTO dto = new PersonagemDTO();
        Personagem personagemEntity = new Personagem();
        personagemEntity.setEspecialidade(CategoriaEspecialidadeEnum.GENJUTSU);

        when(personagemMapper.toEntity(dto)).thenReturn(personagemEntity);

        Personagem resultado = personagemFactory.construirTipoPersonagem(dto);

        assertTrue(resultado instanceof NinjaDeGenjutsu);
        verify(personagemMapper).preencherDados(any(NinjaDeGenjutsu.class), eq(personagemEntity));
    }

    @Test
    void deveLancarExcecaoQuandoEspecialidadeNaoEncontrada() {
        PersonagemDTO dto = new PersonagemDTO();
        Personagem personagemEntity = new Personagem();
        personagemEntity.setEspecialidade(null); 

        when(personagemMapper.toEntity(dto)).thenReturn(personagemEntity);

        Exception exception = assertThrows(NullPointerException.class, () -> {
            personagemFactory.construirTipoPersonagem(dto);
        });

        assertNotNull(exception);
    }
}
