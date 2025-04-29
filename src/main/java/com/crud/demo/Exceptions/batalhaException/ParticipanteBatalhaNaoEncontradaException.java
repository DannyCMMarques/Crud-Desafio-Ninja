package com.crud.demo.Exceptions.batalhaException;

import org.springframework.http.HttpStatus;

import com.crud.demo.Exceptions.ApiException;

public class ParticipanteBatalhaNaoEncontradaException extends ApiException {

    public ParticipanteBatalhaNaoEncontradaException() {
        super("ParticipanteBatalha não encontrado", HttpStatus.NOT_FOUND);

    }
}
