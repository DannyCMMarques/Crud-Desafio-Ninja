package com.crud.demo.Exceptions.jutsusExceptions;

import org.springframework.http.HttpStatus;

import com.crud.demo.Exceptions.ApiException;

public class JutsuNaoEncontradoException extends ApiException {

    public JutsuNaoEncontradoException() {
        super("Jutsu não encontrado", HttpStatus.NOT_FOUND);
    }
}
