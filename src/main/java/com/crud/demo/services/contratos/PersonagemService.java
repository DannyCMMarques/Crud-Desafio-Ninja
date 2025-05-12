package com.crud.demo.services.contratos;

import org.springframework.data.domain.Page;
import com.crud.demo.models.DTO.personagem.PersonagemRequestDTO;
import com.crud.demo.models.DTO.personagem.PersonagemResponseDTO;
import com.crud.demo.models.enuns.CategoriaEspecialidadeEnum;

public interface PersonagemService {

    PersonagemResponseDTO criarPersonagem(PersonagemRequestDTO personagemRequestDTO);

    PersonagemResponseDTO buscarPersonagemPorId(Long id);

    void deletarPersonagem(Long id);

    PersonagemResponseDTO atualizarPersonagem(Long id, PersonagemRequestDTO personagemRequestDTO);

    Page<PersonagemResponseDTO> filtrarPersonagens(
            String nome,
            Long idade,
            Long idadeMin,
            Long idadeMax,
            String aldeia,
            int page,
            int size,
            String direction,
            String sortBy,
            CategoriaEspecialidadeEnum especialidade);

    PersonagemResponseDTO getPersonagemByNome(String nome);

}
