package com.crud.demo.factories;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

import org.springframework.stereotype.Component;

import com.crud.demo.factories.contrato.PersonagemFactory;
import com.crud.demo.models.Personagem;
import com.crud.demo.models.DTO.PersonagemDTO;
import com.crud.demo.models.mappers.PersonagemMapper;
import com.crud.demo.models.tiposPersonagens.NinjaDeGenjutsu;
import com.crud.demo.models.tiposPersonagens.NinjaDeNinjutsu;
import com.crud.demo.models.tiposPersonagens.NinjaDeTaijutsu;

import lombok.AllArgsConstructor;

@Component
@AllArgsConstructor
public class PersonagemFactoryImpl implements PersonagemFactory {

    private final PersonagemMapper personagemMapper;

    private final Map<String, Supplier<Personagem>> mapaDeTipos = new HashMap<>();

    {
        mapaDeTipos.put("TAIJUTSU", NinjaDeTaijutsu::new);
        mapaDeTipos.put("NINJUTSU", NinjaDeNinjutsu::new);
        mapaDeTipos.put("GENJUTSU", NinjaDeGenjutsu::new);
    }

    @Override
    public Personagem construirTipoPersonagem(PersonagemDTO dto) {
        Personagem personagemEntity = personagemMapper.toEntity(dto);

        return personagemEntity.getJutsus().stream()
                .map(j -> j.getTipo().toUpperCase())
                .filter(mapaDeTipos::containsKey)
                .findFirst()
                .map(tipo -> {
                    Personagem subclasse = mapaDeTipos.get(tipo).get();
                    personagemMapper.preencherDados(subclasse, personagemEntity);
                    return subclasse;
                })
                .orElse(personagemEntity);
    }
}
