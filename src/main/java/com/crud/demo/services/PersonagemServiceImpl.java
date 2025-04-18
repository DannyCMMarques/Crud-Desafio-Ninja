package com.crud.demo.services;

import java.util.Collections;
import java.util.List;
import java.util.Optional;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.crud.demo.factories.PersonagemFactoryImpl;
import com.crud.demo.models.Jutsu;
import com.crud.demo.models.Personagem;
import com.crud.demo.models.DTO.PersonagemDTO;
import com.crud.demo.models.mappers.JutsuMapper;
import com.crud.demo.models.mappers.PersonagemMapper;
import com.crud.demo.repositories.JutsuRepository;
import com.crud.demo.repositories.PersonagemRepository;
import com.crud.demo.services.contratos.PersonagemService;
import com.crud.demo.specifications.PersonagemSpecifications;
import com.crud.demo.utils.converters.SpecificationBuilder;
import com.crud.demo.validators.PersonagemValidator;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class PersonagemServiceImpl implements PersonagemService {

        private final PersonagemRepository personagemRepository;
        private final JutsuRepository jutsuRepository;
        private final PersonagemMapper personagemMapper;
        private final JutsuMapper jutsuMapper;
        private final PersonagemValidator personagemValidator;
        private final PersonagemFactoryImpl personagemFactoryImpl;

        @Override
        public PersonagemDTO criarPersonagem(PersonagemDTO dto) {
                personagemValidator.validarCadastro(dto.getNome());

                Personagem personagem = personagemMapper.toEntity(dto);

                List<Jutsu> jutsusTratados = Optional.ofNullable(personagem.getJutsus())
                                .orElse(Collections.emptyList())
                                .stream()
                                .map(jutsu -> jutsuRepository.findByTipo(jutsu.getTipo())
                                                .orElseGet(() -> jutsuRepository.save(
                                                                Jutsu.builder().tipo(jutsu.getTipo()).build())))
                                .toList();

                personagem.setJutsus(jutsusTratados);

                Personagem personagemComTipo = personagemFactoryImpl.construirTipoPersonagem(
                                personagemMapper.toDto(personagem));

                Personagem personagemSalvo = personagemRepository.save(personagemComTipo);

                return personagemMapper.toDto(personagemSalvo);
        }

        @Override
        public PersonagemDTO buscarPersonagemPorId(Long id) {
                Personagem personagem = personagemValidator.validarExistencia(id);
                return personagemMapper.toDto(personagem);
        }

        @Override
        public void deletarPersonagem(Long id) {
                personagemValidator.validarExistencia(id);
                personagemRepository.deleteById(id);
        }

        @Override
        public PersonagemDTO atualizarPersonagem(Long id, PersonagemDTO dto) {
                Personagem personagemExistente = personagemValidator.validarExistencia(id);

                personagemExistente.setNome(dto.getNome());
                personagemExistente.setIdade(dto.getIdade());
                personagemExistente.setAldeia(dto.getAldeia());
                personagemExistente.setChakra(dto.getChakra());

                Personagem personagemAtualizado = personagemRepository.save(personagemExistente);

                return personagemMapper.toDto(personagemAtualizado);
        }

        @Override
        public Page<PersonagemDTO> filtrarPersonagens(
                        String nome, Long idade, Long idadeMin, Long idadeMax,
                        String aldeia, String jutsuTipo, Long chakra, Pageable pageable) {

                Specification<Personagem> spec = new SpecificationBuilder<Personagem>()
                                .add(PersonagemSpecifications.comNomeContendo(nome))
                                .add(PersonagemSpecifications.comIdadeIgualA(idade))
                                .add(PersonagemSpecifications.comIdadeEntre(idadeMin, idadeMax))
                                .add(PersonagemSpecifications.comAldeiaContendo(aldeia))
                                .add(PersonagemSpecifications.comJutsusContendo(jutsuTipo))
                                .add(PersonagemSpecifications.comChakraIgualA(chakra))
                                .build();

                return personagemRepository.findAll(spec, pageable)
                                .map(personagemMapper::toDto);
        }

}
