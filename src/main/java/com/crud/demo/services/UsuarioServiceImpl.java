package com.crud.demo.services;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.domain.Pageable;
import org.springframework.data.domain.Sort;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import com.crud.demo.models.DTO.usuario.UsuarioRequestDTO;
import com.crud.demo.models.DTO.usuario.UsuarioResponseDTO;
import com.crud.demo.models.Usuario;
import com.crud.demo.models.mappers.UsuarioMapper;
import com.crud.demo.repositories.UsuarioRepository;
import com.crud.demo.services.contratos.UsuarioService;
import com.crud.demo.validators.UsuarioValidator;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class UsuarioServiceImpl implements UsuarioService {

    private final UsuarioRepository usuarioRepository;
    private final UsuarioValidator usuarioValidator;
    private final PasswordEncoder passwordEncoder;
    private final UsuarioMapper usuarioMapper;

    @Override
    public UsuarioResponseDTO cadastrarUsuario(UsuarioRequestDTO usuarioDTO) {
        usuarioValidator.validarCadastro(usuarioDTO.getEmail());

        Usuario usuarioEntity = usuarioMapper.toEntity(usuarioDTO);

        usuarioEntity.setSenha(passwordEncoder.encode(usuarioEntity.getSenha()));

        Usuario usuarioSalvo = usuarioRepository.save(usuarioEntity);
        UsuarioResponseDTO usuarioResponseDTO = usuarioMapper.toDto(usuarioSalvo);
        return usuarioResponseDTO;

    }

    @Override
    public UsuarioResponseDTO buscarUsuarioPorId(Long id) {
        Usuario usuario = usuarioValidator.validarExistencia(id);
        UsuarioResponseDTO usuarioResponseDTO = usuarioMapper.toDto(usuario);
        return usuarioResponseDTO;}

    @Override
    public void deleteById(Long id) {
        usuarioValidator.validarExistencia(id);
        usuarioRepository.deleteById(id);
    }

    @Override
    public UsuarioResponseDTO atualizarUsuario(Long id, UsuarioRequestDTO usuarioDTO) {
            usuarioValidator.validarExistencia(id);
          Usuario usuarioExistente = usuarioMapper.toEntity(usuarioDTO);
        usuarioExistente.setNome(usuarioDTO.getNome());
        usuarioExistente.setEmail(usuarioDTO.getEmail());
        usuarioExistente.setSenha(passwordEncoder.encode(usuarioDTO.getSenha()));
        Usuario usuarioAtualizado = usuarioRepository.save(usuarioExistente);
       UsuarioResponseDTO usuarioResponseAtualizadoDTO = usuarioMapper.toDto(usuarioAtualizado);
        return usuarioResponseAtualizadoDTO;

    }

    @Override
    public Page<UsuarioResponseDTO> exibirTodosUsuarios(String direction, String sortBy, int page, int size) {
         Sort sort = direction.equalsIgnoreCase("desc") ? Sort.by(sortBy).descending()
                                : Sort.by(sortBy).ascending();
                Pageable pageable = PageRequest.of(page, size, sort);

        Page<Usuario> usuarios = usuarioRepository.findAll(pageable);
        Page<UsuarioResponseDTO> usuariosDTO = usuarios.map(usuarioMapper::toDto);
        return usuariosDTO;
    }

    @Override
    public UsuarioResponseDTO buscarUsuarioPorNome(String nome) {
        Usuario usuario = usuarioRepository.findByNome(nome)
                .orElseThrow(() -> new RuntimeException("Usuario não encontrado"));
        return usuarioMapper.toDto(usuario);
    }
}