package com.crud.demo.factories;

import java.util.HashMap;
import java.util.Map;
import java.util.function.Supplier;

import org.springframework.stereotype.Component;

import com.crud.demo.factories.contrato.PersonagemFactory;
import com.crud.demo.models.Personagem;
import com.crud.demo.models.DTO.personagem.PersonagemRequestDTO;
import com.crud.demo.models.enuns.CategoriaEspecialidadeEnum;
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
    public Personagem construirTipoPersonagem(PersonagemRequestDTO dto) {
        Personagem personagemEntity = personagemMapper.toEntity(dto);

        CategoriaEspecialidadeEnum especialidade = personagemEntity.getEspecialidade();
        Personagem subClasse = mapaDeTipos.get(especialidade.name()).get();
        this.preencherDados(subClasse, personagemEntity);
        return subClasse;

    }

    public void preencherDados(Personagem destino, Personagem origem) {
        destino.setId(origem.getId());
        destino.setNome(origem.getNome());
        destino.setIdade(origem.getIdade());
        destino.setAldeia(origem.getAldeia());
        destino.setEspecialidade(origem.getEspecialidade());
        destino.setJutsus(origem.getJutsus());
        destino.setDataCriacao(origem.getDataCriacao());
    }
}
