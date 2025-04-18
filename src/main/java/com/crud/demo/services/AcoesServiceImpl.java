package com.crud.demo.services;

import org.springframework.stereotype.Service;

import com.crud.demo.factories.PersonagemFactoryImpl;
import com.crud.demo.models.Personagem;
import com.crud.demo.models.contratos.Ninja;
import com.crud.demo.models.mappers.PersonagemMapper;
import com.crud.demo.services.contratos.AcoesService;
import com.crud.demo.validators.PersonagemValidator;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class AcoesServiceImpl implements AcoesService {

    private final PersonagemMapper personagemMapper;
    private final PersonagemValidator personagemValidator;
    private final PersonagemFactoryImpl personagemFactoryImpl;

    @Override
    public String atacar(Long id) {
        Personagem personagem = personagemValidator.validarExistencia(id);

        Personagem personagemComTipo = personagemFactoryImpl.construirTipoPersonagem(
                personagemMapper.toDto(personagem));

        if (personagemComTipo instanceof Ninja) {
            return ((Ninja) personagemComTipo).usarJutsu(personagemComTipo);
        }

        return personagem.getNome() + " não é um ninja!";
    }

    @Override
    public String defender(Long id) {
        Personagem personagem = personagemValidator.validarExistencia(id);

        Personagem personagemComTipo = personagemFactoryImpl.construirTipoPersonagem(
                personagemMapper.toDto(personagem));

        if (personagemComTipo instanceof Ninja) {
            return ((Ninja) personagemComTipo).desviar(personagemComTipo);
        }

        return personagemComTipo.getNome() + " não é um ninja!";
    }
}
