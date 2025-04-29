package com.crud.demo.models;

import java.time.LocalDateTime;
import java.util.HashMap;
import java.util.Map;

import com.crud.demo.models.enuns.CategoriaEspecialidadeEnum;

import jakarta.persistence.Column;
import jakarta.persistence.DiscriminatorColumn;
import jakarta.persistence.DiscriminatorType;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Inheritance;
import jakarta.persistence.InheritanceType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.JoinTable;
import jakarta.persistence.ManyToMany;
import jakarta.persistence.MapKey;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Getter
@Setter
@Entity
@Table(name = "personagens")
@NoArgsConstructor
@AllArgsConstructor
@Builder
@Inheritance(strategy = InheritanceType.SINGLE_TABLE)
@DiscriminatorColumn(name = "dtype", discriminatorType = DiscriminatorType.STRING)
public class Personagem {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    private String nome;

    private Long idade;

    private String aldeia;

    @Builder.Default
    private Integer chakra = 100;

    @Builder.Default
    private Double vida = 5.00;

    @Enumerated(EnumType.STRING)
    @Column(nullable = false)
    private CategoriaEspecialidadeEnum especialidade;

    @ManyToMany
    @JoinTable(name = "personagem_jutsus", joinColumns = @JoinColumn(name = "personagem_id"), inverseJoinColumns = @JoinColumn(name = "jutsu_id"))
    @Builder.Default
    @MapKey(name = "tipo")
    private Map<String, Jutsu> jutsus = new HashMap<>();

    @Builder.Default
    private LocalDateTime dataCriacao = LocalDateTime.now();
}
