package com.crud.demo.models.mappers.helpers;

import org.mapstruct.Named;
import org.springframework.stereotype.Component;

import com.crud.demo.models.Batalha;
import com.crud.demo.models.DTO.batalha.BatalhaResponseDTO;
import com.crud.demo.models.DTO.personagem.PersonagemResponseDTO;
import com.crud.demo.models.DTO.usuario.UsuarioResponseDTO;
import com.crud.demo.models.Personagem;
import com.crud.demo.models.Usuario;
import com.crud.demo.models.mappers.BatalhaMapper;
import com.crud.demo.models.mappers.PersonagemMapper;
import com.crud.demo.models.mappers.UsuarioMapper;
import com.crud.demo.services.contratos.BatalhaService;
import com.crud.demo.services.contratos.PersonagemService;
import com.crud.demo.services.contratos.UsuarioService;

import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class ParticipanteBatalhaResolver {

    private final BatalhaService      batalhaService;
    private final PersonagemService  personagemService;
    private final UsuarioService     usuarioService;

    private final BatalhaMapper      batalhaMapper;
    private final PersonagemMapper   personagemMapper;
    private final UsuarioMapper      usuarioMapper;

    @Named("mapBatalhaById")
    public Batalha mapBatalhaById(Long id) {
        BatalhaResponseDTO dto = batalhaService.getBatalhaById(id);
        return batalhaMapper.toEntity(dto);
    }

    @Named("mapPersonagemByNome")
    public Personagem mapPersonagemByNome(String nome) {
        PersonagemResponseDTO dto = personagemService.getPersonagemByNome(nome);
        return personagemMapper.toEntity(dto);
    }

    @Named("mapUsuarioByNome")
    public Usuario mapUsuarioByNome(String nomeUsuario) {
        UsuarioResponseDTO dto = usuarioService.buscarUsuarioPorNome(nomeUsuario);
        return usuarioMapper.toEntity(dto);
    }
}
