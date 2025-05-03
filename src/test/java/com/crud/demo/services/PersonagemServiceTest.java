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
import com.crud.demo.models.DTO.personagem.PersonagemRequestDTO;
import com.crud.demo.models.DTO.personagem.PersonagemResponseDTO;
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
    private PersonagemResponseDTO personagemResponseDTO;
    private PersonagemRequestDTO personagemRequestDTO;

    @BeforeEach
    void setUp() {
        MockitoAnnotations.openMocks(this);

        personagemMock = TestDataFactory.criarPersonagemEntityComNinjutsu();
        personagemResponseDTO = TestDataFactory.criarPersonagemResponseDTOComNinjutsu();
        personagemRequestDTO = new PersonagemRequestDTO();
        personagemRequestDTO.setNome("Naruto Uzumaki");
        personagemRequestDTO.setIdade(30L);
        personagemRequestDTO.setAldeia("Konoha");
    }

    @Test
    @DisplayName("Deve salvar um personagem com jutsu associado corretamente")
    void deveSalvarPersonagemComSucesso() {
        when(personagemRepository.save(any(Personagem.class))).thenReturn(personagemMock);
        when(personagemMapper.toEntity(personagemResponseDTO)).thenReturn(personagemMock);
        when(personagemMapper.toDto(personagemMock)).thenReturn(personagemResponseDTO);
        when(jutsuRepository.findByTipo(anyString())).thenReturn(java.util.Optional.empty());
        when(jutsuRepository.save(any(Jutsu.class))).thenReturn(TestDataFactory.criarJutsuNinjutsu());

        when(personagemFactoryImpl.construirTipoPersonagem(personagemRequestDTO)).thenReturn(personagemMock);
        PersonagemResponseDTO personagemResponseDTOCriado = personagemService.criarPersonagem(personagemRequestDTO);

        assertEquals(personagemMock.getNome(), personagemResponseDTOCriado.getNome());

    }

    @Test
    @DisplayName("Deve buscar um personagem por ID")
    void deveBuscarPersonagemPorIdComSucesso() {
        when(personagemValidator.validarExistencia(1L)).thenReturn(personagemMock);
        when(personagemMapper.toDto(personagemMock)).thenReturn(personagemResponseDTO);

        PersonagemResponseDTO personagemResponseDTOEncontrado = personagemService.buscarPersonagemPorId(1L);

        verify(personagemValidator).validarExistencia(1L);
        assertEquals(personagemMock.getNome(), personagemResponseDTOEncontrado.getNome());
        assertEquals(personagemMock.getIdade(), personagemResponseDTOEncontrado.getIdade());
    }

    @Test
    @DisplayName("Deve listar todos os personagens")
    void deveListarPersonagensComSucesso() {
        when(personagemRepository.findAll(any(Specification.class), any(Pageable.class)))
                .thenReturn(new PageImpl<>(List.of(personagemMock)));

        when(personagemMapper.toDto(any(Personagem.class))).thenReturn(personagemResponseDTO);

        Pageable pageable = PageRequest.of(0, 10, Sort.by("nome").ascending());

        Page<PersonagemResponseDTO> personagensPage = personagemService.filtrarPersonagens(
                null, null, null, null, null, 0, 10, "asc", "nome", null);

        assertNotNull(personagensPage);
        assertEquals(1, personagensPage.getContent().size());
        assertEquals(personagemMock.getNome(), personagensPage.getContent().get(0).getNome());
    }

    @Test
    @DisplayName("Deve atualizar um personagem")
    void deveAtualizarPersonagemComSucesso() {
        when(personagemValidator.validarExistencia(1L)).thenReturn(personagemMock);

        when(personagemMapper.toDto(personagemMock)).thenReturn(personagemResponseDTO);

        when(personagemRepository.save(any(Personagem.class))).thenReturn(personagemMock);
        personagemRequestDTO.setNome("Naruto Uzumaki Atualizado");

        PersonagemResponseDTO personagemAtualizado = personagemService.atualizarPersonagem(1L, personagemRequestDTO);

        verify(personagemRepository).save(any(Personagem.class));

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
