package com.crud.demo.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.JpaSpecificationExecutor;
import org.springframework.stereotype.Repository;

import com.crud.demo.models.Personagem;

@Repository
public interface PersonagemRepository extends JpaRepository<Personagem, Long>, JpaSpecificationExecutor<Personagem> {

    Optional<Personagem> findByNome(String nome);
}
