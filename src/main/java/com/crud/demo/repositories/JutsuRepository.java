package com.crud.demo.repositories;

import com.crud.demo.models.Jutsu;
import com.crud.demo.models.enuns.CategoriaEspecialidadeEnum;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.function.Function;
import java.util.stream.Collectors;

public interface JutsuRepository extends JpaRepository<Jutsu, Long> {

    Optional<Jutsu> findByTipo(String tipo);

    List<Jutsu> findByCategoria(CategoriaEspecialidadeEnum categoria);

    default Map<String, Jutsu> findMapByCategoria(CategoriaEspecialidadeEnum categoria) {
        return findByCategoria(categoria).stream()
                .collect(Collectors.toMap(
                        Jutsu::getTipo,
                        Function.identity()
                ));
    }
}
