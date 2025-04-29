package com.crud.demo.repositories;

import org.springframework.data.jpa.repository.JpaRepository;

import com.crud.demo.models.ParticipanteBatalha;

import java.util.List;
import java.util.Optional;

import com.crud.demo.models.Batalha;

public interface ParticipanteBatalhaRepository extends JpaRepository<ParticipanteBatalha, Long> {

    Optional<List<ParticipanteBatalha>> findByBatalha(Batalha batalha);
}
