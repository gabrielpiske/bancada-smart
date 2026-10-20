package com.smart1.appsmartweb.service;

import java.util.List;
import java.util.Optional;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.data.domain.PageRequest;
import org.springframework.stereotype.Service;
import org.springframework.transaction.annotation.Transactional;

import com.smart1.appsmartweb.exception.StorageNotFoundException;
import com.smart1.appsmartweb.model.Block;
import com.smart1.appsmartweb.model.Orders;
import com.smart1.appsmartweb.model.Storage;
import com.smart1.appsmartweb.repository.BlockRepository;
import com.smart1.appsmartweb.repository.OrdersRepository;
import com.smart1.appsmartweb.repository.StorageRepository;
import com.smart1.appsmartweb.util.DataInitializer;

/**
 * Serviço transacional para gestão do inventário do Estoque (storageId = 1)
 * e da Expedição (storageId = 2).
 * Substitui as auto-chamadas HTTP quebradas via RestTemplate (corrige BUG-02).
 */
@Service
public class InventoryService {

    private static final Logger log = LoggerFactory.getLogger(InventoryService.class);

    private final BlockRepository blockRepository;
    private final StorageRepository storageRepository;
    private final OrdersRepository ordersRepository;

    public InventoryService(
            BlockRepository blockRepository,
            StorageRepository storageRepository,
            OrdersRepository ordersRepository) {
        this.blockRepository = blockRepository;
        this.storageRepository = storageRepository;
        this.ordersRepository = ordersRepository;
    }

    @Transactional
    public void addBlockToStock(int position, int color) {
        if (position < 1 || position > DataInitializer.STOCK_CAPACITY) {
            log.warn("Tentativa de adicionar bloco em posição inválida do Estoque: {}", position);
            return;
        }
        Optional<Block> existing = blockRepository.findByStorageIdAndPosition(DataInitializer.STOCK_STORAGE_ID, position);
        if (color <= 0) {
            existing.ifPresent(blockRepository::delete);
            log.info("Bloco removido do Estoque (cor=0) na posição {}", position);
            return;
        }
        Storage stock = storageRepository.findById(DataInitializer.STOCK_STORAGE_ID)
                .orElseThrow(() -> new StorageNotFoundException("Storage Estoque (id=1) não encontrado"));

        Block block = existing.orElseGet(() -> {
            Block b = new Block();
            b.setStorageId(stock);
            b.setPosition(position);
            return b;
        });
        block.setColor(color);
        block.setProductionOrder(null);
        blockRepository.save(block);
        log.info("Bloco adicionado/atualizado no Estoque: posição={}, cor={}", position, color);
    }

    @Transactional
    public void removeBlockFromStock(int position) {
        blockRepository.findByStorageIdAndPosition(DataInitializer.STOCK_STORAGE_ID, position)
                .ifPresent(block -> {
                    blockRepository.delete(block);
                    log.info("Bloco removido do Estoque na posição {}", position);
                });
    }

    @Transactional(readOnly = true)
    public int findFirstFreeStockPosition() {
        return blockRepository.findFirstFreePosition((int) DataInitializer.STOCK_STORAGE_ID);
    }

    @Transactional(readOnly = true)
    public int findFirstAvailableExpeditionPosition() {
        List<Integer> occupied = blockRepository.findOccupiedPositionsInExpedicao();
        for (int i = 1; i <= DataInitializer.EXPEDITION_CAPACITY; i++) {
            if (!occupied.contains(i)) {
                return i;
            }
        }
        return -1;
    }

    @Transactional(readOnly = true)
    public Integer getLastPositionByStorageId(Long storageId) {
        List<Integer> positions = blockRepository.findPositionByStorageIdIdOrderByIdDesc(
                storageId, PageRequest.of(0, 1));
        return positions.isEmpty() ? null : positions.get(0);
    }

    @Transactional
    public void confirmOrderInExpedition(int position, int opNumber) {
        if (position < 1 || position > DataInitializer.EXPEDITION_CAPACITY) {
            log.warn("Posição de expedição inválida ao confirmar OP {}: {}", opNumber, position);
            return;
        }
        if (opNumber <= 0) {
            removeOrderFromExpedition(position);
            return;
        }

        Storage expedition = storageRepository.findById(DataInitializer.EXPEDITION_STORAGE_ID)
                .orElseThrow(() -> new StorageNotFoundException("Storage Expedição (id=2) não encontrado"));

        Orders order = ordersRepository.findFirstByProductionOrder((long) opNumber)
                .orElseGet(() -> {
                    Orders newOrder = new Orders();
                    newOrder.setProductionOrder((long) opNumber);
                    return ordersRepository.save(newOrder);
                });

        Block block = blockRepository.findByStorageIdAndPosition(DataInitializer.EXPEDITION_STORAGE_ID, position)
                .orElseGet(() -> {
                    Block b = new Block();
                    b.setStorageId(expedition);
                    b.setPosition(position);
                    return b;
                });
        block.setProductionOrder(order);
        blockRepository.save(block);
        log.info("Pedido OP={} confirmado na Expedição na posição {}", opNumber, position);
    }

    @Transactional
    public void removeOrderFromExpedition(int position) {
        blockRepository.findByStorageIdAndPosition(DataInitializer.EXPEDITION_STORAGE_ID, position)
                .ifPresent(block -> {
                    blockRepository.delete(block);
                    log.info("Pedido removido da Expedição na posição {}", position);
                });
    }
}
