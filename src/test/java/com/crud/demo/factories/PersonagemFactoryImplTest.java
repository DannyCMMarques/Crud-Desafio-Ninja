package com.crud.demo.factories;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import com.crud.demo.models.Personagem;
import com.crud.demo.models.DTO.personagem.PersonagemRequestDTO;
import com.crud.demo.models.enuns.CategoriaEspecialidadeEnum;
import com.crud.demo.models.mappers.PersonagemMapper;
import com.crud.demo.models.tiposPersonagens.NinjaDeGenjutsu;
import com.crud.demo.models.tiposPersonagens.NinjaDeNinjutsu;
import com.crud.demo.models.tiposPersonagens.NinjaDeTaijutsu;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class PersonagemFactoryImplTest {

    @Mock
    private PersonagemMapper personagemMapper;

    private PersonagemFactoryImpl personagemFactory;

    @BeforeEach
    void setUp() {
        personagemFactory = new PersonagemFactoryImpl(personagemMapper);
    }

    @Test
    @DisplayName("Deve construir personagem do tipo Taijutsu")
    void deveConstruirPersonagemTaijutsu() {
        PersonagemRequestDTO dto = new PersonagemRequestDTO();
        Personagem personagemEntity = new Personagem();
        personagemEntity.setEspecialidade(CategoriaEspecialidadeEnum.TAIJUTSU);

        when(personagemMapper.toEntity(dto)).thenReturn(personagemEntity);

        Personagem resultado = personagemFactory.construirTipoPersonagem(dto);

        assertTrue(resultado instanceof NinjaDeTaijutsu);
        verify(personagemMapper).toEntity(dto);
    }

    @Test
    @DisplayName("Deve construir personagem do tipo Ninjutsu")
    void deveConstruirPersonagemNinjutsu() {
        PersonagemRequestDTO dto = new PersonagemRequestDTO();
        Personagem personagemEntity = new Personagem();
        personagemEntity.setEspecialidade(CategoriaEspecialidadeEnum.NINJUTSU);

        when(personagemMapper.toEntity(dto)).thenReturn(personagemEntity);

        Personagem resultado = personagemFactory.construirTipoPersonagem(dto);

        assertTrue(resultado instanceof NinjaDeNinjutsu);
        verify(personagemMapper).toEntity(dto);
    }

    @Test
    @DisplayName("Deve construir personagem do tipo Genjutsu")
    void deveConstruirPersonagemGenjutsu() {
        PersonagemRequestDTO dto = new PersonagemRequestDTO();
        Personagem personagemEntity = new Personagem();
        personagemEntity.setEspecialidade(CategoriaEspecialidadeEnum.GENJUTSU);

        when(personagemMapper.toEntity(dto)).thenReturn(personagemEntity);

        Personagem resultado = personagemFactory.construirTipoPersonagem(dto);

        assertTrue(resultado instanceof NinjaDeGenjutsu);
        verify(personagemMapper).toEntity(dto);
    }

    @Test
    @DisplayName("Deve lançar exceção quando especialidade é nula")
    void deveLancarExcecaoQuandoEspecialidadeNaoEncontrada() {
        PersonagemRequestDTO dto = new PersonagemRequestDTO();
        Personagem personagemEntity = new Personagem();

        when(personagemMapper.toEntity(dto)).thenReturn(personagemEntity);

        assertThrows(NullPointerException.class,
                () -> personagemFactory.construirTipoPersonagem(dto));
    }
}
