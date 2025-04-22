package com.crud.demo.specifications;

import static org.junit.jupiter.api.Assertions.assertNull;
import static org.junit.jupiter.api.Assertions.assertSame;
import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.DisplayName;
import org.junit.jupiter.api.Test;
import org.junit.jupiter.api.extension.ExtendWith;
import org.mockito.Mock;
import static org.mockito.Mockito.verify;
import static org.mockito.Mockito.when;
import org.mockito.junit.jupiter.MockitoExtension;
import org.mockito.junit.jupiter.MockitoSettings;
import org.mockito.quality.Strictness;

import com.crud.demo.models.Personagem;

import jakarta.persistence.criteria.CriteriaBuilder;
import jakarta.persistence.criteria.CriteriaQuery;
import jakarta.persistence.criteria.Join;
import jakarta.persistence.criteria.Path;
import jakarta.persistence.criteria.Predicate;
import jakarta.persistence.criteria.Root;

@ExtendWith(MockitoExtension.class)
@MockitoSettings(strictness = Strictness.LENIENT)
class PersonagemSpecificationsTest {

    @Mock
    private Root<Personagem> root;

    @Mock
    private CriteriaQuery<?> query;

    @Mock
    private CriteriaBuilder builder;

    @Mock
    private Path<String> stringPath;

    @Mock
    private Path<Long> longPath;

    @Mock
    @SuppressWarnings("rawtypes")
    private Join personagemJoin;

    @Mock
    private Predicate predicate;

    @BeforeEach
    void setUp() {
        when(root.<String>get("nome")).thenReturn(stringPath);
        when(root.<String>get("aldeia")).thenReturn(stringPath);
        when(root.<Long>get("idade")).thenReturn(longPath);
        when(root.<Long>get("chakra")).thenReturn(longPath);
        when(personagemJoin.<String>get("tipo")).thenReturn(stringPath);
    }

    @Test
        @DisplayName("Deve retornar null para nome quando nulo ou em branco")
    void deveRetornarNullQuandoNomeNuloOuEmBranco() {
        assertNull(PersonagemSpecifications.comNomeContendo(null).toPredicate(root, query, builder));
        assertNull(PersonagemSpecifications.comNomeContendo("  ").toPredicate(root, query, builder));
    }

    @Test
    @DisplayName("Deve criar predicate LIKE para nome válido")
    void deveCriarPredicateLikeParaNomeValido() {
        String nome = "Naruto";
        when(builder.lower(stringPath)).thenReturn(stringPath);
        String expectedPattern = "%" + nome.toLowerCase() + "%";
        when(builder.like(stringPath, expectedPattern)).thenReturn(predicate);

        Predicate result = PersonagemSpecifications.comNomeContendo(nome).toPredicate(root, query, builder);

        verify(root).get("nome");
        verify(builder).lower(stringPath);
        verify(builder).like(stringPath, expectedPattern);
        assertSame(predicate, result);
    }

    @Test
    @DisplayName("Deve retornar null para idade quando for null")
    void deveRetornarNullQuandoIdadeNula() {
        assertNull(PersonagemSpecifications.comIdadeIgualA(null).toPredicate(root, query, builder));
    }

    @Test
    @DisplayName("Deve criar predicate EQUAL para idade válida")
    void deveCriarPredicateEqualParaIdadeValida() {
        Long idade = 18L;
        when(builder.equal(longPath, idade)).thenReturn(predicate);

        Predicate result = PersonagemSpecifications.comIdadeIgualA(idade).toPredicate(root, query, builder);

        verify(root).get("idade");
        verify(builder).equal(longPath, idade);
        assertSame(predicate, result);
    }

    @Test
    @DisplayName("Deve retornar null para aldeia quando nulo ou em branco")
    void deveRetornarNullQuandoAldeiaNulaOuEmBranco() {
        assertNull(PersonagemSpecifications.comAldeiaContendo(null).toPredicate(root, query, builder));
        assertNull(PersonagemSpecifications.comAldeiaContendo("  ").toPredicate(root, query, builder));
    }

