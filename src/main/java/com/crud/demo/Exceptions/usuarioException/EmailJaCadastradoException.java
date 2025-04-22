package com.crud.demo.Exceptions.usuarioException;

import org.springframework.http.HttpStatus;

import com.crud.demo.Exceptions.ApiException;

public class EmailJaCadastradoException extends ApiException {
    public EmailJaCadastradoException(String email) {

        super("O email " + email + " já está cadastrado!",
                HttpStatus.CONFLICT);
    }

}
