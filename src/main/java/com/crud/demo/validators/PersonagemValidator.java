package com.crud.demo.validators;

import java.util.Optional;

import org.springframework.stereotype.Component;

import com.crud.demo.Exceptions.personagemException.NomeJaCadastradoException;
import com.crud.demo.Exceptions.personagemException.PersonagemNaoEncontradoException;
import com.crud.demo.models.Personagem;
import com.crud.demo.repositories.PersonagemRepository;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class PersonagemValidator {

    private final PersonagemRepository personagemRepository;

    public void validarCadastro(String nome) {

        Optional<Personagem> nomeCadastrado = personagemRepository.findByNome(nome);
        if (nomeCadastrado.isPresent()) {
            throw new NomeJaCadastradoException(nomeCadastrado.get().getNome());
        }

    }

    public Personagem validarExistencia(Long id) {
        return personagemRepository.findById(id)
                .orElseThrow(PersonagemNaoEncontradoException::new);
    }

    public Personagem validarExistencia(String nome) {
        return personagemRepository.findByNome(nome)
                .orElseThrow(PersonagemNaoEncontradoException::new);
    }

}
