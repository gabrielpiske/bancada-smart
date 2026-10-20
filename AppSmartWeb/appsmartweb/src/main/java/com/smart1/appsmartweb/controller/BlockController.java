package com.smart1.appsmartweb.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.transaction.annotation.Transactional;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.smart1.appsmartweb.exception.StorageNotFoundException;
import com.smart1.appsmartweb.model.Block;
import com.smart1.appsmartweb.model.Storage;
import com.smart1.appsmartweb.repository.BlockRepository;
import com.smart1.appsmartweb.repository.StorageRepository;
import com.smart1.appsmartweb.util.DataInitializer;

@Controller
public class BlockController {

    private static final Logger log = LoggerFactory.getLogger(BlockController.class);

    @Autowired
    private BlockRepository blockRepository;

    @Autowired
    private StorageRepository storageRepository;

    @GetMapping("/estoque")
    public String listBlocks(Model model) {
        populateStockModel(model, false, " ");
        return "stock";
    }

    @GetMapping("estoque/editar")
    public String editBlocks(Model model) {
        populateStockModel(model, true, ", ");
        return "stock";
    }

    private void populateStockModel(Model model, boolean editMode, String orderSeparator) {
        final List<Block> listBlocks = blockRepository.findAll(Sort.by(Sort.Direction.ASC, "position"));
        final Map<Integer, Integer> estoqueColorsByPosition = new HashMap<>();
        final Map<Integer, String> expedicaoOrdersByPosition = new HashMap<>();
        final List<Integer> estoque = new ArrayList<>(DataInitializer.STOCK_CAPACITY);
        final List<String> expedicao = new ArrayList<>(DataInitializer.EXPEDITION_CAPACITY);

        for (Block block : listBlocks) {
            if (block.getStorageId() == null || block.getStorageId().getId() == null) {
                continue;
            }
            long storageId = block.getStorageId().getId();
            if (storageId == DataInitializer.STOCK_STORAGE_ID) {
                estoqueColorsByPosition.put(block.getPosition(), block.getColor());
            } else if (storageId == DataInitializer.EXPEDITION_STORAGE_ID && block.getProductionOrder() != null) {
                int position = block.getPosition();
                Long newProductionOrder = block.getProductionOrder().getProductionOrder();
                if (newProductionOrder != null) {
                    if (expedicaoOrdersByPosition.containsKey(position)) {
                        String existing = expedicaoOrdersByPosition.get(position);
                        expedicaoOrdersByPosition.put(position, existing + orderSeparator + newProductionOrder);
                    } else {
                        expedicaoOrdersByPosition.put(position, newProductionOrder.toString());
                    }
                }
            }
        }

        for (int i = 1; i <= DataInitializer.STOCK_CAPACITY; i++) {
            estoque.add(estoqueColorsByPosition.getOrDefault(i, 0));
        }

        for (int i = 1; i <= DataInitializer.EXPEDITION_CAPACITY; i++) {
            expedicao.add(expedicaoOrdersByPosition.getOrDefault(i, ""));
        }

        model.addAttribute("estoque", estoque);
        model.addAttribute("expedicao", expedicao);
        model.addAttribute("editMode", editMode);
    }

    @PostMapping("estoque/editar")
    @Transactional
    public String saveBlock(@RequestParam List<String> listBlocks) {
        Storage stockStorage = storageRepository.findById(DataInitializer.STOCK_STORAGE_ID)
                .orElseThrow(() -> new StorageNotFoundException("Armazém de Estoque (id=1) não encontrado"));

        List<Block> existingBlocks = blockRepository.findAll();
        Map<Integer, Block> blocksByPosition = new HashMap<>();
        for (Block block : existingBlocks) {
            if (block.getStorageId() != null
                    && Long.valueOf(DataInitializer.STOCK_STORAGE_ID).equals(block.getStorageId().getId())) {
                blocksByPosition.put(block.getPosition(), block);
            }
        }

        for (int i = 0; i < listBlocks.size(); i++) {
            int position = i + 1;
            int color = Integer.parseInt(listBlocks.get(i));

            Block block = blocksByPosition.get(position);
            if (color == 0) {
                // Deletar blocos cuja cor foi enviada como 0
                if (block != null) {
                    blockRepository.delete(block);
                }
            } else {
                // Atualizar ou criar bloco se a cor for diferente de 0
                if (block == null) {
                    block = new Block();
                    block.setStorageId(stockStorage);
                    block.setPosition(position);
                }
                block.setColor(color);
                blockRepository.save(block);
            }
        }

        log.info("Estoque atualizado manualmente via interface web ({} posições processadas)", listBlocks.size());
        return "redirect:/estoque";
    }

}
