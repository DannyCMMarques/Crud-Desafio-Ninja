package com.crud.demo.exceptions;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.security.SignatureException;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.InjectMocks;
import org.mockito.Mock;
import org.mockito.junit.jupiter.MockitoExtension;
import org.springframework.http.HttpHeaders;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.validation.BindingResult;
import org.springframework.validation.FieldError;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.context.request.WebRequest;

import com.crud.demo.Exceptions.ApiException;
import com.crud.demo.Exceptions.RestErrorMessage;
import com.crud.demo.Exceptions.handler.GlobalExceptionHandler;

@ExtendWith(MockitoExtension.class)
class GlobalExceptionHandlerTest {

    @InjectMocks
    private GlobalExceptionHandler handler;

    @Mock
    private WebRequest webRequest;

    @Mock
    private MethodArgumentNotValidException methodEx;

    @Test
    @DisplayName("handleMethodArgumentNotValid deve retornar BAD_REQUEST com lista de erros")
    void deveRetornarBadRequestComErrosDeValidacao() {

        FieldError fe1 = new FieldError("obj", "field1", "must not be blank");
        FieldError fe2 = new FieldError("obj", "field2", "size must be >= 5");

        List<FieldError> fieldErrors = Arrays.asList(fe1, fe2);
        BindingResult bindingResult = mock(BindingResult.class);
        when(bindingResult.getFieldErrors()).thenReturn(fieldErrors);
        when(methodEx.getBindingResult()).thenReturn(bindingResult);

        ResponseEntity<Object> response = handler.handleMethodArgumentNotValid(
                methodEx,
                new HttpHeaders(),
                HttpStatus.BAD_REQUEST,
                webRequest);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertTrue(response.getBody() instanceof Map);

        @SuppressWarnings("unchecked")
        Map<String, List<String>> body = (Map<String, List<String>>) response.getBody();
        List<String> erros = body.get("erros");

        assertNotNull(erros);
        assertEquals(2, erros.size());
        assertTrue(erros.contains("field1: must not be blank"));
        assertTrue(erros.contains("field2: size must be >= 5"));
    }

    @Test
    @DisplayName("handleApiException deve propagar status da ApiException")
    void deveRetornarNotFoundParaApiException() {

        ApiException apiEx = new ApiException("Resource not found", HttpStatus.NOT_FOUND) {
        };

        ResponseEntity<RestErrorMessage> response = handler.handleApiException(apiEx);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        RestErrorMessage body = response.getBody();
        assertNotNull(body);
        assertEquals(HttpStatus.NOT_FOUND, body.getStatus());
        assertEquals("Resource not found", body.getMessage());
        assertFalse(body.getTimestamp().isAfter(LocalDateTime.now()));
    }

    @Test
    @DisplayName("handleSignatureException deve retornar UNAUTHORIZED")
    void deveRetornarUnauthorizedParaSignatureException() {

        SignatureException sigEx = new SignatureException("Invalid signature");

        ResponseEntity<RestErrorMessage> response = handler.handleSignatureException(sigEx);

        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
        RestErrorMessage body = response.getBody();
        assertNotNull(body);
        assertEquals(HttpStatus.UNAUTHORIZED, body.getStatus());
        assertEquals("O token está inválido", body.getMessage());
    }

    @Test
    @DisplayName("genericExceptionHandler deve retornar INTERNAL_SERVER_ERROR")
    void deveRetornarInternalServerErrorParaExcecaoGenerica() {

        Exception ex = new RuntimeException("Unexpected error");

        ResponseEntity<RestErrorMessage> response = handler.genericExceptionHandler(ex);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        RestErrorMessage body = response.getBody();
        assertNotNull(body);
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, body.getStatus());
        assertEquals("Erro interno no servidor.", body.getMessage());
    }
}
