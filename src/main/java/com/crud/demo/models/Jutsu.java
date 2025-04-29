package com.crud.demo.models;

import com.crud.demo.models.enuns.CategoriaEspecialidadeEnum;
import com.fasterxml.jackson.annotation.JsonIgnoreProperties;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;
@JsonIgnoreProperties({ "hibernateLazyInitializer", "handler" })
@Entity
@Table(name = "jutsus")
@Data
@NoArgsConstructor
@AllArgsConstructor
@Builder
public class Jutsu {
    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(nullable = false, unique = true)
    private String tipo;
    
    private Integer dano; 
    private Integer consumo_de_chakra;
    @Enumerated(EnumType.STRING)
    private CategoriaEspecialidadeEnum categoria;
}
