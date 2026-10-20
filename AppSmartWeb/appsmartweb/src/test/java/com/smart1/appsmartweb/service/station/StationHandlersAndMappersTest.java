package com.smart1.appsmartweb.service.station;

import static org.assertj.core.api.Assertions.assertThat;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.smart1.appsmartweb.plc.mapper.AssemblyDb57Mapper;
import com.smart1.appsmartweb.plc.mapper.AssemblyDb57Mapper.AssemblyPlcSnapshot;
import com.smart1.appsmartweb.plc.mapper.ExpeditionDb9Mapper;
import com.smart1.appsmartweb.plc.mapper.ExpeditionDb9Mapper.ExpeditionPlcSnapshot;
import com.smart1.appsmartweb.plc.mapper.ProcessDb2Mapper;
import com.smart1.appsmartweb.plc.mapper.ProcessDb2Mapper.ProcessPlcSnapshot;
import com.smart1.appsmartweb.plc.mapper.StockDb9Mapper;
import com.smart1.appsmartweb.plc.mapper.StockDb9Mapper.StockPlcSnapshot;
import com.smart1.appsmartweb.plc.simulator.InMemorySimulatedPlcClient;
import com.smart1.appsmartweb.repository.BlockRepository;

@SpringBootTest
class StationHandlersAndMappersTest {

    @Autowired
    private BancadaStateStore stateStore;

    @Autowired
    private StockStationHandler stockStationHandler;

    @Autowired
    private ProcessStationHandler processStationHandler;

    @Autowired
    private AssemblyStationHandler assemblyStationHandler;

    @Autowired
    private ExpeditionStationHandler expeditionStationHandler;

    @Autowired
    private BlockRepository blockRepository;

    @BeforeEach
    void setUp() {
        blockRepository.deleteAll();
        stateStore.setReadOnly(false);
        stateStore.setAuxExpedicao(false);
        stateStore.setPedidoEmCurso(true);
        stateStore.setStatusProducao((byte) 0);
        stateStore.setPosicaoExpedicaoSolicitada(1);
        expeditionStationHandler.resetLastProcessedOp();
    }

    @Test
    void stockDb9MapperDecodesAll111BytesAndOrderBufferInBigEndian() {
        int[] orderShorts = new int[30];
        orderShorts[0] = 2; // cor_Andar_1 = Vermelho
        orderShorts[1] = 7; // posicao_Estoque_Andar_1 = 7
        orderShorts[8] = 1; // processamento_Andar_1 = 1
        orderShorts[27] = 15; // numeroPedidoEst = 15
        orderShorts[28] = 1; // andares = 1
        orderShorts[29] = 4; // posicaoExpedicaoEst = 4

        byte[] orderBytes = StockDb9Mapper.serializeOrderBuffer(orderShorts);
        assertThat(orderBytes).hasSize(60);

        byte[] db9 = new byte[111];
        System.arraycopy(orderBytes, 0, db9, 2, 60);
        db9[0] = 0x01; // recebidoOpEst = true
        db9[62] = 0x01; // iniciarPedido = true
        db9[64] = 0x03; // recebidoEstoque = true, iniciarGuardarEst = true
        ByteBuffer.wrap(db9, 66, 2).order(ByteOrder.BIG_ENDIAN).putShort((short) 12); // posicaoGuardarEst = 12
        db9[68] = 3; // posicoesOcupadas[0] = Azul
        ByteBuffer.wrap(db9, 96, 2).order(ByteOrder.BIG_ENDIAN).putShort((short) 15); // numeroOPEst = 15
        db9[98] = 0x04; // startOPEst = true
        db9[100] = 0x01; // ocupadoEst = true
        db9[110] = 0b01000011; // xEmergenciaAtivadaEst=true, xComutadorAutomaticoEst=true, xCondicaoIniciarEst=true

        StockPlcSnapshot snap = StockDb9Mapper.fromBytes(db9);
        assertThat(snap.recebidoOpEst()).isTrue();
        assertThat(snap.cor_Andar_1()).isEqualTo(2);
        assertThat(snap.posicao_Estoque_Andar_1()).isEqualTo(7);
        assertThat(snap.numeroPedidoEst()).isEqualTo(15);
        assertThat(snap.andares()).isEqualTo(1);
        assertThat(snap.posicaoExpedicaoEst()).isEqualTo(4);
        assertThat(snap.iniciarPedido()).isTrue();
        assertThat(snap.recebidoEstoque()).isTrue();
        assertThat(snap.iniciarGuardarEst()).isTrue();
        assertThat(snap.posicaoGuardarEst()).isEqualTo(12);
        assertThat(snap.posicoesOcupadas()[0]).isEqualTo((byte) 3);
        assertThat(snap.numeroOPEst()).isEqualTo(15);
        assertThat(snap.startOPEst()).isTrue();
        assertThat(snap.ocupadoEst()).isTrue();
        assertThat(snap.xEmergenciaAtivadaEst()).isTrue();
        assertThat(snap.xComutadorAutomaticoEst()).isTrue();
        assertThat(snap.xCondicaoIniciarEst()).isTrue();
    }

