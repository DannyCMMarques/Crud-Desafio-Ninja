package com.crud.demo.utils;


import com.crud.demo.models.DTO.usuario.UsuarioRequestDTO;
import com.crud.demo.models.Usuario;

public class TestDataFactoryUsuario {

    public static final String NOME_PADRAO = "João da Silva";
    public static final String EMAIL_PADRAO = "joao@example.com";
    public static final String SENHA_PADRAO = "senha123";
    public static final Long ID_PADRAO = 1L;
public static final String TOKEN="eyJhbGciOiJIUzI1NiJ9.eyJyb2xlIjoiVVNFUiIsImNyZWF0ZWRfYXQiOjE3NDUyMTIxODY3NTYsInN1YiI6ImpvYW8uc2lsdmFAZXhhbXBsZS5jb20iLCJpYXQiOjE3NDUyMTIxODYsImV4cCI6MTc0NTI5ODU4Nn0.uX45hyt0F59eFol0CkPpcf9ZWqAnWpyHkZPqPTpo4CU";

    public static UsuarioRequestDTO criarUsuarioDTO() {
        UsuarioRequestDTO dto = new UsuarioRequestDTO(NOME_PADRAO, EMAIL_PADRAO, SENHA_PADRAO);
        return dto;
    }

    public static Usuario criarUsuarioEntity() {
        Usuario usuario = new Usuario();
        usuario.setId(ID_PADRAO);
        usuario.setNome(NOME_PADRAO);
        usuario.setEmail(EMAIL_PADRAO);
        usuario.setSenha(SENHA_PADRAO);
        return usuario;
    }
}
