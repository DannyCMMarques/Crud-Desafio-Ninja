package com.crud.demo.services;

import java.util.List;
import java.util.Optional;
import java.util.stream.Collectors;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.crud.demo.models.Batalha;
import com.crud.demo.models.ParticipanteBatalha;
import com.crud.demo.models.Personagem;
import com.crud.demo.models.Usuario;
import com.crud.demo.models.DTO.batalha.BatalhaResponseDTO;
import com.crud.demo.models.DTO.participanteBatalha.ParticipanteBatalhaRequestDTO;
import com.crud.demo.models.DTO.participanteBatalha.ParticipanteBatalhaResponseDTO;
import com.crud.demo.models.DTO.personagem.PersonagemResponseDTO;
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
    public ParticipanteBatalhaResponseDTO criarParticipanteBatalha(ParticipanteBatalhaRequestDTO dto) {
        Usuario usuario = usuarioValidator.validarExistencia(dto.getNomeUsuario());

        ParticipanteBatalha participanteEntity = participanteBatMapper.toEntity(dto);
        ParticipanteBatalha salvo = participanteBatalhaRepository.save(participanteEntity);
        ParticipanteBatalhaResponseDTO participanteBatalhaResponseDTO = participanteBatMapper.toDto(salvo);

        return participanteBatalhaResponseDTO;
    }

    @Override
    public void deletarParticipanteBatalha(Long id) {
        participanteBatalhaValidator.verificarParticipanteBatalhaDTO(id);
        participanteBatalhaRepository.deleteById(id);
    }

    @Override
    public List<PersonagemResponseDTO> getPersonagemByBatalhaId(Long id) {
        BatalhaResponseDTO batalha = batalhaService.getBatalhaById(id);
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
    public ParticipanteBatalhaResponseDTO getParticipanteBatalhaById(Long id) {
        ParticipanteBatalha participanteBatalha = participanteBatalhaValidator.verificarParticipanteBatalhaDTO(id);
        return participanteBatMapper.toDto(participanteBatalha);
    }

    @Override
    public ParticipanteBatalhaResponseDTO atualizarParticipanteBatalha(Long id,
            ParticipanteBatalhaRequestDTO participanteBDTO) {
        ParticipanteBatalhaResponseDTO participanteBatalhaExistente = this.getParticipanteBatalhaById(id);
        ParticipanteBatalha participanteBatalha = participanteBatMapper.toEntity(participanteBatalhaExistente);

        participanteBatalha.setPlayerOrder(participanteBDTO.getPlayerOrder());
        participanteBatalha.setVencedor(participanteBDTO.getVencedor());

        ParticipanteBatalha participanteAtualizado = participanteBatalhaRepository.save(participanteBatalha);

        return participanteBatMapper.toDto(participanteAtualizado);
    }

    @Override
    public Page<ParticipanteBatalhaResponseDTO> listarTodosParticipantes(String direction, String sortBy, int page,
            int size) {
        Sort sort = direction.equalsIgnoreCase("desc") ? Sort.by(sortBy).descending() : Sort.by(sortBy).ascending();
        Pageable pageable = PageRequest.of(page, size, sort);

        Page<ParticipanteBatalha> participantesBatalhas = participanteBatalhaRepository.findAll(pageable);
        return participantesBatalhas.map(participanteBatMapper::toDto);
    }

}
