package com.crud.demo.exceptions.batalhaException;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import com.crud.demo.Exceptions.batalhaException.ParticipanteBatalhaNaoEncontradaException;

class ParticipanteBatalhaNaoEncontradaExceptionTest {

    @Test
    @DisplayName("Deve conter mensagem padrão e status NOT_FOUND para Participante não encontrado")
    void testParticipanteNaoEncontradaException() {
        ParticipanteBatalhaNaoEncontradaException ex = new ParticipanteBatalhaNaoEncontradaException();
        assertEquals("ParticipanteBatalha não encontrado", ex.getMessage());
        assertEquals(HttpStatus.NOT_FOUND, ex.getStatus());
    }
}
