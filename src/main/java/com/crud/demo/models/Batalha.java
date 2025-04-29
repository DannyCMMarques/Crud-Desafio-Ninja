package com.crud.demo.models;

import java.time.LocalDateTime;
import java.util.ArrayList;
import java.util.List;

import com.crud.demo.models.enuns.StatusEnum;

import jakarta.persistence.CascadeType;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.OneToMany;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.NoArgsConstructor;

@Entity
@Data
@AllArgsConstructor
@NoArgsConstructor
@Builder
public class Batalha {

    @Id
    @GeneratedValue(strategy = GenerationType.IDENTITY)
    private Long id;

    @Column(name = "criado_em", nullable = false)
    private LocalDateTime criadoEm = LocalDateTime.now();

    @Column(name = "finalizado_em")
    private LocalDateTime finalizadoEm;

    @Enumerated(EnumType.STRING)
    @Column(columnDefinition = "status_enum", nullable = false)
    private StatusEnum status = StatusEnum.NAO_INICIADA;

    @OneToMany(mappedBy = "batalha", cascade = CascadeType.ALL, orphanRemoval = true, fetch = FetchType.LAZY)
    @Builder.Default
    private List<ParticipanteBatalha> participantes = new ArrayList<>();

}
