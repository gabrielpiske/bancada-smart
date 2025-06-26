package com.smart1.appsmartweb.controller;

import java.util.Arrays;
import java.util.HashMap;
import java.util.Map;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.smart1.appsmartweb.service.connection.SmartService;

@RestController
public class SmartServiceController {

    private final SmartService smartService;

    public SmartServiceController(SmartService smartService) {
        this.smartService = smartService;
    }

    @GetMapping("/api/estado")
    public Map<String, Object> getEstadoAtual() {
        Map<String, Object> estado = new HashMap<>();

        // Exemplo com algumas variáveis (a lista completa você pode colar abaixo):
        estado.put("readOnly", smartService.readOnly);
        estado.put("aux_expedicao", smartService.aux_expedicao);
        estado.put("pedidoEmCurso", smartService.pedidoEmCurso);
        estado.put("statusProducao", smartService.statusProducao);
        estado.put("statusEstoque", smartService.statusEstoque);
        estado.put("statusProcesso", smartService.statusProcesso);
        estado.put("statusMontagem", smartService.statusMontagem);
        estado.put("statusExpedicao", smartService.statusExpedicao);
        estado.put("posicaoEstoqueSolicitada", smartService.posicaoEstoqueSolicitada);
        estado.put("posicaoExpedicaoSolicitada", smartService.posicaoExpedicaoSolicitada);
        estado.put("blockFinished", smartService.blockFinished);
        estado.put("recebidoOpEst", smartService.recebidoOpEst);
        estado.put("cor_Andar_1", smartService.cor_Andar_1);
        estado.put("posicao_Estoque_Andar_1", smartService.posicao_Estoque_Andar_1);
        estado.put("cor_Lamina_1_Andar_1", smartService.cor_Lamina_1_Andar_1);
        estado.put("cor_Lamina_2_Andar_1", smartService.cor_Lamina_2_Andar_1);
        estado.put("cor_Lamina_3_Andar_1", smartService.cor_Lamina_3_Andar_1);
        estado.put("padrao_Lamina_1_Andar_1", smartService.padrao_Lamina_1_Andar_1);
        estado.put("padrao_Lamina_2_Andar_1", smartService.padrao_Lamina_2_Andar_1);
        estado.put("padrao_Lamina_3_Andar_1", smartService.padrao_Lamina_3_Andar_1);
        estado.put("processamento_Andar_1", smartService.processamento_Andar_1);
        estado.put("cor_Andar_2", smartService.cor_Andar_2);
        estado.put("posicao_Estoque_Andar_2", smartService.posicao_Estoque_Andar_2);
        estado.put("cor_Lamina_1_Andar_2", smartService.cor_Lamina_1_Andar_2);
        estado.put("cor_Lamina_2_Andar_2", smartService.cor_Lamina_2_Andar_2);
        estado.put("cor_Lamina_3_Andar_2", smartService.cor_Lamina_3_Andar_2);
        estado.put("padrao_Lamina_1_Andar_2", smartService.padrao_Lamina_1_Andar_2);
        estado.put("padrao_Lamina_2_Andar_2", smartService.padrao_Lamina_2_Andar_2);
        estado.put("padrao_Lamina_3_Andar_2", smartService.padrao_Lamina_3_Andar_2);
        estado.put("processamento_Andar_2", smartService.processamento_Andar_2);
        estado.put("cor_Andar_3", smartService.cor_Andar_3);
        estado.put("posicao_Estoque_Andar_3", smartService.posicao_Estoque_Andar_3);
        estado.put("cor_Lamina_1_Andar_3", smartService.cor_Lamina_1_Andar_3);
        estado.put("cor_Lamina_2_Andar_3", smartService.cor_Lamina_2_Andar_3);
        estado.put("cor_Lamina_3_Andar_3", smartService.cor_Lamina_3_Andar_3);
        estado.put("padrao_Lamina_1_Andar_3", smartService.padrao_Lamina_1_Andar_3);
        estado.put("padrao_Lamina_2_Andar_3", smartService.padrao_Lamina_2_Andar_3);
        estado.put("padrao_Lamina_3_Andar_3", smartService.padrao_Lamina_3_Andar_3);
        estado.put("processamento_Andar_3", smartService.processamento_Andar_3);
        estado.put("numeroPedidoEst", smartService.numeroPedidoEst);
        estado.put("andares", smartService.andares);
        estado.put("posicaoExpedicaoEst", smartService.posicaoExpedicaoEst);
        estado.put("iniciarPedido", smartService.iniciarPedido);
        estado.put("recebidoEstoque", smartService.recebidoEstoque);
        estado.put("iniciarGuardarEst", smartService.iniciarGuardarEst);
        estado.put("posicaoGuardarEst", smartService.posicaoGuardarEst);
        estado.put("posicoesOcupadas", Arrays.toString(smartService.posicoesOcupadas));
        estado.put("numeroOPEst", smartService.numeroOPEst);
        estado.put("cancelOPEst", smartService.cancelOPEst);
        estado.put("finishOPEst", smartService.finishOPEst);
        estado.put("startOPEst", smartService.startOPEst);
        estado.put("ocupadoEst", smartService.ocupadoEst);
        estado.put("aguardandoEst", smartService.aguardandoEst);
        estado.put("manualEst", smartService.manualEst);
        estado.put("emergenciaEst", smartService.emergenciaEst);
        estado.put("pedirPosicaoEst", smartService.pedirPosicaoEst);
        estado.put("posicaoEstoque", smartService.posicaoEstoque);
        estado.put("adicionarEstoque", smartService.adicionarEstoque);
        estado.put("removerEstoque", smartService.removerEstoque);
        estado.put("retornoEstoqueCheio", smartService.retornoEstoqueCheio);
        estado.put("corGuardarEstoque", smartService.corGuardarEstoque);
        estado.put("xEmergenciaAtivadaEst", smartService.xEmergenciaAtivadaEst);
        estado.put("xComutadorAutomaticoEst", smartService.xComutadorAutomaticoEst);
        estado.put("xNecessitaHomeEixoVerticalEst", smartService.xNecessitaHomeEixoVerticalEst);
        estado.put("xNecessitaHomeEixoGiroEst", smartService.xNecessitaHomeEixoGiroEst);
        estado.put("xServoDesligadoEixoVerticalEst", smartService.xServoDesligadoEixoVerticalEst);
        estado.put("xServoDesligadoEixoGiroEst", smartService.xServoDesligadoEixoGiroEst);
        estado.put("xCondicaoIniciarEst", smartService.xCondicaoIniciarEst);
        estado.put("recebidoOpPro", smartService.recebidoOpPro);
        estado.put("numeroOPPro", smartService.numeroOPPro);
        estado.put("cancelOPPro", smartService.cancelOPPro);
        estado.put("finishOPPro", smartService.finishOPPro);
        estado.put("startOPPro", smartService.startOPPro);
        estado.put("ocupadoPro", smartService.ocupadoPro);
        estado.put("aguardandoPro", smartService.aguardandoPro);
        estado.put("manualPro", smartService.manualPro);
        estado.put("emergenciaPro", smartService.emergenciaPro);
        estado.put("xEmergenciaAtivadaPro", smartService.xEmergenciaAtivadaPro);
        estado.put("xComutadorAutomaticoPro", smartService.xComutadorAutomaticoPro);
        estado.put("xNecessitaHomeEixoXPro", smartService.xNecessitaHomeEixoXPro);
        estado.put("xNecessitaHomeEixoYPro", smartService.xNecessitaHomeEixoYPro);
        estado.put("xServoDesligadoEixoXPro", smartService.xServoDesligadoEixoXPro);
        estado.put("xServoDesligadoEixoYPro", smartService.xServoDesligadoEixoYPro);
        estado.put("xCondicaoIniciarPro", smartService.xCondicaoIniciarPro);
        estado.put("recebidoOpMon", smartService.recebidoOpMon);
        estado.put("numeroOPMon", smartService.numeroOPMon);
        estado.put("cancelOPMon", smartService.cancelOPMon);
        estado.put("finishOPMon", smartService.finishOPMon);
        estado.put("startOPMon", smartService.startOPMon);
        estado.put("ocupadoMon", smartService.ocupadoMon);
        estado.put("aguardandoMon", smartService.aguardandoMon);
        estado.put("manualMon", smartService.manualMon);
        estado.put("emergenciaMon", smartService.emergenciaMon);
        estado.put("xEmergenciaAtivadaMon", smartService.xEmergenciaAtivadaMon);
        estado.put("xComutadorAutomaticoMon", smartService.xComutadorAutomaticoMon);
        estado.put("xCondicaoIniciarMon", smartService.xCondicaoIniciarMon);
        estado.put("recebidoOpExp", smartService.recebidoOpExp);
        estado.put("recebidoExpedicao", smartService.recebidoExpedicao);
        estado.put("iniciarGuardarExp", smartService.iniciarGuardarExp);
        estado.put("posicaoGuardarExp", smartService.posicaoGuardarExp);
        estado.put("orderExpedicao", Arrays.toString(smartService.orderExpedicao));
        estado.put("numeroOPExp", smartService.numeroOPExp);
        estado.put("cancelOPExp", smartService.cancelOPExp);
        estado.put("finishOPExp", smartService.finishOPExp);
        estado.put("startOPExp", smartService.startOPExp);
        estado.put("ocupadoExp", smartService.ocupadoExp);
        estado.put("aguardandoExp", smartService.aguardandoExp);
        estado.put("manualExp", smartService.manualExp);
        estado.put("emergenciaExp", smartService.emergenciaExp);
        estado.put("pedirPosicaoExp", smartService.pedirPosicaoExp);
        estado.put("posicaoGuardadoExpedicao", smartService.posicaoGuardadoExpedicao);
        estado.put("posicaoRemovidoExpedicao", smartService.posicaoRemovidoExpedicao);
        estado.put("adicionarExpedicao", smartService.adicionarExpedicao);
        estado.put("removerExpedicao", smartService.removerExpedicao);
        estado.put("opGuardadoExpedicao", smartService.opGuardadoExpedicao);
        estado.put("xEmergenciaAtivadaExp", smartService.xEmergenciaAtivadaExp);
        estado.put("xComutadorAutomaticoExp", smartService.xComutadorAutomaticoExp);
        estado.put("xNecessitaHomeEixoVerticalExp", smartService.xNecessitaHomeEixoVerticalExp);
        estado.put("xNecessitaHomeEixoGiroExp", smartService.xNecessitaHomeEixoGiroExp);
        estado.put("xNecessitaHomeEixoHorizontalExp", smartService.xNecessitaHomeEixoHorizontalExp);
        estado.put("xServoDesligadoEixoHorizontalExp", smartService.xServoDesligadoEixoHorizontalExp);
        estado.put("xServoDesligadoEixoGiroExp", smartService.xServoDesligadoEixoGiroExp);
        estado.put("xServoDesligadoEixoVerticalExp", smartService.xServoDesligadoEixoVerticalExp);
        estado.put("xCondicaoIniciarExp", smartService.xCondicaoIniciarExp);

        return estado;
    }
}
