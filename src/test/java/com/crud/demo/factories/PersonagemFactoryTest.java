package com.crud.demo.factories;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.ArgumentMatchers.*;
import static org.mockito.Mockito.*;

import java.util.HashMap;
import java.util.List;
import java.util.Map;
import java.util.function.Supplier;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.test.util.ReflectionTestUtils;

import com.crud.demo.models.Jutsu;
import com.crud.demo.models.Personagem;
import com.crud.demo.models.DTO.PersonagemDTO;
import com.crud.demo.models.mappers.PersonagemMapper;
import com.crud.demo.models.tiposPersonagens.NinjaDeGenjutsu;
import com.crud.demo.models.tiposPersonagens.NinjaDeNinjutsu;
import com.crud.demo.models.tiposPersonagens.NinjaDeTaijutsu;

@ExtendWith(MockitoExtension.class)
class PersonagemFactoryImplTest {

    @Mock
    private PersonagemMapper mapper;

    private PersonagemFactoryImpl factory;

    @BeforeEach
    void setup() {
        factory = new PersonagemFactoryImpl(mapper);

        Map<String, Supplier<Personagem>> tipos = new HashMap<>();
        tipos.put("TAIJUTSU", NinjaDeTaijutsu::new);
        tipos.put("NINJUTSU", NinjaDeNinjutsu::new);
        tipos.put("GENJUTSU", NinjaDeGenjutsu::new);

        ReflectionTestUtils.setField(factory, "mapaDeTipos", tipos);
    }

    @Test
    @DisplayName("Deve retornar NinjaDeTaijutsu quando o tipo for Taijutsu")
    void deveRetornarNinjaDeTaijutsuQuandoTipoTaijutsu() {
        PersonagemDTO dto = new PersonagemDTO();
        Personagem base = mock(Personagem.class);
        when(mapper.toEntity(dto)).thenReturn(base);

        Jutsu jutsu = mock(Jutsu.class);
        when(jutsu.getTipo()).thenReturn("TAIJUTSU");
        when(base.getJutsus()).thenReturn(List.of(jutsu));

        Personagem result = factory.construirTipoPersonagem(dto);

        assertTrue(result instanceof NinjaDeTaijutsu, "Deveria ser NinjaDeTaijutsu");
        verify(mapper).preencherDados((NinjaDeTaijutsu) result, base);
    }

    @Test
    @DisplayName("Deve retornar NinjaDeNinjutsu quando o tipo for Ninjutsu")
    void deveRetornarNinjaDeNinjutsuQuandoTipoNinjutsu() {
        PersonagemDTO dto = new PersonagemDTO();
        Personagem base = mock(Personagem.class);
        when(mapper.toEntity(dto)).thenReturn(base);

        Jutsu jutsu = mock(Jutsu.class);
        when(jutsu.getTipo()).thenReturn("NINJUTSU");
        when(base.getJutsus()).thenReturn(List.of(jutsu));

        Personagem result = factory.construirTipoPersonagem(dto);

        assertTrue(result instanceof NinjaDeNinjutsu, "Deveria ser NinjaDeNinjutsu");
        verify(mapper).preencherDados((NinjaDeNinjutsu) result, base);
    }

    @Test
    @DisplayName("Deve retornar NinjaDeGenjutsu quando o tipo for Genjutsu")
    void deveRetornarNinjaDeGenjutsuQuandoTipoGenjutsu() {
        PersonagemDTO dto = new PersonagemDTO();
        Personagem base = mock(Personagem.class);
        when(mapper.toEntity(dto)).thenReturn(base);

        Jutsu jutsu = mock(Jutsu.class);
        when(jutsu.getTipo()).thenReturn("GENJUTSU");
        when(base.getJutsus()).thenReturn(List.of(jutsu));

        Personagem result = factory.construirTipoPersonagem(dto);

        assertTrue(result instanceof NinjaDeGenjutsu, "Deveria ser NinjaDeGenjutsu");
        verify(mapper).preencherDados((NinjaDeGenjutsu) result, base);
    }

    @Test
    @DisplayName("Deve retornar entidade original quando o tipo for desconhecido")
    void deveRetornarEntidadeOriginalQuandoTipoDesconhecido() {
        PersonagemDTO dto = new PersonagemDTO();
        Personagem base = mock(Personagem.class);
        when(mapper.toEntity(dto)).thenReturn(base);

        Jutsu jutsu = mock(Jutsu.class);
        when(jutsu.getTipo()).thenReturn("KEKKEI_GENKAI");
        when(base.getJutsus()).thenReturn(List.of(jutsu));

        Personagem result = factory.construirTipoPersonagem(dto);

        assertSame(base, result, "Deveria retornar a entidade original");
        verify(mapper, never()).preencherDados(any(), any());
    }

