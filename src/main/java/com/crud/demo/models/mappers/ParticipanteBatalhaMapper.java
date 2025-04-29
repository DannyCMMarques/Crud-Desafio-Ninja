package com.crud.demo.models.mappers;

import org.springframework.stereotype.Component;

import com.crud.demo.models.Batalha;
import com.crud.demo.models.ParticipanteBatalha;
import com.crud.demo.models.Personagem;
import com.crud.demo.models.Usuario;
import com.crud.demo.models.DTO.ParticipanteBatalhaDTO;
import com.crud.demo.validators.BatalhaValidators;
import com.crud.demo.validators.PersonagemValidator;

import lombok.Builder;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
@Builder
public class ParticipanteBatalhaMapper {

  private final PersonagemValidator personagemValidator;
  private final BatalhaValidators batalhaValidators;
    public ParticipanteBatalhaDTO toDto(ParticipanteBatalha participante) {
        if (participante == null)
            return null;

        return ParticipanteBatalhaDTO.builder()
                .id(participante.getId())
                .batalha(participante.getBatalha().getId()) 
                .nomeUsuario(participante.getUsuario().getNome()) 
                .personagem(participante.getPersonagem().getNome()) 
                .playerOrder(participante.getPlayerOrder())
                .vencedor(participante.getVencedor())
                .build();
    }

    public ParticipanteBatalha toEntity(ParticipanteBatalhaDTO dto, Usuario usuario) {
        if (dto == null)
            return null;
            Long idBatalha= dto.getBatalha();
      Batalha batalha = batalhaValidators.validarExistencia(idBatalha);
      Personagem personagem = personagemValidator.validarExistencia(dto.getPersonagem()); 
      return ParticipanteBatalha.builder()
                .id(dto.getId())
                .batalha(batalha)
                .usuario(usuario)
                .personagem(personagem) 
                .playerOrder(dto.getPlayerOrder())
                .vencedor(dto.getVencedor())
                .build();
    }
}
