package com.crud.demo.repositories;

import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;

import com.crud.demo.models.Jutsu;

public interface JutsuRepository extends JpaRepository<Jutsu, Long> {
    Optional<Jutsu> findByTipo(String tipo);

}
