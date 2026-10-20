package com.smart1.appsmartweb;

import static org.assertj.core.api.Assertions.assertThat;

import java.util.ArrayList;
import java.util.List;

import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;

import com.smart1.appsmartweb.config.BancadaProperties;
import com.smart1.appsmartweb.controller.BlockController;
import com.smart1.appsmartweb.model.Block;
import com.smart1.appsmartweb.model.Storage;
import com.smart1.appsmartweb.repository.BlockRepository;
import com.smart1.appsmartweb.repository.StorageRepository;
import com.smart1.appsmartweb.util.DataInitializer;

@SpringBootTest
class AppsmartwebApplicationTests {

    @Autowired
    private BancadaProperties bancadaProperties;

    @Autowired
    private StorageRepository storageRepository;

    @Autowired
    private BlockRepository blockRepository;

    @Autowired
    private DataInitializer dataInitializer;

    @Autowired
    private BlockController blockController;

    @Test
    void contextLoadsAndPropertiesAreBound() {
        assertThat(bancadaProperties.getPlc().getDefaultSubnet()).isEqualTo("10.74.241");
        assertThat(bancadaProperties.getPlc().getPort()).isEqualTo(102);
        assertThat(bancadaProperties.getPlc().getPollIntervalMs()).isEqualTo(800L);
        assertThat(bancadaProperties.getPlc().getDefaultEstoqueIp()).isEqualTo("10.74.241.10");
        assertThat(bancadaProperties.getPlc().getDefaultProcessoIp()).isEqualTo("10.74.241.20");
        assertThat(bancadaProperties.getPlc().getDefaultMontagemIp()).isEqualTo("10.74.241.30");
        assertThat(bancadaProperties.getPlc().getDefaultExpedicaoIp()).isEqualTo("10.74.241.40");
        assertThat(bancadaProperties.getEsp32().getUrl()).isEqualTo("http://10.74.241.245:80/api/move_pos");
    }

    @Test
    void dataInitializerCreatesBothStoragesIndividually() {
        // Verifica que o @PostConstruct criou ambos os armazéns (1=Stock, 2=Expedition)
        assertThat(storageRepository.findById(1L))
                .isPresent()
                .get()
                .extracting(Storage::getName, Storage::getCapacity)
                .containsExactly("Stock", 28);

        assertThat(storageRepository.findById(2L))
                .isPresent()
                .get()
                .extracting(Storage::getName, Storage::getCapacity)
                .containsExactly("Expedition", 12);

        // Simula o cenário do BUG-07: deletar apenas o Storage 2 e reexecutar init()
        storageRepository.deleteById(2L);
        assertThat(storageRepository.existsById(2L)).isFalse();

        dataInitializer.init();

        assertThat(storageRepository.findById(2L))
                .isPresent()
                .get()
                .extracting(Storage::getName, Storage::getCapacity)
                .containsExactly("Expedition", 12);
    }

    @Test
    void blockControllerSaveBlockUsesStockStorageId1() {
        List<String> colors = new ArrayList<>();
        for (int i = 1; i <= 28; i++) {
            colors.add(i <= 3 ? String.valueOf(i) : "0");
        }

        String redirect = blockController.saveBlock(colors);
        assertThat(redirect).isEqualTo("redirect:/estoque");

        List<Block> savedBlocks = blockRepository.findAll();
        assertThat(savedBlocks).hasSize(3);
        assertThat(savedBlocks)
                .allMatch(b -> b.getStorageId() != null && b.getStorageId().getId() == 1L);
    }

}
