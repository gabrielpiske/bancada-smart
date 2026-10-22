package com.smart1.appsmartweb.controller;

import static org.assertj.core.api.Assertions.assertThat;
import static org.hamcrest.Matchers.containsString;
import static org.springframework.test.web.servlet.request.MockMvcRequestBuilders.get;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.content;
import static org.springframework.test.web.servlet.result.MockMvcResultMatchers.status;

import java.util.Map;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.autoconfigure.web.servlet.AutoConfigureMockMvc;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.test.web.servlet.MockMvc;

@SpringBootTest
@AutoConfigureMockMvc
class SmartServiceControllerTest {

    @Autowired
    private SmartServiceController smartServiceController;

    @Autowired
    private MockMvc mockMvc;

    @Test
    void getEstadoAtualReturnsAllGlobalAndFourStationKeysForMonitorAndView() {
        Map<String, Object> estado = smartServiceController.getEstadoAtual();

        // Verifica que todas as categorias de variáveis exigidas por monitor.html e view.js estão presentes (BUG-08)
        assertThat(estado)
                .hasSizeGreaterThanOrEqualTo(110)
                .containsKeys(
                        // Globais
                        "readOnly", "aux_expedicao", "pedidoEmCurso", "blockFinished",
                        "statusProducao", "statusEstoque", "statusProcesso", "statusMontagem", "statusExpedicao",
                        "posicaoEstoqueSolicitada", "posicaoExpedicaoSolicitada",
                        // Estoque + Andares + Realidade Aumentada
                        "recebidoOpEst", "finishOPEst", "startOPEst", "cancelOPEst", "posicoesOcupadas",
                        "cor_Andar_1", "cor_Andar_2", "cor_Andar_3", "xEmergenciaAtivadaEst",
                        // Processo
                        "recebidoOpPro", "finishOPPro", "startOPPro", "cancelOPPro", "xEmergenciaAtivadaPro",
                        // Montagem
                        "recebidoOpMon", "finishOPMon", "startOPMon", "cancelOPMon", "xEmergenciaAtivadaMon",
                        // Expedição
                        "recebidoOpExp", "finishOPExp", "startOPExp", "cancelOPExp", "orderExpedicao", "xEmergenciaAtivadaExp"
                );
    }

    @Test
    void allFiveFrontendPagesRenderSuccessfullyWithSenaiBrandingAndCredits() throws Exception {
        String[] routes = { "/", "/estoque", "/loja", "/view", "/monitoramento" };
        for (String route : routes) {
            mockMvc.perform(get(route))
                    .andExpect(status().isOk())
                    .andExpect(content().string(containsString("BANCADA")))
                    .andExpect(content().string(containsString("Gabriel Schweder Piske")))
                    .andExpect(content().string(containsString("Julio Henrique Busarello")))
                    .andExpect(content().string(containsString("SENAI Timbó")));
        }
    }
}
