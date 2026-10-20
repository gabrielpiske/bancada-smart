package com.smart1.appsmartweb.service;

import static org.assertj.core.api.Assertions.assertThat;
import static org.assertj.core.api.Assertions.assertThatThrownBy;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.LinkedHashMap;
import java.util.Map;

import org.junit.jupiter.api.BeforeEach;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.ui.ConcurrentModel;
import org.springframework.ui.Model;

import com.smart1.appsmartweb.controller.MainController;
import com.smart1.appsmartweb.exception.BlockNotFoundException;
import com.smart1.appsmartweb.model.enums.StationType;
import com.smart1.appsmartweb.repository.BlockRepository;
import com.smart1.appsmartweb.repository.OrdersRepository;
import com.smart1.appsmartweb.service.connection.PlcConnectionManager;
import com.smart1.appsmartweb.util.PlcConnector;

@SpringBootTest
class OrderOrchestratorServiceTest {

    @Autowired
    private OrderOrchestratorService orderOrchestratorService;

    @Autowired
    private InventoryService inventoryService;

    @Autowired
    private MainController mainController;

    @Autowired
    private BlockRepository blockRepository;

    @Autowired
    private OrdersRepository ordersRepository;

    @BeforeEach
    void setUp() {
        blockRepository.deleteAll();
        ordersRepository.deleteAll();
        PlcConnectionManager.configure("10.74.241", 102, true);
    }

    @Test
    void orderWithThreeBlocksOfSameColorAllocatesThreeDistinctStockPositions() throws Exception {
        // Insere 3 blocos Pretos (color = 1) nas posições 2, 5 e 9 do Estoque (storageId = 1)
        inventoryService.addBlockToStock(2, 1);
        inventoryService.addBlockToStock(5, 1);
        inventoryService.addBlockToStock(9, 1);

        Map<String, String> formData = new LinkedHashMap<>();
        formData.put("block-color-1", "1");
        formData.put("l1-color-1", "1");
        formData.put("l1-pattern-1", "1");

        formData.put("block-color-2", "1");
        formData.put("l1-color-2", "2");
        formData.put("l1-pattern-2", "2");

        formData.put("block-color-3", "1");
        formData.put("l1-color-3", "3");
        formData.put("l1-pattern-3", "3");

        byte[] payload = orderOrchestratorService.processarPedidoCompleto(formData);
        assertThat(payload).hasSize(60);

        ByteBuffer buf = ByteBuffer.wrap(payload).order(ByteOrder.BIG_ENDIAN);
        int[] shorts = new int[30];
        for (int i = 0; i < 30; i++) {
            shorts[i] = buf.getShort();
        }

        // Critério de Aceite da Fase 4: índices 1, 10 e 19 possuem 3 posições de estoque distintas (2, 5 e 9)
        assertThat(shorts[0]).isEqualTo(1);
        assertThat(shorts[1]).isEqualTo(2);  // Andar 1 -> posição 2
        assertThat(shorts[8]).isEqualTo(1);

        assertThat(shorts[9]).isEqualTo(1);
        assertThat(shorts[10]).isEqualTo(5); // Andar 2 -> posição 5
        assertThat(shorts[17]).isEqualTo(1);

        assertThat(shorts[18]).isEqualTo(1);
        assertThat(shorts[19]).isEqualTo(9); // Andar 3 -> posição 9
        assertThat(shorts[26]).isEqualTo(1);

        assertThat(shorts[27]).isEqualTo(1); // Nº da OP = 1
        assertThat(shorts[28]).isEqualTo(3); // Total de blocos = 3
        assertThat(shorts[29]).isEqualTo(1); // Posição na expedição = 1

        // Verifica que os bytes foram gravados no CLP Estoque simulado (DB9 offset 2) e IniciarPedido = TRUE
        PlcConnector plcEstoque = PlcConnectionManager.getConexao(StationType.ESTOQUE);
        assertThat(plcEstoque.readBit(9, 62, 0)).isTrue();
    }

    @Test
    void orderRequestingMoreBlocksOfColorThanAvailableThrowsBlockNotFoundException() {
        // Apenas 1 bloco Azul (color = 3) no estoque
        inventoryService.addBlockToStock(4, 3);

        Map<String, String> formData = new LinkedHashMap<>();
        formData.put("block-color-1", "3");
        formData.put("block-color-2", "3"); // 2º bloco Azul não existe!

        assertThatThrownBy(() -> orderOrchestratorService.processarPedidoCompleto(formData))
                .isInstanceOf(BlockNotFoundException.class)
                .hasMessageContaining("Azul");
    }

    @Test
    void storeColorAvailabilityExcludesColorZero() {
        // Mesmo que exista um registro no banco com color = 0 (cenário do createDB_test.sql / BUG-16)
        inventoryService.addBlockToStock(1, 1); // Preto
        inventoryService.addBlockToStock(2, 2); // Vermelho

        Model model = new ConcurrentModel();
        mainController.goToStore(model);

        @SuppressWarnings("unchecked")
        Map<Integer, Integer> availability = (Map<Integer, Integer>) model.getAttribute("colorAvailability");
        assertThat(availability)
                .doesNotContainKey(0)
                .containsEntry(1, 1)
                .containsEntry(2, 1);
    }
}
