package com.crud.demo.validators;

import java.util.Optional;

import org.springframework.stereotype.Component;

import com.crud.demo.Exceptions.usuarioException.EmailJaCadastradoException;
import com.crud.demo.Exceptions.usuarioException.UsuarioNaoEncontradoException;
import com.crud.demo.models.Usuario;
import com.crud.demo.repositories.UsuarioRepository;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class UsuarioValidator {

    private final UsuarioRepository usuarioRepository;

    public void validarCadastro(String email) {

        Optional<Usuario> emailCadastrado = usuarioRepository.findByEmail(email);
        if (emailCadastrado.isPresent()) {
            throw new EmailJaCadastradoException(emailCadastrado.get().getEmail());
        }

    }

    public Usuario validarExistencia(Long id) {
        return usuarioRepository.findById(id)
                .orElseThrow(UsuarioNaoEncontradoException::new);
    }

    public Usuario validarExistencia(String nome) {
        return usuarioRepository.findByNome(nome)
                .orElseThrow(UsuarioNaoEncontradoException::new);
    }

    public Usuario validarExistenciaLogin(String email) {
        return usuarioRepository.findByEmail(email)
                .orElseThrow(UsuarioNaoEncontradoException::new);
    }
}
