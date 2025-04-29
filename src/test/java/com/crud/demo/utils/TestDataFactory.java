package com.crud.demo.utils;

import com.crud.demo.models.Jutsu;
import com.crud.demo.models.Personagem;
import com.crud.demo.models.DTO.PersonagemDTO;

public class TestDataFactory {

    public static final String NOME_PADRAO = "Naruto Uzumaki";
    public static final String ALDEIA = "Konoha";
    public static final Long CHAKRA = 1000L;
    public static final Long IDADE = 30L;
    public static final String NINJUTSU = "Ninjutsu";
    public static final String TAIJUTSU = "Taijutsu";
    public static final String GENJUTSU = "Genjutsu";

    public static PersonagemDTO criarPersonagemDTOComNinjutsu() {
        PersonagemDTO dto = new PersonagemDTO();
        dto.setNome(NOME_PADRAO);
        dto.setAldeia(ALDEIA);
        dto.setIdade(IDADE);
        return dto;
    }

    public static PersonagemDTO criarPersonagemDTOComTaijutsu() {
        PersonagemDTO dto = new PersonagemDTO();
        dto.setNome("Rock Lee");
        dto.setAldeia(ALDEIA);
        dto.setIdade(25L);
        return dto;
    }

    public static PersonagemDTO criarPersonagemDTOComGenjutsu() {
        PersonagemDTO dto = new PersonagemDTO();
        dto.setNome("Itachi Uchiha");
        dto.setAldeia(ALDEIA);
        dto.setIdade(28L);
        return dto;
    }

    public static Jutsu criarJutsuNinjutsu() {
        return Jutsu.builder().id(1L).tipo(NINJUTSU).build();
    }

    public static Jutsu criarJutsuTaijutsu() {
        return Jutsu.builder().id(2L).tipo(TAIJUTSU).build();
    }

    public static Jutsu criarJutsuGenjutsu() {
        return Jutsu.builder().id(3L).tipo(GENJUTSU).build();
    }

    public static Personagem criarPersonagemEntityComNinjutsu() {
        Personagem personagem = new Personagem();
        personagem.setNome(NOME_PADRAO);
        personagem.setAldeia(ALDEIA);
        personagem.setIdade(IDADE);
        return personagem;
    }

    public static Personagem criarPersonagemEntityComTaijutsu() {
        Personagem personagem = new Personagem();
        personagem.setNome("Rock Lee");
        personagem.setAldeia(ALDEIA);
        personagem.setIdade(25L);
        return personagem;
    }

    public static Personagem criarPersonagemEntityComGenjutsu() {
        Personagem personagem = new Personagem();
        personagem.setNome("Itachi Uchiha");
        personagem.setAldeia(ALDEIA);
        personagem.setIdade(28L);
        return personagem;
    }
}
