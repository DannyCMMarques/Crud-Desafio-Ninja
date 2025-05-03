package com.crud.demo.services.contratos;

import org.springframework.data.domain.Page;

import com.crud.demo.models.DTO.batalha.BatalhaResponseDTO;
import com.crud.demo.models.DTO.batalha.BatalhaRequestDTO;

public interface BatalhaService {

    BatalhaResponseDTO criarBatalha(BatalhaRequestDTO dto);

    void deletarBatalha(Long id);

    BatalhaResponseDTO getBatalhaById(Long id);

    BatalhaResponseDTO atualizarBatalha(Long id, BatalhaResponseDTO batalhaDTO);

    Page<BatalhaResponseDTO> exibirTodasAsBatalhas(int page, int size, String sortBy, String direction);

    void verificarBatalhaEncerrada(Long id);

}
