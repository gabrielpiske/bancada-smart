package com.smart1.appsmartweb.service.station;

import java.util.LinkedHashMap;
import java.util.Map;
import java.util.concurrent.atomic.AtomicReference;

import org.springframework.stereotype.Component;

import com.smart1.appsmartweb.config.BancadaProperties;
import com.smart1.appsmartweb.plc.mapper.AssemblyDb57Mapper.AssemblyPlcSnapshot;
import com.smart1.appsmartweb.plc.mapper.ExpeditionDb9Mapper.ExpeditionPlcSnapshot;
import com.smart1.appsmartweb.plc.mapper.ProcessDb2Mapper.ProcessPlcSnapshot;
import com.smart1.appsmartweb.plc.mapper.StockDb9Mapper.StockPlcSnapshot;

/**
 * Armazenamento central thread-safe do estado atual da Bancada Smart (corrige BUG-09).
 */
@Component
public class BancadaStateStore {

    private volatile boolean readOnly = false;
    private volatile boolean auxExpedicao = false;
    private volatile boolean pedidoEmCurso = false;
    private volatile byte statusProducao = 0;
    private volatile byte statusEstoque = 0;
    private volatile byte statusProcesso = 0;
    private volatile byte statusMontagem = 0;
    private volatile byte statusExpedicao = 0;
    private volatile int posicaoEstoqueSolicitada = 0;
    private volatile int posicaoExpedicaoSolicitada = 0;
    private volatile boolean blockFinished = false;

    private final AtomicReference<StockPlcSnapshot> stockSnapshot =
            new AtomicReference<>(StockPlcSnapshot.empty());
    private final AtomicReference<ProcessPlcSnapshot> processSnapshot =
            new AtomicReference<>(ProcessPlcSnapshot.empty());
    private final AtomicReference<AssemblyPlcSnapshot> assemblySnapshot =
            new AtomicReference<>(AssemblyPlcSnapshot.empty());
    private final AtomicReference<ExpeditionPlcSnapshot> expeditionSnapshot =
            new AtomicReference<>(ExpeditionPlcSnapshot.empty());

    public BancadaStateStore(BancadaProperties properties) {
        if (properties != null && properties.getPlc() != null) {
            this.readOnly = properties.getPlc().isReadOnly();
        }
    }

    public boolean isReadOnly() {
        return readOnly;
    }

    public void setReadOnly(boolean readOnly) {
        this.readOnly = readOnly;
    }

    public boolean isAuxExpedicao() {
        return auxExpedicao;
    }

    public void setAuxExpedicao(boolean auxExpedicao) {
        this.auxExpedicao = auxExpedicao;
    }

    public boolean isPedidoEmCurso() {
        return pedidoEmCurso;
    }

    public void setPedidoEmCurso(boolean pedidoEmCurso) {
        this.pedidoEmCurso = pedidoEmCurso;
    }

    public byte getStatusProducao() {
        return statusProducao;
    }

    public void setStatusProducao(byte statusProducao) {
        this.statusProducao = statusProducao;
    }

    public byte getStatusEstoque() {
        return statusEstoque;
    }

    public void setStatusEstoque(byte statusEstoque) {
        this.statusEstoque = statusEstoque;
    }

    public byte getStatusProcesso() {
        return statusProcesso;
    }

    public void setStatusProcesso(byte statusProcesso) {
        this.statusProcesso = statusProcesso;
    }

    public byte getStatusMontagem() {
        return statusMontagem;
    }

    public void setStatusMontagem(byte statusMontagem) {
        this.statusMontagem = statusMontagem;
    }

    public byte getStatusExpedicao() {
        return statusExpedicao;
    }

    public void setStatusExpedicao(byte statusExpedicao) {
        this.statusExpedicao = statusExpedicao;
    }

    public int getPosicaoEstoqueSolicitada() {
        return posicaoEstoqueSolicitada;
    }

