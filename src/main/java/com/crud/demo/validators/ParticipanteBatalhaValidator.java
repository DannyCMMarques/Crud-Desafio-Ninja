package com.crud.demo.validators;

import org.springframework.stereotype.Component;

import com.crud.demo.Exceptions.batalhaException.ParticipanteBatalhaNaoEncontradaException;
import com.crud.demo.repositories.ParticipanteBatalhaRepository;
import com.crud.demo.models.ParticipanteBatalha;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class ParticipanteBatalhaValidator {

    private final ParticipanteBatalhaRepository participanteBatalhaRepository;

    public ParticipanteBatalha verificarParticipanteBatalhaDTO(Long id) {
        return participanteBatalhaRepository.findById(id)
                .orElseThrow(ParticipanteBatalhaNaoEncontradaException::new);
    }
}
