package com.crud.demo.services;

import static org.junit.jupiter.api.Assertions.assertEquals;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyLong;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.mock;
import static org.mockito.Mockito.when;
import org.mockito.MockitoAnnotations;

import com.crud.demo.factories.PersonagemFactoryImpl;
import com.crud.demo.models.DTO.PersonagemDTO;
import com.crud.demo.models.Personagem;
import com.crud.demo.models.mappers.PersonagemMapper;
import com.crud.demo.models.tiposPersonagens.NinjaDeNinjutsu;
import com.crud.demo.utils.TestDataFactory;
import com.crud.demo.validators.PersonagemValidator;

class AcoesServiceTest {

    @InjectMocks
    private AcoesServiceImpl acoesService;

    @Mock
    private PersonagemMapper personagemMapper;

    @Mock
    private PersonagemValidator personagemValidator;

    @Mock
    private PersonagemFactoryImpl personagemFactoryImpl;

    private Personagem personagemMock;
    private PersonagemDTO personagemDTO;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);
        personagemMock = TestDataFactory.criarPersonagemEntityComNinjutsu();
        personagemDTO = TestDataFactory.criarPersonagemDTOComNinjutsu();

    }

    @Test
        @DisplayName("Deve atacar com sucesso quando o personagem é ninja de Ninjutsu")
    void deveAtacarComPersonagemNinjaComSucesso() {
        when(personagemMapper.toDto(personagemMock)).thenReturn(personagemDTO);
        when(personagemMapper.toEntity(personagemDTO)).thenReturn(personagemMock);
        when(personagemValidator.validarExistencia(1L)).thenReturn(personagemMock);

        NinjaDeNinjutsu ninjaMock = mock(NinjaDeNinjutsu.class);
        when(personagemFactoryImpl.construirTipoPersonagem(personagemDTO)).thenReturn(ninjaMock);
        when(ninjaMock.usarJutsu(any())).thenReturn("Usando jutsu de Ninjutsu");

        String resultado = acoesService.atacar(1L);

        assertEquals("Usando jutsu de Ninjutsu", resultado);
    }

    @Test
    @DisplayName("Deve informar que não é ninja ao atacar com personagem não ninja")
    void deveInformarNaoNinjaAoAtacarComPersonagemNaoNinja() {
        when(personagemValidator.validarExistencia(1L)).thenReturn(personagemMock);

        when(personagemFactoryImpl.construirTipoPersonagem(personagemDTO)).thenReturn(personagemMock);

        String resultado = acoesService.atacar(1L);

        assertEquals(personagemMock.getNome() + " não é um ninja!", resultado);
    }

    @Test
    @DisplayName("Deve informar que não é ninja ao defender com personagem não ninja")
    void deveInformarNaoNinjaAoDefenderComPersonagemNaoNinja() {
        when(personagemValidator.validarExistencia(anyLong())).thenReturn(personagemMock);

        when(personagemFactoryImpl.construirTipoPersonagem(any())).thenReturn(personagemMock);

        String resultado = acoesService.defender(1L);

        assertEquals(personagemMock.getNome() + " não é um ninja!", resultado);
    }
}
