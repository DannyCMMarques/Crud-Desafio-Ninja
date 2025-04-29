package com.crud.demo.repositories;

import java.util.List;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import com.crud.demo.models.Batalha;
import com.crud.demo.models.Personagem;

@Repository
public interface BatalhaRepository extends JpaRepository<Batalha, Long> {

    @Query("""
        SELECT DISTINCT p
        FROM ParticipanteBatalha pb
        JOIN pb.personagem p
        JOIN FETCH p.jutsus
        WHERE pb.batalha.id = :batalhaID
      """)
    List<Personagem> findPersonagensByBattleId(@Param("batalhaID") Long batalhaID);

}
