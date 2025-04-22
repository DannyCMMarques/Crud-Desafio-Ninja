package com.crud.demo.Exceptions.usuarioException;

import org.springframework.http.HttpStatus;

import com.crud.demo.Exceptions.ApiException;

public class UsuarioNaoEncontradoException extends ApiException {

    public UsuarioNaoEncontradoException() {
        super("Usuário não encontrado!", HttpStatus.NOT_FOUND);
    }

}
