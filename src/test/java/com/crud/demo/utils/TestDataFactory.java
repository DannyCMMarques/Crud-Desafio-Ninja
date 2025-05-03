package com.crud.demo.utils;

import com.crud.demo.models.DTO.personagem.PersonagemResponseDTO;
import com.crud.demo.models.Jutsu;
import com.crud.demo.models.Personagem;

public class TestDataFactory {

    public static final String NOME_PADRAO = "Naruto Uzumaki";
    public static final String ALDEIA = "Konoha";
    public static final Long CHAKRA = 1000L;
    public static final Long IDADE = 30L;
    public static final String NINJUTSU = "Ninjutsu";
    public static final String TAIJUTSU = "Taijutsu";
    public static final String GENJUTSU = "Genjutsu";

    public static PersonagemResponseDTO criarPersonagemResponseDTOComNinjutsu() {
        return PersonagemResponseDTO.builder()
            .nome(NOME_PADRAO)
            .aldeia(ALDEIA)
            .idade(IDADE)
            .build();
    }

    public static PersonagemResponseDTO criarPersonagemResponseDTOComTaijutsu() {
        return PersonagemResponseDTO.builder()
            .nome("Rock Lee")
            .aldeia(ALDEIA)
            .idade(25L)
            .build();
    }

    public static PersonagemResponseDTO criarPersonagemResponseDTOComGenjutsu() {
        PersonagemResponseDTO dto = PersonagemResponseDTO.builder()
            .nome("Itachi Uchiha")
            .aldeia(ALDEIA)
            .idade(28L)
            .build();
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
