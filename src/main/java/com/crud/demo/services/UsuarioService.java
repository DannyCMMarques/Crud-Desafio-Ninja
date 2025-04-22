package com.crud.demo.services;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.crud.demo.models.DTO.RegistroUsuarioDTO;
import com.crud.demo.models.Usuario;
import com.crud.demo.models.mappers.UsuarioMapper;
import com.crud.demo.repositories.UsuarioRepository;
import com.crud.demo.validators.UsuarioValidator;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final UsuarioValidator usuarioValidator;
    private final PasswordEncoder passwordEncoder;
    private final UsuarioMapper usuarioMapper;

    public RegistroUsuarioDTO cadastrarUsuario(RegistroUsuarioDTO usuarioDTO) {
        usuarioValidator.validarCadastro(usuarioDTO.getEmail());

        Usuario usuarioEntity = usuarioMapper.toEntity(usuarioDTO);

        usuarioEntity.setSenha(passwordEncoder.encode(usuarioEntity.getSenha()));

        Usuario usuarioSalvo = usuarioRepository.save(usuarioEntity);
        return usuarioMapper.toDto(usuarioSalvo);

    }

    public RegistroUsuarioDTO buscarUsuarioPorId(Long id) {
        Usuario usuario = usuarioValidator.validarExistencia(id);
        return usuarioMapper.toDto(usuario);
    }

    public void deleteById(Long id) {
        usuarioValidator.validarExistencia(id);
        usuarioRepository.deleteById(id);
    }

    public RegistroUsuarioDTO atualizarUsuario(Long id, RegistroUsuarioDTO usuarioDTO) {
        Usuario usuarioExistente = usuarioValidator.validarExistencia(id);

        usuarioExistente.setNome(usuarioDTO.getNome());
        usuarioExistente.setEmail(usuarioDTO.getEmail());
        usuarioExistente.setSenha(passwordEncoder.encode(usuarioDTO.getSenha()));
        Usuario usuarioAtualizado = usuarioRepository.save(usuarioExistente);

        return usuarioMapper.toDto(usuarioAtualizado);

    }

    public Page<RegistroUsuarioDTO> exibirTodosUsuarios(Pageable pageable) {
        Page<Usuario> usuarios = usuarioRepository.findAll(pageable);

        return usuarios.map(usuarioMapper::toDto);
    }
}