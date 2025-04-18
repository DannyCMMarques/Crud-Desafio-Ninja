package com.crud.demo.specifications;

import org.springframework.data.jpa.domain.Specification;

import com.crud.demo.models.Personagem;

import jakarta.persistence.criteria.Join;

public class PersonagemSpecifications {

        public static Specification<Personagem> comNomeContendo(String nome) {
                return (root, query, builder) -> nome == null || nome.isBlank() ? null
                                : builder.like(builder.lower(root.get("nome")), "%" + nome.toLowerCase() + "%");
        }

        public static Specification<Personagem> comIdadeIgualA(Long idade) {
                return (root, query, builder) -> idade == null ? null
                                : builder.equal(root.get("idade"), idade);
        }

        public static Specification<Personagem> comAldeiaContendo(String aldeia) {
                return (root, query, builder) -> aldeia == null || aldeia.isBlank() ? null
                                : builder.like(builder.lower(root.get("aldeia")), "%" + aldeia.toLowerCase() + "%");
        }

        public static Specification<Personagem> comChakraIgualA(Long chakra) {
                return (root, query, builder) -> chakra == null ? null
                                : builder.equal(root.get("chakra"), chakra);
        }

        public static Specification<Personagem> comJutsusContendo(String tipoJutsu) {
                return (root, query, builder) -> {
                        if (tipoJutsu == null || tipoJutsu.isBlank())
                                return null;

                        query.distinct(true);

                        Join<Object, Object> join = root.join("jutsus");
                        return builder.like(
                                        builder.lower(join.get("tipo")),
                                        "%" + tipoJutsu.toLowerCase() + "%");
                };
        }

        public static Specification<Personagem> comIdadeEntre(Long idadeMin, Long idadeMax) {
                return (root, query, builder) -> {
                        if (idadeMin == null && idadeMax == null)
                                return null;

                        if (idadeMin != null && idadeMax != null) {
                                return builder.between(root.get("idade"), idadeMin, idadeMax);
                        } else if (idadeMin != null) {
                                return builder.greaterThanOrEqualTo(root.get("idade"), idadeMin);
                        } else {
                                return builder.lessThanOrEqualTo(root.get("idade"), idadeMax);
                        }
                };
        }
}
