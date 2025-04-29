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
import com.crud.demo.models.DTO.BatalhaDTO;
import com.crud.demo.models.DTO.BatalhaRequestDTO;
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
    public BatalhaDTO criarBatalha(BatalhaRequestDTO request) {
        BatalhaDTO dto = batalhaMapper.fromRequest(request, List.of());
        Batalha batalhaEntity = batalhaMapper.toEntity(dto);

        Batalha salvo = batalhaRepository.save(batalhaEntity);
        return batalhaMapper.toDto(salvo);
    }

    @Override
    public void deletarBatalha(Long id) {
        getBatalhaById(id);
        batalhaRepository.deleteById(id);
    }

    @Override
    public BatalhaDTO getBatalhaById(Long id) {
        Batalha batalhaEncontrada = batalhaRepository.findById(id).orElseThrow(BatalhaNaoEncontradaException::new);
        return batalhaMapper.toDto(batalhaEncontrada);
    }

    @Override
    public BatalhaDTO atualizarBatalha(Long id, BatalhaDTO batalhaDTO) {
        BatalhaDTO batalhaExistente = this.getBatalhaById(id);
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
    public Page<BatalhaDTO> exibirTodasAsBatalhas(int page, int size, String sortBy, String direction) {
        Sort sort = direction.equalsIgnoreCase("desc") ? Sort.by(sortBy).descending()
                : Sort.by(sortBy).ascending();
        Pageable pageable = PageRequest.of(page, size, sort);

        Page<Batalha> batalhas = batalhaRepository.findAll(pageable);
        return batalhas.map(batalhaMapper::toDto);
    }

    @Override
    public void verificarBatalhaEncerrada(Long id) throws BatalhaFinalizadaException {
        BatalhaDTO batalha = this.getBatalhaById(id);
        if (batalha.getFinalizadoEm() != null) {
            throw new BatalhaFinalizadaException();
        }

    }
}
