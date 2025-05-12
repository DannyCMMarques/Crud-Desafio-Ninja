package com.crud.demo.services.contratos;

import java.util.List;

import org.springframework.data.domain.Page;

import com.crud.demo.models.DTO.participanteBatalha.ParticipanteBatalhaRequestDTO;
import com.crud.demo.models.DTO.participanteBatalha.ParticipanteBatalhaResponseDTO;
import com.crud.demo.models.DTO.personagem.PersonagemResponseDTO;

public interface ParticipanteBatalhaService {

    ParticipanteBatalhaResponseDTO criarParticipanteBatalha(ParticipanteBatalhaRequestDTO dto);

    void deletarParticipanteBatalha(Long id);

    ParticipanteBatalhaResponseDTO getParticipanteBatalhaById(Long id);

    ParticipanteBatalhaResponseDTO atualizarParticipanteBatalha(Long id,
            ParticipanteBatalhaRequestDTO participanteBDTO);


    Page<ParticipanteBatalhaResponseDTO> listarTodosParticipantes(String direction, String sortBy, int page, int size);

    List<PersonagemResponseDTO> getPersonagemByBatalhaId(Long id);

}
