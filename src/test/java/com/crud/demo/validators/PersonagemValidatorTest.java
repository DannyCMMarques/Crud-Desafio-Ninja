package com.crud.demo.validators;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.util.Optional;

import com.crud.demo.Exceptions.personagemException.NomeJaCadastradoException;
import com.crud.demo.Exceptions.personagemException.PersonagemNaoEncontradoException;
import com.crud.demo.models.Personagem;
import com.crud.demo.repositories.PersonagemRepository;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;

@ExtendWith(MockitoExtension.class)
class PersonagemValidatorTest {

        @Mock
        private PersonagemRepository personagemRepository;

        @InjectMocks
        private PersonagemValidator validator;

        @Test
        @DisplayName("Deve validar cadastro sem erro quando nome não existir")
        void deveValidarCadastroSemErroQuandoNomeNaoExistir() {
                String nome = "Naruto";
                when(personagemRepository.findByNome(nome))
                                .thenReturn(Optional.empty());

                assertDoesNotThrow(() -> validator.validarCadastro(nome));
                verify(personagemRepository).findByNome(nome);
        }

        @Test
        @DisplayName("Deve lançar NomeJaCadastradoException quando nome já estiver cadastrado")
        void deveLancarExceptionQuandoNomeJaCadastrado() {
                String nome = "Sasuke";
                Personagem existente = new Personagem();
                existente.setNome(nome);
                when(personagemRepository.findByNome(nome))
                                .thenReturn(Optional.of(existente));

                NomeJaCadastradoException ex = assertThrows(
                                NomeJaCadastradoException.class,
                                () -> validator.validarCadastro(nome));
                assertTrue(ex.getMessage().contains(nome));
                verify(personagemRepository).findByNome(nome);
        }

        @Test
        @DisplayName("Deve retornar personagem quando validar existência com ID existente")
        void deveRetornarPersonagemQuandoIdExistir() {
                Long id = 1L;
                Personagem p = new Personagem();
                p.setId(id);
                when(personagemRepository.findById(id))
                                .thenReturn(Optional.of(p));

                Personagem resultado = validator.validarExistencia(id);

                assertNotNull(resultado);
                assertEquals(id, resultado.getId());
                verify(personagemRepository).findById(id);
        }

        @Test
        @DisplayName("Deve lançar PersonagemNaoEncontradoException quando ID não existir")
        void deveLancarExceptionQuandoIdNaoExistir() {
                Long id = 99L;
                when(personagemRepository.findById(id))
                                .thenReturn(Optional.empty());

                assertThrows(
                                PersonagemNaoEncontradoException.class,
                                () -> validator.validarExistencia(id));
                verify(personagemRepository).findById(id);
        }
}
