package com.crud.demo.services;

import org.springframework.data.domain.Page;
import org.springframework.data.domain.Pageable;
import org.springframework.data.jpa.domain.Specification;
import org.springframework.stereotype.Service;

import com.crud.demo.factories.PersonagemFactoryImpl;
import com.crud.demo.models.DTO.PersonagemDTO;
import com.crud.demo.models.Personagem;
import com.crud.demo.models.mappers.PersonagemMappers;
import com.crud.demo.repositories.PersonagemRepository;
import com.crud.demo.services.contratos.PersonagemService;
import com.crud.demo.specifications.PersonagemSpecifications;
import com.crud.demo.validators.PersonagemValidator;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class PersonagemServiceImpl implements PersonagemService {

    private final PersonagemRepository personagemRepository;
    private final PersonagemMappers pessoaMappers;
    private final PersonagemValidator personagemValidator;
    private final PersonagemFactoryImpl personagemFactoryImpl;

    @Override
    public PersonagemDTO criarPersonagem(PersonagemDTO pessoaDTO) {

        Personagem personagem = personagemFactoryImpl.construirTipoPersonagem(pessoaDTO);
        personagemValidator
                .validarCadastro(personagem.getNome());

        Personagem personagemSalvo = personagemRepository.save(personagem);
        return pessoaMappers.toDto(personagemSalvo);

    }

    @Override
    public PersonagemDTO buscarPersonagemPorId(Long id) {
        Personagem personagem = personagemValidator.validarExistencia(id);
        return pessoaMappers.toDto(personagem);
    }

    @Override
    public void deletarPersonagem(Long id) {
        personagemValidator.validarExistencia(id);
        personagemRepository.deleteById(id);
    }

    @Override
    public PersonagemDTO atualizarPersonagem(Long id, PersonagemDTO personagemDTO) {
        personagemValidator
                .validarExistencia(id);

        Personagem personagem = personagemFactoryImpl.construirTipoPersonagem(personagemDTO);
        personagem.setId(id);

        Personagem personagemAtualizado = personagemRepository.save(personagem);
        return pessoaMappers.toDto(personagemAtualizado);
    }

    @Override
    public Page<PersonagemDTO> filtrarPersonagens(String nome, long idade, long idadeMin, long idadeMax,
            String aldeia, String jutsus, String chakra, Pageable pageable) {
        Specification<Personagem> spec = Specification
                .where(PersonagemSpecifications.comNomeContendo(nome))
                .and(PersonagemSpecifications.comIdadeIgualA(idade))
                .and(PersonagemSpecifications.comIdadeEntre(idadeMin, idadeMax))
                .and(PersonagemSpecifications.comAldeiaContendo(aldeia))
                .and(PersonagemSpecifications.comJutsusContendo(jutsus))
                .and(PersonagemSpecifications.comChakraIgualA(chakra));
        return personagemRepository
                .findAll(spec, pageable)
                .map(pessoaMappers::toDto);
    }

}