    @Test
    @DisplayName("Deve criar predicate LIKE para aldeia válida")
    void deveCriarPredicateLikeParaAldeiaValida() {
        String aldeia = "Konoha";
        when(builder.lower(stringPath)).thenReturn(stringPath);
        String expectedPattern = "%" + aldeia.toLowerCase() + "%";
        when(builder.like(stringPath, expectedPattern)).thenReturn(predicate);

        Predicate result = PersonagemSpecifications.comAldeiaContendo(aldeia).toPredicate(root, query, builder);

        verify(root).get("aldeia");
        verify(builder).lower(stringPath);
        verify(builder).like(stringPath, expectedPattern);
        assertSame(predicate, result);
    }

    @Test
    @DisplayName("Deve retornar null para chakra quando for null")
    void deveRetornarNullQuandoChakraNulo() {
        assertNull(PersonagemSpecifications.comChakraIgualA(null).toPredicate(root, query, builder));
    }

    @Test
    @DisplayName("Deve criar predicate EQUAL para chakra válido")
    void deveCriarPredicateEqualParaChakraValido() {
        Long chakra = 100L;
        when(builder.equal(longPath, chakra)).thenReturn(predicate);

        Predicate result = PersonagemSpecifications.comChakraIgualA(chakra).toPredicate(root, query, builder);

        verify(root).get("chakra");
        verify(builder).equal(longPath, chakra);
        assertSame(predicate, result);
    }

    @Test
    @DisplayName("Deve retornar null para jutsus quando nulo ou em branco")
    void deveRetornarNullQuandoJutsusNuloOuEmBranco() {
        assertNull(PersonagemSpecifications.comJutsusContendo(null).toPredicate(root, query, builder));
        assertNull(PersonagemSpecifications.comJutsusContendo(" ").toPredicate(root, query, builder));
    }

    @Test
    @DisplayName("Deve criar predicate LIKE para jutsus válidos")
    void deveCriarPredicateLikeParaJutsusValidos() {
        String tipo = "Genjutsu";
        when(root.join("jutsus")).thenReturn(personagemJoin);
        when(builder.lower(stringPath)).thenReturn(stringPath);
        String expectedPattern = "%" + tipo.toLowerCase() + "%";
        when(builder.like(stringPath, expectedPattern)).thenReturn(predicate);

        Predicate result = PersonagemSpecifications.comJutsusContendo(tipo).toPredicate(root, query, builder);

        verify(query).distinct(true);
        verify(root).join("jutsus");
        verify(personagemJoin).get("tipo");
        verify(builder).lower(stringPath);
        verify(builder).like(stringPath, expectedPattern);
        assertSame(predicate, result);
    }

    @Test
    @DisplayName("Deve retornar null para faixa de idade quando ambos min e max forem nulos")
    void deveRetornarNullQuandoFaixaDeIdadeAmbosNulos() {
        assertNull(PersonagemSpecifications.comIdadeEntre(null, null).toPredicate(root, query, builder));
    }

    @Test
    @DisplayName("Deve usar between para faixa de idade completa")
    void deveUsarBetweenParaFaixaDeIdadeCompleta() {
        Long min = 10L, max = 20L;
        when(builder.between(longPath, min, max)).thenReturn(predicate);

        Predicate result = PersonagemSpecifications.comIdadeEntre(min, max).toPredicate(root, query, builder);

        verify(root).get("idade");
        verify(builder).between(longPath, min, max);
        assertSame(predicate, result);
    }

    @Test
    @DisplayName("Deve usar greaterThanOrEqualTo para idade mínima apenas")
    void deveUsarGreaterThanOrEqualToParaIdadeMinimaApenas() {
        Long min = 15L;
        when(builder.greaterThanOrEqualTo(longPath, min)).thenReturn(predicate);

        Predicate result = PersonagemSpecifications.comIdadeEntre(min, null).toPredicate(root, query, builder);

        verify(root).get("idade");
        verify(builder).greaterThanOrEqualTo(longPath, min);
        assertSame(predicate, result);
    }

    @Test
    @DisplayName("Deve usar lessThanOrEqualTo para idade máxima apenas")
    void deveUsarLessThanOrEqualToParaIdadeMaximaApenas() {
        Long max = 25L;
        when(builder.lessThanOrEqualTo(longPath, max)).thenReturn(predicate);

        Predicate result = PersonagemSpecifications.comIdadeEntre(null, max).toPredicate(root, query, builder);

        verify(root).get("idade");
        verify(builder).lessThanOrEqualTo(longPath, max);
        assertSame(predicate, result);
    }
}