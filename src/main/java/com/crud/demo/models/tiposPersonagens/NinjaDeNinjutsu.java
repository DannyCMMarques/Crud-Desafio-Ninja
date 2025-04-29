package com.crud.demo.models.tiposPersonagens;

import com.crud.demo.events.EstatisticaDoJogadorEvent;
import com.crud.demo.models.Personagem;
import com.crud.demo.models.DTO.JutsuDTO;
import com.crud.demo.models.contratos.Ninja;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;
import lombok.Data;
import lombok.EqualsAndHashCode;


@Data
@EqualsAndHashCode(callSuper = true)
@Entity 
@DiscriminatorValue("NINJUTSU")
public class NinjaDeNinjutsu extends Personagem implements Ninja {

    @Override
    public String usarJutsu(Personagem personagem, JutsuDTO jutsu) {
        return String.format(
                "Houve um ataque: %s,um ninja de Ninjutsu, usa '%s' e causa a perda de %d chakras no adversário",
                this.getNome(),
                jutsu.getTipo(),
                jutsu.getDano());
    }

    @Override
    public String desviar(EstatisticaDoJogadorEvent defensor) {
        return String.format(
                "Defesa: %s,um ninja de Ninjutsu, desviou do ataque ",
                this.getNome());
    }
}
