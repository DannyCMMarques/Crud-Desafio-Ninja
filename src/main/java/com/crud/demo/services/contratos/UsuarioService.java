package com.crud.demo.services.contratos;

import org.springframework.data.domain.Page;

import com.crud.demo.models.DTO.usuario.UsuarioRequestDTO;
import com.crud.demo.models.DTO.usuario.UsuarioResponseDTO;

public interface UsuarioService {

    UsuarioResponseDTO cadastrarUsuario(UsuarioRequestDTO usuarioDTO);

    UsuarioResponseDTO buscarUsuarioPorId(Long id);

    void deleteById(Long id);

    UsuarioResponseDTO atualizarUsuario(Long id, UsuarioRequestDTO usuarioDTO);

    Page<UsuarioResponseDTO> exibirTodosUsuarios(String direction, String sortBy, int page, int size);

    UsuarioResponseDTO buscarUsuarioPorNome(String nome);

}