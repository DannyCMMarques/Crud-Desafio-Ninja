package com.crud.demo.exceptions.batalhaException;

import static org.junit.jupiter.api.Assertions.assertEquals;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.springframework.http.HttpStatus;

import com.crud.demo.Exceptions.batalhaException.BatalhaFinalizadaException;

class BatalhaFinalizadaExceptionTest {

    @Test
    @DisplayName("Deve conter mensagem padrão e status CONFLICT")
    void testExceptionDetails() {
        BatalhaFinalizadaException ex = new BatalhaFinalizadaException();
        assertEquals("Batalha já finalizada", ex.getMessage());
        assertEquals(HttpStatus.CONFLICT, ex.getStatus());
    }
}
