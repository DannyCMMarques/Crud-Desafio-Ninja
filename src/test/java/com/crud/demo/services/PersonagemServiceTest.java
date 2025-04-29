package com.crud.demo.services;

import java.util.List;

import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertNotNull;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import static org.mockito.ArgumentMatchers.any;
import static org.mockito.ArgumentMatchers.anyString;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.MockitoAnnotations;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageImpl;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.data.jpa.domain.Specification;

import com.crud.demo.factories.PersonagemFactoryImpl;
import com.crud.demo.models.DTO.PersonagemDTO;
import com.crud.demo.models.Jutsu;
import com.crud.demo.models.Personagem;
import com.crud.demo.models.mappers.JutsuMapper;
import com.crud.demo.models.mappers.PersonagemMapper;
import com.crud.demo.repositories.JutsuRepository;
import com.crud.demo.repositories.PersonagemRepository;
import com.crud.demo.utils.TestDataFactory;
import com.crud.demo.validators.PersonagemValidator;

class PersonagemServiceTest {

    @InjectMocks
    private PersonagemServiceImpl personagemService;

    @Mock
    private PersonagemRepository personagemRepository;

    @Mock
    private PersonagemMapper personagemMapper;

    @Mock
    private PersonagemValidator personagemValidator;

    @Mock
    private PersonagemFactoryImpl personagemFactoryImpl;

    @Mock
    private JutsuMapper jutsuMapper;

    @Mock
    private JutsuRepository jutsuRepository;
    private Personagem personagemMock;
    private PersonagemDTO personagemDTO;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        personagemMock = TestDataFactory.criarPersonagemEntityComNinjutsu();
        personagemDTO = TestDataFactory.criarPersonagemDTOComNinjutsu();
    }

    @Test
    @DisplayName("Deve salvar um personagem com jutsu associado corretamente")
    void deveSalvarPersonagemComSucesso() {
        when(personagemRepository.save(any(Personagem.class))).thenReturn(personagemMock);
        when(personagemMapper.toEntity(personagemDTO)).thenReturn(personagemMock);
        when(personagemMapper.toDto(personagemMock)).thenReturn(personagemDTO);
        when(jutsuRepository.findByTipo(anyString())).thenReturn(java.util.Optional.empty());
        when(jutsuRepository.save(any(Jutsu.class))).thenReturn(TestDataFactory.criarJutsuNinjutsu());

        when(personagemFactoryImpl.construirTipoPersonagem(personagemDTO)).thenReturn(personagemMock);
        PersonagemDTO personagemDTOCriado = personagemService.criarPersonagem(personagemDTO);

        assertEquals(personagemMock.getNome(), personagemDTOCriado.getNome());

    
    }

    @Test
    @DisplayName("Deve buscar um personagem por ID")
    void deveBuscarPersonagemPorIdComSucesso() {
        when(personagemValidator.validarExistencia(1L)).thenReturn(personagemMock);
        when(personagemMapper.toDto(personagemMock)).thenReturn(personagemDTO);

        PersonagemDTO personagemDTOEncontrado = personagemService.buscarPersonagemPorId(1L);

        verify(personagemValidator).validarExistencia(1L);
        assertEquals(personagemMock.getNome(), personagemDTOEncontrado.getNome());
        assertEquals(personagemMock.getIdade(), personagemDTOEncontrado.getIdade());
    }

    @Test
    @DisplayName("Deve listar todos os personagens")
    void deveListarPersonagensComSucesso() {
        when(personagemRepository.findAll(any(Specification.class), any(Pageable.class)))
                .thenReturn(new PageImpl<>(List.of(personagemMock)));

        when(personagemMapper.toDto(any(Personagem.class))).thenReturn(personagemDTO);

        Pageable pageable = PageRequest.of(0, 10, Sort.by("nome").ascending());

        Page<PersonagemDTO> personagens = personagemService.filtrarPersonagens(
                null, null, null, null, null, pageable, null);

        assertNotNull(personagens);
        assertEquals(1, personagens.getContent().size());
        assertEquals(personagemMock.getNome(), personagens.getContent().get(0).getNome());
    }

    @Test
    @DisplayName("Deve atualizar um personagem")
    void deveAtualizarPersonagemComSucesso() {
        when(personagemValidator.validarExistencia(1L)).thenReturn(personagemMock);

        when(personagemMapper.toDto(personagemMock)).thenReturn(personagemDTO);

        when(personagemRepository.save(any(Personagem.class))).thenReturn(personagemMock);

        personagemDTO.setNome("Naruto Uzumaki Atualizado");

        PersonagemDTO personagemAtualizado = personagemService.atualizarPersonagem(1L, personagemDTO);

        verify(personagemRepository).save(any(Personagem.class));

        assertEquals("Naruto Uzumaki Atualizado", personagemAtualizado.getNome());
        assertEquals(personagemMock.getIdade(), personagemAtualizado.getIdade());
        assertEquals(personagemMock.getAldeia(), personagemAtualizado.getAldeia());
    }

    @Test
    @DisplayName("Deve deletar um personagem")
    void deveDeletarPersonagemComSucesso() {
        when(personagemValidator.validarExistencia(1L)).thenReturn(personagemMock);

        personagemService.deletarPersonagem(1L);

        verify(personagemValidator).validarExistencia(1L);
        verify(personagemRepository).deleteById(1L);
    }
}
