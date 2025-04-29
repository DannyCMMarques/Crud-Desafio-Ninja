package com.crud.demo.services.contratos;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;

import com.crud.demo.models.DTO.ParticipanteBatalhaDTO;
import com.crud.demo.models.DTO.PersonagemDTO;

public interface ParticipanteBatalhaService {

    ParticipanteBatalhaDTO criarParticipanteBatalha(ParticipanteBatalhaDTO dto);

    void deletarParticipanteBatalha(Long id);

    ParticipanteBatalhaDTO getParticipanteBatalhaById(Long id);

    ParticipanteBatalhaDTO atualizarParticipanteBatalha(Long id, ParticipanteBatalhaDTO participanteBDTO);

    Page<ParticipanteBatalhaDTO> listarTodosParticipantes(Pageable pageable);

    List<PersonagemDTO> getPersonagemByBatalhaId(Long id);

}
