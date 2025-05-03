package com.crud.demo.services;

import java.util.List;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Service;

import com.crud.demo.Exceptions.batalhaException.BatalhaFinalizadaException;
import com.crud.demo.Exceptions.batalhaException.BatalhaNaoEncontradaException;
import com.crud.demo.models.Batalha;
import com.crud.demo.models.DTO.batalha.BatalhaRequestDTO;
import com.crud.demo.models.DTO.batalha.BatalhaResponseDTO;
import com.crud.demo.models.ParticipanteBatalha;
import com.crud.demo.models.mappers.BatalhaMapper;
import com.crud.demo.repositories.BatalhaRepository;
import com.crud.demo.services.contratos.BatalhaService;

import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class BatalhaServiceImpl implements BatalhaService {

    private final BatalhaRepository batalhaRepository;
    private final BatalhaMapper batalhaMapper;

    @Override
    public BatalhaResponseDTO criarBatalha(BatalhaRequestDTO request) {
        Batalha batalhaEntity = batalhaMapper.toEntity(request);
        Batalha salvo = batalhaRepository.save(batalhaEntity);
        BatalhaResponseDTO batalhaResponseDTO = batalhaMapper.toDto(salvo);
        return batalhaResponseDTO;
    }

    @Override
    public void deletarBatalha(Long id) {
        getBatalhaById(id);
        batalhaRepository.deleteById(id);
    }

    @Override
    public BatalhaResponseDTO getBatalhaById(Long id) {
        Batalha batalhaEncontrada = batalhaRepository.findById(id).orElseThrow(BatalhaNaoEncontradaException::new);
        return batalhaMapper.toDto(batalhaEncontrada);
    }

    @Override
    public BatalhaResponseDTO atualizarBatalha(Long id, BatalhaResponseDTO batalhaDTO) {
        BatalhaResponseDTO batalhaExistente = this.getBatalhaById(id);
        Batalha batalhaExistenteEntity = batalhaMapper.toEntity(batalhaExistente);
        List<ParticipanteBatalha> participantes = batalhaDTO.getParticipantesBatalha();

        batalhaExistenteEntity.setCriadoEm(batalhaDTO.getCriadoEm());
        batalhaExistenteEntity.setFinalizadoEm(batalhaDTO.getFinalizadoEm());
        batalhaExistenteEntity.setStatus(batalhaDTO.getStatus());
        batalhaExistenteEntity.setParticipantes(participantes);

        Batalha batalhaAtualizada = batalhaRepository.save(batalhaExistenteEntity);
        return batalhaMapper.toDto(batalhaAtualizada);
    }

    @Override
    public Page<BatalhaResponseDTO> exibirTodasAsBatalhas(int page, int size, String sortBy, String direction) {
        Sort sort = direction.equalsIgnoreCase("desc") ? Sort.by(sortBy).descending()
                : Sort.by(sortBy).ascending();
        Pageable pageable = PageRequest.of(page, size, sort);

        Page<Batalha> batalhas = batalhaRepository.findAll(pageable);
        return batalhas.map(batalhaMapper::toDto);
    }

    @Override
    public void verificarBatalhaEncerrada(Long id) throws BatalhaFinalizadaException {
        BatalhaResponseDTO batalha = this.getBatalhaById(id);
        if (batalha.getFinalizadoEm() != null) {
            throw new BatalhaFinalizadaException();
        }

    }
}
