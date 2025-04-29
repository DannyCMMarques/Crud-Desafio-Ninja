package com.crud.demo.Exceptions.batalhaException;

import org.springframework.http.HttpStatus;

import com.crud.demo.Exceptions.ApiException;

public class BatalhaNaoEncontradaException extends ApiException {

    public BatalhaNaoEncontradaException() {
        super("Batalha não encontrado", HttpStatus.NOT_FOUND);

    }
}
