package com.crud.demo.Exceptions.personagemException;

import com.crud.demo.Exceptions.ApiException;
import org.springframework.http.HttpStatus;

public class NomeJaCadastradoException extends ApiException {

    public NomeJaCadastradoException(String nome) {

        super("O nome " + nome + " já está cadastrado!",
                HttpStatus.CONFLICT);
    }

}