    @Test
    @DisplayName("Deve usar e desviar corretamente para Taijutsu")
    void deveUsarEDesviarCorretamenteParaTaijutsu() {
        NinjaDeTaijutsu ninja = new NinjaDeTaijutsu();
        Personagem alvo = new Personagem();
        alvo.setNome("Sasuke");

        assertEquals(
            "Sasuke está usando um jutsu de Taijutsu!",
            ninja.usarJutsu(alvo)
        );
        assertEquals(
            "Sasuke está desviando de um ataque utilizando Taijutsu!",
            ninja.desviar(alvo)
        );
    }

    @Test
    @DisplayName("Deve usar e desviar corretamente para Ninjutsu")
    void deveUsarEDesviarCorretamenteParaNinjutsu() {
        NinjaDeNinjutsu ninja = new NinjaDeNinjutsu();
        Personagem alvo = new Personagem();
        alvo.setNome("Naruto");

        assertEquals(
            "Naruto está usando um jutsu de Ninjutsu!",
            ninja.usarJutsu(alvo)
        );
        assertEquals(
            "Naruto está desviando de um ataque utilizando Ninjutsu!",
            ninja.desviar(alvo)
        );
    }


    @Test
    @DisplayName("Deve usar e desviar corretamente para Genjutsu")
    void deveUsarEDesviarCorretamenteParaGenjutsu() {
        NinjaDeGenjutsu ninja = new NinjaDeGenjutsu();
        Personagem alvo = new Personagem();
        alvo.setNome("Itachi");

        assertEquals(
            "Itachi está usando um jutsu de Genjutsu!",
            ninja.usarJutsu(alvo),
            "usarJutsu deve indicar Genjutsu"
        );
        assertEquals(
            "Itachi está desviando de um ataque utilizando Genjutsu!",
            ninja.desviar(alvo),
            "desviar deve indicar Genjutsu"
        );
    }


    @Test
    @DisplayName("Deve retornar entidade original quando lista de jutsus estiver vazia")
    void deveRetornarEntidadeOriginalQuandoListaDeJutsusVazia() {
        PersonagemDTO dto = new PersonagemDTO();
        Personagem base = mock(Personagem.class);
        when(mapper.toEntity(dto)).thenReturn(base);
        when(base.getJutsus()).thenReturn(List.of()); 

        Personagem result = factory.construirTipoPersonagem(dto);

        assertSame(base, result, "Sem jutsus, deve retornar entidade original");
        verify(mapper, never()).preencherDados(any(), any());
    }

    @Test
    @DisplayName("Deve tratar tipo ignore case corretamente")
    void deveTratarTipoIgnoreCaseCorretamente() {
        PersonagemDTO dto = new PersonagemDTO();
        Personagem base = mock(Personagem.class);
        when(mapper.toEntity(dto)).thenReturn(base);

        Jutsu jutsu = mock(Jutsu.class);
        when(jutsu.getTipo()).thenReturn("genjutsu"); 
        when(base.getJutsus()).thenReturn(List.of(jutsu));

        Personagem result = factory.construirTipoPersonagem(dto);

        assertTrue(result instanceof NinjaDeGenjutsu, "Deveria mapear genjutsu mesmo em lowercase");
        verify(mapper).preencherDados((NinjaDeGenjutsu) result, base);
    }

    @Test
    @DisplayName("Deve usar primeiro tipo válido quando múltiplos jutsus estiverem presentes")
    void deveUsarPrimeiroTipoValidoQuandoMultiplosJutsusPresentes() {
        PersonagemDTO dto = new PersonagemDTO();
        Personagem base = mock(Personagem.class);
        when(mapper.toEntity(dto)).thenReturn(base);

        Jutsu desconhecido = mock(Jutsu.class);
        when(desconhecido.getTipo()).thenReturn("KARAMATSU");
        Jutsu ninjutsu = mock(Jutsu.class);
        when(ninjutsu.getTipo()).thenReturn("NINJUTSU");
        Jutsu taijutsu = mock(Jutsu.class);

        when(base.getJutsus()).thenReturn(List.of(desconhecido, ninjutsu, taijutsu));

        Personagem result = factory.construirTipoPersonagem(dto);

        assertTrue(result instanceof NinjaDeNinjutsu,
            "Deveria escolher o primeiro tipo válido (NINJUTSU)");
        verify(mapper).preencherDados((NinjaDeNinjutsu) result, base);
    }
}
