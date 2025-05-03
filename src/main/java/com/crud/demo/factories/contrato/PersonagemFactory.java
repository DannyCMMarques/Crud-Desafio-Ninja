package com.crud.demo.factories.contrato;

import com.crud.demo.models.DTO.personagem.PersonagemRequestDTO;
import com.crud.demo.models.Personagem;

public interface PersonagemFactory {

    public Personagem construirTipoPersonagem(PersonagemRequestDTO dto);

}
