package com.crud.demo.Exceptions.usuarioException;

import org.springframework.http.HttpStatus;

import com.crud.demo.Exceptions.ApiException;

public class UsuarioNaoAutorizadoException extends ApiException {
    public UsuarioNaoAutorizadoException() {
        super("Usuário não autorizado", HttpStatus.UNAUTHORIZED);
    }
}
