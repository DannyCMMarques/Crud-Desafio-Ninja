package com.crud.demo.services.contratos;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.crud.demo.models.DTO.PersonagemDTO;

public interface PersonagemService {

    PersonagemDTO criarPersonagem(PersonagemDTO personagemDTO);

    PersonagemDTO buscarPersonagemPorId(Long id);

    void deletarPersonagem(Long id);

    PersonagemDTO atualizarPersonagem(Long id, PersonagemDTO personagemDTO);

    Page<PersonagemDTO> filtrarPersonagens(String nome, Long idade, Long idadeMin, Long idadeMax,
            String aldeia, String jutsus, Long chakra, Pageable pageable);



}
