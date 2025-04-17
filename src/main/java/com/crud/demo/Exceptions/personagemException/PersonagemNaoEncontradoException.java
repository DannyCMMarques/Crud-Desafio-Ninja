package com.crud.demo.Exceptions.personagemException;

import org.springframework.http.HttpStatus;

import com.crud.demo.Exceptions.ApiException;

public class PersonagemNaoEncontradoException extends ApiException {

      public PersonagemNaoEncontradoException() {
            super("Personagem não encontrado", HttpStatus.NOT_FOUND);

      }
}