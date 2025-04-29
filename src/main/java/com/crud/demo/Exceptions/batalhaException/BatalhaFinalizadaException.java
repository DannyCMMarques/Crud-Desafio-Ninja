package com.crud.demo.Exceptions.batalhaException;

import org.springframework.http.HttpStatus;

import com.crud.demo.Exceptions.ApiException;

public class BatalhaFinalizadaException extends ApiException {

    public BatalhaFinalizadaException() {
        super("Batalha já finalizada", HttpStatus.CONFLICT);

    }
}
