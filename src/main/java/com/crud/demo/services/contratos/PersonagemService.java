package com.crud.demo.services.contratos;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.crud.demo.models.DTO.PersonagemDTO;

public interface PersonagemService {

    PersonagemDTO criarPersonagem(PersonagemDTO personagemDTO);

    PersonagemDTO buscarPersonagemPorId(Long id);

    void deletarPersonagem(Long id);

    PersonagemDTO atualizarPersonagem(Long id, PersonagemDTO personagemDTO);

    Page<PersonagemDTO> filtrarPersonagens(String nome, long idade, long idadeMin, long idadeMax,
            String aldeia, String jutsus, String chakra, Pageable pageable);

}
