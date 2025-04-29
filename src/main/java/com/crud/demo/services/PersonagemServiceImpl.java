package com.crud.demo.services;

import java.util.Map;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.crud.demo.factories.PersonagemFactoryImpl;
import com.crud.demo.models.Jutsu;
import com.crud.demo.models.Personagem;
import com.crud.demo.models.DTO.PersonagemDTO;
import com.crud.demo.models.enuns.CategoriaEspecialidadeEnum;
import com.crud.demo.models.mappers.PersonagemMapper;
import com.crud.demo.repositories.JutsuRepository;
import com.crud.demo.repositories.PersonagemRepository;
import com.crud.demo.services.contratos.PersonagemService;
import com.crud.demo.specifications.PersonagemSpecifications;
import com.crud.demo.utils.SpecificationBuilder;
import com.crud.demo.validators.PersonagemValidator;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class PersonagemServiceImpl implements PersonagemService {

        private final PersonagemRepository personagemRepository;
        private final JutsuRepository jutsuRepository;
        private final PersonagemMapper personagemMapper;
        private final PersonagemValidator personagemValidator;
        private final PersonagemFactoryImpl personagemFactoryImpl;

        @Override
        @Transactional
        public PersonagemDTO criarPersonagem(PersonagemDTO dto) {
                personagemValidator.validarCadastro(dto.getNome());

                Map<String, Jutsu> jutsusPersonagem = jutsuRepository.findMapByCategoria(dto.getEspecialidade());

                Personagem personagemComTipo = personagemFactoryImpl
                                .construirTipoPersonagem(dto);
                personagemComTipo.setJutsus(jutsusPersonagem);
                Personagem salvo = personagemRepository.save(personagemComTipo);
                return personagemMapper.toDto(salvo);
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
        @Transactional
        public PersonagemDTO atualizarPersonagem(Long id, PersonagemDTO dto) {
                Personagem existente = personagemValidator.validarExistencia(id);

                existente.setNome(dto.getNome());
                existente.setIdade(dto.getIdade());
                existente.setAldeia(dto.getAldeia());
                existente.setEspecialidade(dto.getEspecialidade());

                Personagem atualizado = personagemRepository.save(existente);
                return personagemMapper.toDto(atualizado);
        }

        @Override
        public Page<PersonagemDTO> filtrarPersonagens(
                        String nome,
                        Long idade,
                        Long idadeMin,
                        Long idadeMax,
                        String aldeia,
                        Pageable pageable,
                        CategoriaEspecialidadeEnum especialidade) {

                Specification<Personagem> spec = new SpecificationBuilder<Personagem>()
                                .add(PersonagemSpecifications.comNomeContendo(nome))
                                .add(PersonagemSpecifications.comIdadeIgualA(idade))
                                .add(PersonagemSpecifications.comIdadeEntre(idadeMin, idadeMax))
                                .add(PersonagemSpecifications.comAldeiaContendo(aldeia))
                                .add(PersonagemSpecifications.comEspecialidade(especialidade))
                                .build();

                return personagemRepository.findAll(spec, pageable)
                                .map(personagemMapper::toDto);
        }

}
