package com.crud.demo.exceptions;

import static org.junit.jupiter.api.Assertions.*;
import static org.mockito.Mockito.*;

import java.security.SignatureException;
import java.time.LocalDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
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

    @BeforeEach
    void setUp() {
    }

    @Test
        @DisplayName("Deve retornar Bad Request com erros de validação")
    void deveRetornarBadRequestComErrosDeValidacao() {
        FieldError fe1 = new FieldError("obj", "field1", "must not be blank");
        FieldError fe2 = new FieldError("obj", "field2", "size must be >= 5");
        List<FieldError> fieldErrors = Arrays.asList(fe1, fe2);
        BindingResult bindingResult = mock(BindingResult.class);
        when(bindingResult.getFieldErrors()).thenReturn(fieldErrors);
        when(methodEx.getBindingResult()).thenReturn(bindingResult);

        HttpHeaders headers = new HttpHeaders();
        HttpStatus statusCode = HttpStatus.BAD_REQUEST;

        ResponseEntity<Object> response = handler.handleMethodArgumentNotValid(
                methodEx,
                headers,
                statusCode,
                webRequest);

        assertEquals(HttpStatus.BAD_REQUEST, response.getStatusCode());
        assertTrue(response.getBody() instanceof Map<?, ?>);
        @SuppressWarnings("unchecked")
        Map<String, List<String>> body = (Map<String, List<String>>) response.getBody();
        List<String> errors = body.get("erros");
        assertNotNull(errors);
        assertEquals(2, errors.size());
        assertTrue(errors.contains("field1: must not be blank"));
        assertTrue(errors.contains("field2: size must be >= 5"));
    }

    @Test
    @DisplayName("Deve retornar NOT_FOUND para ApiException personalizada")
    void deveRetornarNotFoundParaApiException() {
        ApiException apiEx = new ApiException("Resource not found", HttpStatus.NOT_FOUND) {};

        ResponseEntity<RestErrorMessage> response = handler.handleApiException(apiEx);

        assertEquals(HttpStatus.NOT_FOUND, response.getStatusCode());
        RestErrorMessage body = response.getBody();
        assertNotNull(body);
        assertEquals(HttpStatus.NOT_FOUND, body.getStatus());
        assertEquals("Resource not found", body.getMessage());
        assertNotNull(body.getTimestamp());
        assertFalse(body.getTimestamp().isAfter(LocalDateTime.now()));
    }

    @Test
    @DisplayName("Deve retornar UNAUTHORIZED para SignatureException")
    void deveRetornarUnauthorizedParaSignatureException() {
        SignatureException sigEx = new SignatureException("Invalid signature");

        ResponseEntity<RestErrorMessage> response = handler.handleSignatureException(sigEx);

        assertEquals(HttpStatus.UNAUTHORIZED, response.getStatusCode());
        RestErrorMessage body = response.getBody();
        assertNotNull(body);
        assertEquals(HttpStatus.UNAUTHORIZED, body.getStatus());
        assertEquals("O token está inválido", body.getMessage());
        assertNotNull(body.getTimestamp());
    }

    @Test
    @DisplayName("Deve retornar INTERNAL_SERVER_ERROR para exceção genérica")
    void deveRetornarInternalServerErrorParaExcecaoGenerica() {
        Exception ex = new RuntimeException("Unexpected error");

        ResponseEntity<RestErrorMessage> response = handler.genericExceptionHandler(ex);

        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, response.getStatusCode());
        RestErrorMessage body = response.getBody();
        assertNotNull(body);
        assertEquals(HttpStatus.INTERNAL_SERVER_ERROR, body.getStatus());
        assertEquals("Erro interno no servidor.", body.getMessage());
        assertNotNull(body.getTimestamp());
    }
}