    public void setPosicaoEstoqueSolicitada(int posicaoEstoqueSolicitada) {
        this.posicaoEstoqueSolicitada = posicaoEstoqueSolicitada;
    }

    public int getPosicaoExpedicaoSolicitada() {
        return posicaoExpedicaoSolicitada;
    }

    public void setPosicaoExpedicaoSolicitada(int posicaoExpedicaoSolicitada) {
        this.posicaoExpedicaoSolicitada = posicaoExpedicaoSolicitada;
    }

    public boolean isBlockFinished() {
        return blockFinished;
    }

    public void setBlockFinished(boolean blockFinished) {
        this.blockFinished = blockFinished;
    }

    public StockPlcSnapshot getStockSnapshot() {
        return stockSnapshot.get();
    }

    public void setStockSnapshot(StockPlcSnapshot snapshot) {
        if (snapshot != null) {
            stockSnapshot.set(snapshot);
        }
    }

    public ProcessPlcSnapshot getProcessSnapshot() {
        return processSnapshot.get();
    }

    public void setProcessSnapshot(ProcessPlcSnapshot snapshot) {
        if (snapshot != null) {
            processSnapshot.set(snapshot);
        }
    }

    public AssemblyPlcSnapshot getAssemblySnapshot() {
        return assemblySnapshot.get();
    }

    public void setAssemblySnapshot(AssemblyPlcSnapshot snapshot) {
        if (snapshot != null) {
            assemblySnapshot.set(snapshot);
        }
    }

    public ExpeditionPlcSnapshot getExpeditionSnapshot() {
        return expeditionSnapshot.get();
    }

    public void setExpeditionSnapshot(ExpeditionPlcSnapshot snapshot) {
        if (snapshot != null) {
            expeditionSnapshot.set(snapshot);
        }
    }

