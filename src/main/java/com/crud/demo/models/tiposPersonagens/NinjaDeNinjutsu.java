package com.crud.demo.models.tiposPersonagens;




import com.crud.demo.models.Personagem;
import com.crud.demo.models.contratos.Ninja;

import jakarta.persistence.DiscriminatorValue;
import jakarta.persistence.Entity;

@Entity
@DiscriminatorValue("NINJUTSU")
public class NinjaDeNinjutsu extends Personagem implements Ninja {

    @Override
    public String usarJutsu(Personagem personagem) {
        return personagem.getNome() + " está usando um jutsu de Ninjutsu!";
    }

    @Override
    public String desviar(Personagem personagem) {
        return personagem.getNome() + " está desviando de um ataque utilizando Ninjutsu!";
    }

}
