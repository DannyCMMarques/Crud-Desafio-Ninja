package com.crud.demo.services;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.stereotype.Service;

import com.crud.demo.models.Batalha;
import com.crud.demo.models.ParticipanteBatalha;
import com.crud.demo.models.Personagem;
import com.crud.demo.models.Usuario;
import com.crud.demo.models.DTO.BatalhaDTO;
import com.crud.demo.models.DTO.ParticipanteBatalhaDTO;
import com.crud.demo.models.DTO.PersonagemDTO;
import com.crud.demo.models.mappers.BatalhaMapper;
import com.crud.demo.models.mappers.ParticipanteBatalhaMapper;
import com.crud.demo.models.mappers.PersonagemMapper;
import com.crud.demo.repositories.ParticipanteBatalhaRepository;
import com.crud.demo.services.contratos.BatalhaService;
import com.crud.demo.services.contratos.ParticipanteBatalhaService;
import com.crud.demo.validators.ParticipanteBatalhaValidator;
import com.crud.demo.validators.UsuarioValidator;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class ParticipanteBatalhaServiceImpl implements ParticipanteBatalhaService {

    private final ParticipanteBatalhaRepository participanteBatalhaRepository;
    private final ParticipanteBatalhaValidator participanteBatalhaValidator;
    private final ParticipanteBatalhaMapper participanteBatMapper;
    private final UsuarioValidator usuarioValidator;
    private final PersonagemMapper personagemMapper;
    private final BatalhaService batalhaService;
    private final BatalhaMapper batalhaMapper;

    @Override
    public ParticipanteBatalhaDTO criarParticipanteBatalha(ParticipanteBatalhaDTO dto) {
        Usuario usuario = usuarioValidator.validarExistencia(dto.getNomeUsuario());

        ParticipanteBatalha participanteEntity = participanteBatMapper.toEntity(dto, usuario);
        ParticipanteBatalha salvo = participanteBatalhaRepository.save(participanteEntity);
        return participanteBatMapper.toDto(salvo);
    }

    @Override
    public void deletarParticipanteBatalha(Long id) {
        participanteBatalhaValidator.verificarParticipanteBatalhaDTO(id);
        participanteBatalhaRepository.deleteById(id);
    }

    @Override
    public List<PersonagemDTO> getPersonagemByBatalhaId(Long id) {
        BatalhaDTO batalha = batalhaService.getBatalhaById(id);
        Batalha batalhaEntity = batalhaMapper.toEntity(batalha);
        Optional<List<ParticipanteBatalha>> participantesIdBatalhas = participanteBatalhaRepository
                .findByBatalha(batalhaEntity);

        List<Personagem> personagens = participantesIdBatalhas
                .orElseThrow(() -> new RuntimeException("Nenhum participante encontrado para a batalha."))
                .stream()
                .map(ParticipanteBatalha::getPersonagem)
                .collect(Collectors.toList());

        return personagens.stream().map(personagem -> personagemMapper.toDto(personagem)).collect(Collectors.toList());
    }

    @Override
    public ParticipanteBatalhaDTO getParticipanteBatalhaById(Long id) {
        ParticipanteBatalha participanteBatalha = participanteBatalhaValidator.verificarParticipanteBatalhaDTO(id);
        return participanteBatMapper.toDto(participanteBatalha);
    }

    @Override
    public ParticipanteBatalhaDTO atualizarParticipanteBatalha(Long id, ParticipanteBatalhaDTO participanteBDTO) {
        ParticipanteBatalha participanteBatalhaEncontrado = participanteBatalhaValidator
                .verificarParticipanteBatalhaDTO(id);

        participanteBatalhaEncontrado.setPlayerOrder(participanteBDTO.getPlayerOrder());
        participanteBatalhaEncontrado.setVencedor(participanteBDTO.getVencedor());

        ParticipanteBatalha participanteAtualizado = participanteBatalhaRepository.save(participanteBatalhaEncontrado);
        return participanteBatMapper.toDto(participanteAtualizado);
    }

    @Override
    public Page<ParticipanteBatalhaDTO> listarTodosParticipantes(Pageable pageable) {
        Page<ParticipanteBatalha> participantesBatalhas = participanteBatalhaRepository.findAll(pageable);
        return participantesBatalhas.map(participanteBatMapper::toDto);
    }

}
