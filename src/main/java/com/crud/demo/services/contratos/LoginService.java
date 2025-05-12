package com.crud.demo.services.contratos;

import com.crud.demo.models.DTO.login.LoginDTO;
import com.crud.demo.models.DTO.login.LoginResponseDTO;

public interface LoginService {

    String autentificar(LoginDTO loginDTO);

    LoginResponseDTO gerarLoginResponse(String token);

}