package com.crud.demo.services.acaoService;

import org.springframework.stereotype.Service;

import com.crud.demo.events.AtaqueEvent;
import com.crud.demo.services.contratos.AtacarService;
import com.crud.demo.services.contratos.ChakrasService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AtacarServiceImpl implements AtacarService {

    private final ChakrasService chakraService;

    @Override
    public void atacar(AtaqueEvent ataqueEvent) {
        chakraService.atualizarEstatistica(ataqueEvent);
    }
}