    public Map<String, Object> toStateMap() {
        Map<String, Object> map = new LinkedHashMap<>();

        // Globais
        map.put("readOnly", readOnly);
        map.put("aux_expedicao", auxExpedicao);
        map.put("pedidoEmCurso", pedidoEmCurso);
        map.put("statusProducao", statusProducao);
        map.put("statusEstoque", statusEstoque);
        map.put("statusProcesso", statusProcesso);
        map.put("statusMontagem", statusMontagem);
        map.put("statusExpedicao", statusExpedicao);
        map.put("posicaoEstoqueSolicitada", posicaoEstoqueSolicitada);
        map.put("posicaoExpedicaoSolicitada", posicaoExpedicaoSolicitada);
        map.put("blockFinished", blockFinished);

        // Estoque
        StockPlcSnapshot est = stockSnapshot.get();
        map.put("recebidoOpEst", est.recebidoOpEst());
        map.put("cor_Andar_1", est.cor_Andar_1());
        map.put("posicao_Estoque_Andar_1", est.posicao_Estoque_Andar_1());
        map.put("cor_Lamina_1_Andar_1", est.cor_Lamina_1_Andar_1());
        map.put("cor_Lamina_2_Andar_1", est.cor_Lamina_2_Andar_1());
        map.put("cor_Lamina_3_Andar_1", est.cor_Lamina_3_Andar_1());
        map.put("padrao_Lamina_1_Andar_1", est.padrao_Lamina_1_Andar_1());
        map.put("padrao_Lamina_2_Andar_1", est.padrao_Lamina_2_Andar_1());
        map.put("padrao_Lamina_3_Andar_1", est.padrao_Lamina_3_Andar_1());
        map.put("processamento_Andar_1", est.processamento_Andar_1());

        map.put("cor_Andar_2", est.cor_Andar_2());
        map.put("posicao_Estoque_Andar_2", est.posicao_Estoque_Andar_2());
        map.put("cor_Lamina_1_Andar_2", est.cor_Lamina_1_Andar_2());
        map.put("cor_Lamina_2_Andar_2", est.cor_Lamina_2_Andar_2());
        map.put("cor_Lamina_3_Andar_2", est.cor_Lamina_3_Andar_2());
        map.put("padrao_Lamina_1_Andar_2", est.padrao_Lamina_1_Andar_2());
        map.put("padrao_Lamina_2_Andar_2", est.padrao_Lamina_2_Andar_2());
        map.put("padrao_Lamina_3_Andar_2", est.padrao_Lamina_3_Andar_2());
        map.put("processamento_Andar_2", est.processamento_Andar_2());

        map.put("cor_Andar_3", est.cor_Andar_3());
        map.put("posicao_Estoque_Andar_3", est.posicao_Estoque_Andar_3());
        map.put("cor_Lamina_1_Andar_3", est.cor_Lamina_1_Andar_3());
        map.put("cor_Lamina_2_Andar_3", est.cor_Lamina_2_Andar_3());
        map.put("cor_Lamina_3_Andar_3", est.cor_Lamina_3_Andar_3());
        map.put("padrao_Lamina_1_Andar_3", est.padrao_Lamina_1_Andar_3());
        map.put("padrao_Lamina_2_Andar_3", est.padrao_Lamina_2_Andar_3());
        map.put("padrao_Lamina_3_Andar_3", est.padrao_Lamina_3_Andar_3());
        map.put("processamento_Andar_3", est.processamento_Andar_3());

        map.put("numeroPedidoEst", est.numeroPedidoEst());
        map.put("andares", est.andares());
        map.put("posicaoExpedicaoEst", est.posicaoExpedicaoEst());
        map.put("iniciarPedido", est.iniciarPedido());
        map.put("recebidoEstoque", est.recebidoEstoque());
        map.put("iniciarGuardarEst", est.iniciarGuardarEst());
        map.put("posicaoGuardarEst", est.posicaoGuardarEst());
        map.put("posicoesOcupadas", est.posicoesOcupadas());

        map.put("numeroOPEst", est.numeroOPEst());
        map.put("cancelOPEst", est.cancelOPEst());
        map.put("finishOPEst", est.finishOPEst());
        map.put("startOPEst", est.startOPEst());
        map.put("ocupadoEst", est.ocupadoEst());
        map.put("aguardandoEst", est.aguardandoEst());
        map.put("manualEst", est.manualEst());
        map.put("emergenciaEst", est.emergenciaEst());

        map.put("pedirPosicaoEst", est.pedirPosicaoEst());
        map.put("posicaoEstoque", est.posicaoEstoque());
        map.put("adicionarEstoque", est.adicionarEstoque());
        map.put("removerEstoque", est.removerEstoque());
        map.put("retornoEstoqueCheio", est.retornoEstoqueCheio());
        map.put("corGuardarEstoque", est.corGuardarEstoque());

        map.put("xEmergenciaAtivadaEst", est.xEmergenciaAtivadaEst());
        map.put("xComutadorAutomaticoEst", est.xComutadorAutomaticoEst());
        map.put("xNecessitaHomeEixoVerticalEst", est.xNecessitaHomeEixoVerticalEst());
        map.put("xNecessitaHomeEixoGiroEst", est.xNecessitaHomeEixoGiroEst());
        map.put("xServoDesligadoEixoVerticalEst", est.xServoDesligadoEixoVerticalEst());
        map.put("xServoDesligadoEixoGiroEst", est.xServoDesligadoEixoGiroEst());
        map.put("xCondicaoIniciarEst", est.xCondicaoIniciarEst());

        // Processo
        ProcessPlcSnapshot pro = processSnapshot.get();
        map.put("recebidoOpPro", pro.recebidoOpPro());
        map.put("numeroOPPro", pro.numeroOPPro());
        map.put("cancelOPPro", pro.cancelOPPro());
        map.put("finishOPPro", pro.finishOPPro());
        map.put("startOPPro", pro.startOPPro());
        map.put("ocupadoPro", pro.ocupadoPro());
        map.put("aguardandoPro", pro.aguardandoPro());
        map.put("manualPro", pro.manualPro());
        map.put("emergenciaPro", pro.emergenciaPro());
        map.put("xEmergenciaAtivadaPro", pro.xEmergenciaAtivadaPro());
        map.put("xComutadorAutomaticoPro", pro.xComutadorAutomaticoPro());
        map.put("xNecessitaHomeEixoXPro", pro.xNecessitaHomeEixoXPro());
        map.put("xNecessitaHomeEixoYPro", pro.xNecessitaHomeEixoYPro());
        map.put("xServoDesligadoEixoXPro", pro.xServoDesligadoEixoXPro());
        map.put("xServoDesligadoEixoYPro", pro.xServoDesligadoEixoYPro());
        map.put("xCondicaoIniciarPro", pro.xCondicaoIniciarPro());

        // Montagem
        AssemblyPlcSnapshot mon = assemblySnapshot.get();
        map.put("recebidoOpMon", mon.recebidoOpMon());
        map.put("numeroOPMon", mon.numeroOPMon());
        map.put("cancelOPMon", mon.cancelOPMon());
        map.put("finishOPMon", mon.finishOPMon());
        map.put("startOPMon", mon.startOPMon());
        map.put("ocupadoMon", mon.ocupadoMon());
        map.put("aguardandoMon", mon.aguardandoMon());
        map.put("manualMon", mon.manualMon());
        map.put("emergenciaMon", mon.emergenciaMon());
        map.put("xEmergenciaAtivadaMon", mon.xEmergenciaAtivadaMon());
        map.put("xComutadorAutomaticoMon", mon.xComutadorAutomaticoMon());
        map.put("xCondicaoIniciarMon", mon.xCondicaoIniciarMon());

        // Expedição
        ExpeditionPlcSnapshot exp = expeditionSnapshot.get();
        map.put("recebidoOpExp", exp.recebidoOpExp());
        map.put("recebidoExpedicao", exp.recebidoExpedicao());
        map.put("iniciarGuardarExp", exp.iniciarGuardarExp());
        map.put("posicaoGuardarExp", exp.posicaoGuardarExp());
        map.put("orderExpedicao", exp.orderExpedicao());
        map.put("numeroOPExp", exp.numeroOPExp());
        map.put("cancelOPExp", exp.cancelOPExp());
        map.put("finishOPExp", exp.finishOPExp());
        map.put("startOPExp", exp.startOPExp());
        map.put("ocupadoExp", exp.ocupadoExp());
        map.put("aguardandoExp", exp.aguardandoExp());
        map.put("manualExp", exp.manualExp());
        map.put("emergenciaExp", exp.emergenciaExp());
        map.put("pedirPosicaoExp", exp.pedirPosicaoExp());
        map.put("posicaoGuardadoExpedicao", exp.posicaoGuardadoExpedicao());
        map.put("posicaoRemovidoExpedicao", exp.posicaoRemovidoExpedicao());
        map.put("adicionarExpedicao", exp.adicionarExpedicao());
        map.put("removerExpedicao", exp.removerExpedicao());
        map.put("opGuardadoExpedicao", exp.opGuardadoExpedicao());
        map.put("xEmergenciaAtivadaExp", exp.xEmergenciaAtivadaExp());
        map.put("xComutadorAutomaticoExp", exp.xComutadorAutomaticoExp());
        map.put("xNecessitaHomeEixoVerticalExp", exp.xNecessitaHomeEixoVerticalExp());
        map.put("xNecessitaHomeEixoGiroExp", exp.xNecessitaHomeEixoGiroExp());
        map.put("xNecessitaHomeEixoHorizontalExp", exp.xNecessitaHomeEixoHorizontalExp());
        map.put("xServoDesligadoEixoHorizontalExp", exp.xServoDesligadoEixoHorizontalExp());
        map.put("xServoDesligadoEixoGiroExp", exp.xServoDesligadoEixoGiroExp());
        map.put("xServoDesligadoEixoVerticalExp", exp.xServoDesligadoEixoVerticalExp());
        map.put("xCondicaoIniciarExp", exp.xCondicaoIniciarExp());

        return map;
    }
}
