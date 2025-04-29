package com.crud.demo.services.contratos;

import org.springframework.data.domain.Page;

import com.crud.demo.models.DTO.BatalhaDTO;
import com.crud.demo.models.DTO.BatalhaRequestDTO;

public interface BatalhaService {

    BatalhaDTO criarBatalha(BatalhaRequestDTO dto);

    void deletarBatalha(Long id);

    BatalhaDTO getBatalhaById(Long id);

    BatalhaDTO atualizarBatalha(Long id, BatalhaDTO batalhaDTO);

    Page<BatalhaDTO> exibirTodasAsBatalhas(int page, int size, String sortBy, String direction);

    void verificarBatalhaEncerrada(Long id);

}
