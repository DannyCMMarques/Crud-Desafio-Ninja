package com.crud.demo.validators;

import org.springframework.stereotype.Component;

import com.crud.demo.Exceptions.batalhaException.BatalhaFinalizadaException;
import com.crud.demo.Exceptions.batalhaException.BatalhaNaoEncontradaException;
import com.crud.demo.models.Batalha;
import com.crud.demo.repositories.BatalhaRepository;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class BatalhaValidators {

    private final BatalhaRepository batalhaRepository;

    public Batalha validarExistencia(Long id) {
        return batalhaRepository.findById(id)
                .orElseThrow(BatalhaNaoEncontradaException::new);
    }

    public void verificarBatalhaEncerrada(Long id) throws BatalhaFinalizadaException {
        Batalha batalha = validarExistencia(id);
        if (batalha.getFinalizadoEm() != null) {
            throw new BatalhaFinalizadaException();
        }
    }
}