    @Test
    void stockStationHandlerAddsAndRemovesBlockInDatabaseWithoutRestTemplateError() throws Exception {
        InMemorySimulatedPlcClient plc = new InMemorySimulatedPlcClient("10.74.241.10");
        plc.connect();

        // Simula adição de bloco pelo CLP: posicaoEstoque = 5, adicionarEstoque = true, corGuardarEstoque = 2 (Vermelho)
        byte[] db9Add = new byte[111];
        ByteBuffer.wrap(db9Add, 104, 2).order(ByteOrder.BIG_ENDIAN).putShort((short) 5);
        db9Add[106] = 0x01; // adicionarEstoque = true
        ByteBuffer.wrap(db9Add, 108, 2).order(ByteOrder.BIG_ENDIAN).putShort((short) 2);

        stockStationHandler.handle(plc, db9Add);

        assertThat(plc.readBit(9, 64, 0)).isTrue(); // recebidoEstoque = true
        assertThat(plc.readByte(9, 68 + 4)).isEqualTo((byte) 2); // posicoesOcupadas[4] = 2
        assertThat(blockRepository.findByStorageIdAndPosition(1L, 5))
                .isPresent()
                .get()
                .extracting(b -> b.getColor())
                .isEqualTo(2);

        // Simula remoção do mesmo bloco pelo CLP: posicaoEstoque = 5, removerEstoque = true
        byte[] db9Remove = new byte[111];
        ByteBuffer.wrap(db9Remove, 104, 2).order(ByteOrder.BIG_ENDIAN).putShort((short) 5);
        db9Remove[106] = 0x02; // removerEstoque = true

        stockStationHandler.handle(plc, db9Remove);

        assertThat(plc.readByte(9, 68 + 4)).isEqualTo((byte) 0);
        assertThat(blockRepository.findByStorageIdAndPosition(1L, 5)).isEmpty();
    }

    @Test
    void processAndAssemblyHandlersExecuteStandardOpHandshake() throws Exception {
        InMemorySimulatedPlcClient plcPro = new InMemorySimulatedPlcClient("10.74.241.20");
        plcPro.connect();

        byte[] db2 = new byte[9];
        db2[4] = 0x04; // startOPPro = true
        ProcessPlcSnapshot proSnap = processStationHandler.handle(plcPro, db2);
        assertThat(proSnap.startOPPro()).isTrue();
        assertThat(plcPro.readBit(2, 0, 0)).isTrue(); // recebidoOpPro = true
        assertThat(stateStore.getStatusProcesso()).isEqualTo((byte) 1);

        InMemorySimulatedPlcClient plcMon = new InMemorySimulatedPlcClient("10.74.241.30");
        plcMon.connect();

        byte[] db57 = new byte[9];
        db57[4] = 0x02; // finishOPMon = true
        AssemblyPlcSnapshot monSnap = assemblyStationHandler.handle(plcMon, db57);
        assertThat(monSnap.finishOPMon()).isTrue();
        assertThat(plcMon.readBit(57, 0, 0)).isTrue(); // recebidoOpMon = true
        assertThat(stateStore.getStatusMontagem()).isEqualTo((byte) 2);
    }

    @Test
    void expeditionStationHandlerPersistsOrderAndPreventsInfiniteLoopOnSameOp() throws Exception {
        InMemorySimulatedPlcClient plcExp = new InMemorySimulatedPlcClient("10.74.241.40");
        plcExp.connect();

        // Simula adição de pedido na Expedição: posicaoGuardarExp = 3, adicionarExpedicao = true, opGuardadoExpedicao = 10
        byte[] db9Exp = new byte[48];
        ByteBuffer.wrap(db9Exp, 4, 2).order(ByteOrder.BIG_ENDIAN).putShort((short) 3); // posicaoGuardarExp = 3
        db9Exp[42] = 0x01; // adicionarExpedicao = true
        ByteBuffer.wrap(db9Exp, 44, 2).order(ByteOrder.BIG_ENDIAN).putShort((short) 10); // opGuardadoExpedicao = 10

        ExpeditionPlcSnapshot snap1 = expeditionStationHandler.handle(plcExp, db9Exp);
        assertThat(snap1.opGuardadoExpedicao()).isEqualTo(10);
        assertThat(expeditionStationHandler.getLastProcessedOp()).isEqualTo(10);
        long firstTimestamp = expeditionStationHandler.getLastProcessedOpTimestamp();
        assertThat(firstTimestamp).isGreaterThan(0L);

        // Verifica que o pedido OP=10 foi persistido na posição 3 da Expedição (storageId = 2) sem RestTemplate (BUG-02)
        assertThat(blockRepository.findByStorageIdAndPosition(2L, 3))
                .isPresent()
                .get()
                .extracting(b -> b.getProductionOrder().getProductionOrder())
                .isEqualTo(10L);

        // Simula próximo ciclo de 800ms com o mesmo opGuardadoExpedicao = 10: não deve reprocessar como novo pedido (BUG-05)
        Thread.sleep(10);
        expeditionStationHandler.handle(plcExp, db9Exp);
        assertThat(expeditionStationHandler.getLastProcessedOpTimestamp()).isEqualTo(firstTimestamp);
    }
}
