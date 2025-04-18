package com.crud.demo.models;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorColumn;
import jakarta.persistence.DiscriminatorType;
import jakarta.persistence.Entity;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Inheritance;
import jakarta.persistence.InheritanceType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
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
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nome;

    private long idade;

    private String aldeia;

    private long chakra;

    @ManyToMany
    @JoinTable(name = "personagem_jutsus", joinColumns = @JoinColumn(name = "personagem_id"), inverseJoinColumns = @JoinColumn(name = "jutsu_id"))
    @Builder.Default
    private List<Jutsu> jutsus = new ArrayList<>();

    @Column(name = "data_criacao")
    @Builder.Default
    private LocalDateTime dataCriacao = LocalDateTime.now();

    public void adicionarJutsu(Jutsu jutsu) {
        jutsus.add(jutsu);
    }

    public void aumentarChakra(long quantidade) {
        this.chakra += quantidade;
    }

    public String exibirInformacoes() {
        return String.format(
                "Nome: %s\nIdade: %d\nAldeia: %s\nChakra: %d\nJutsus: %s",
                nome, idade, aldeia, chakra,
                jutsus.stream().map(Jutsu::getTipo).toList());
    }
}
