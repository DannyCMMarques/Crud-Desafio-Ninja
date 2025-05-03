package com.crud.demo.services;

import java.util.Random;
import java.util.logging.Logger;

import org.springframework.stereotype.Service;

import com.crud.demo.events.AtaqueEvent;
import com.crud.demo.events.EstatisticaDoJogadorEvent;
import com.crud.demo.factories.PersonagemFactoryImpl;
import com.crud.demo.models.Personagem;
import com.crud.demo.models.DTO.websocket.DialogoDTO;
import com.crud.demo.models.contratos.Ninja;
import com.crud.demo.models.mappers.PersonagemMapper;
import com.crud.demo.services.contratos.AtacarService;
import com.crud.demo.services.contratos.ChakrasService;
import com.crud.demo.services.contratos.DefesaService;
import com.crud.demo.websocket.notificacoes.DialogoNotificacoes;

import lombok.RequiredArgsConstructor;

@RequiredArgsConstructor
@Service
public class DefesaServiceImpl implements DefesaService {

    private static final Logger log = Logger.getLogger(DefesaServiceImpl.class.getName());

    private final PersonagemFactoryImpl personagemFactoryImpl;
    private final AtacarService atacarService;
    private final PersonagemMapper personagemMapper;
    private final DialogoNotificacoes dialogoNotificacao;
    private static final int MAX_CHAKRA = 100;
    private final ChakrasService chakraService;

    @Override
    public void defender(AtaqueEvent ataqueEvent) {
        Long idBatalha = ataqueEvent.getIdBatalha();
        EstatisticaDoJogadorEvent defensor = ataqueEvent.getDefensor();
        Personagem personagemComTipo = personagemFactoryImpl.construirTipoPersonagem(
                personagemMapper.toRequestDto(defensor.getPersonagem()));
                     Random random = new Random();
        int numeroAleatorio = random.nextInt(MAX_CHAKRA) + 1;

        if (numeroAleatorio <= defensor.getValor_chakra() / 2) {
            log.info(String.format("Numero aleatorio: %d", numeroAleatorio));
            if (personagemComTipo instanceof Ninja) {
                String dialogoDefesa = ((Ninja) personagemComTipo).desviar(defensor);
                log.info(String.format("Defendeu : %s", dialogoDefesa));

                DialogoDTO dialogoDeDefesa = DialogoDTO.builder()
                        .text(dialogoDefesa)
                        .build();
                dialogoNotificacao.enviarDialogo(idBatalha, dialogoDeDefesa);
            }

        } else {
            DialogoDTO dialogo = DialogoDTO.builder()
                    .text(personagemComTipo.getNome() + " não conseguiu se defender")
                    .build();
            log.info("Não Defendeu : " + dialogo);

            dialogoNotificacao.enviarDialogo(idBatalha, dialogo);
            atacarService.atacar(ataqueEvent);
            chakraService.calcularDerrota(ataqueEvent);
        }

    }
}
