package com.smart1.appsmartweb.controller;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.Map;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

@SpringBootTest
class SmartServiceControllerTest {

    @Autowired
    private SmartServiceController smartServiceController;

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
}
