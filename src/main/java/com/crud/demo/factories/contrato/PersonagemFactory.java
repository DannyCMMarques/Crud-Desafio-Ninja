package com.crud.demo.factories.contrato;

import com.crud.demo.models.Personagem;
import com.crud.demo.models.DTO.PersonagemDTO;

public interface PersonagemFactory {

    public Personagem construirTipoPersonagem(PersonagemDTO dto);

}
