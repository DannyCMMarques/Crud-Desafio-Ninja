package com.crud.demo.models;

import java.util.List;

import com.crud.demo.utils.converters.StringListConverter;

import jakarta.persistence.Column;
import jakarta.persistence.Convert;
import jakarta.persistence.DiscriminatorColumn;
import jakarta.persistence.DiscriminatorType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.Id;
import jakarta.persistence.Inheritance;
import jakarta.persistence.InheritanceType;
import jakarta.persistence.Table;
import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@Table(name = "personagens")
@NoArgsConstructor(access = AccessLevel.PUBLIC)
@AllArgsConstructor
@Builder
@Entity
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "tipo", discriminatorType = DiscriminatorType.STRING)
public class Personagem {

    @Id
    @GeneratedValue(strategy = jakarta.persistence.GenerationType.IDENTITY)
    private Long id;

    private String nome;

    private long idade;

    private String aldeia;

    private long chakra;

    @Column(name = "jutsus", columnDefinition = "text[]")
    @Convert(converter = StringListConverter.class)
    private List<String> jutsus;

    public void adicionarJutsu(String jutsu) {
        jutsus.add(jutsu);
    }

    public void aumentarChakra(long quantidade) {
        this.chakra += quantidade;
    }

    public String exibirInformacoes() {
        return String.format(
                "Nome: %s\nIdade: %d\nAldeia: %s\nChakra: %d\nJutsus: %s",
                nome, idade, aldeia, chakra, jutsus);
    }

}