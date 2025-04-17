package com.smart1.appsmartweb.controller;

import java.util.ArrayList;
import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.data.domain.Sort;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

import com.smart1.appsmartweb.model.Block;
import com.smart1.appsmartweb.repository.BlockRepository;
import com.smart1.appsmartweb.repository.StorageRepository;

@Controller
public class BlockController {

    @Autowired
    private BlockRepository blockRepository;

    @Autowired
    private StorageRepository storageRepository;

    @GetMapping("/estoque")
    public String listBlocks(Model model) {
        final List<Block> listBlocks = blockRepository.findAll(Sort.by(Sort.Direction.ASC, "Position"));
        final Map<Integer, Integer> estoqueColorsByPosition = new HashMap<>();
        final Map<Integer, String> expedicaoOrdersByPosition = new HashMap<>();
        final List<Integer> estoque = new ArrayList<>();
        final List<String> expedicao = new ArrayList<>();

        for (Block block : listBlocks) {
            if (block.getStorageId().getId() == 1) {
                estoqueColorsByPosition.put(block.getPosition(), block.getColor());
            } else if (block.getStorageId().getId() == 2) {
                int position = block.getPosition();
                String newProductionOrder = block.getProductionOrders().getProductionOrder();
                if (expedicaoOrdersByPosition.containsKey(position)) {
                    String existingProductionOrders = expedicaoOrdersByPosition.get(position);
                    String updatedProductionOrders = existingProductionOrders + " " + newProductionOrder;
                    expedicaoOrdersByPosition.put(position, updatedProductionOrders);
                } else {
                    expedicaoOrdersByPosition.put(position, newProductionOrder);
                }
            }
        }

        for (int i = 1; i <= 28; i++) {
            estoque.add(estoqueColorsByPosition.getOrDefault(i, 0));
        }

        for (int i = 1; i <= 12; i++) {
            expedicao.add(expedicaoOrdersByPosition.getOrDefault(i, ""));
        }

        model.addAttribute("estoque", estoque);
        model.addAttribute("expedicao", expedicao);
        model.addAttribute("editMode", false);

        return "index";
    }

    @GetMapping("estoque/editar")
    public String editBlocks(Model model) {
        final List<Block> listBlocks = blockRepository.findAll(Sort.by(Sort.Direction.ASC, "Position"));
        final Map<Integer, Integer> estoqueColorsByPosition = new HashMap<>();
        final Map<Integer, String> expedicaoOrdersByPosition = new HashMap<>();
        final List<Integer> estoque = new ArrayList<>();
        final List<String> expedicao = new ArrayList<>();

        for (Block block : listBlocks) {
            if (block.getStorageId().getId() == 1) {
                estoqueColorsByPosition.put(block.getPosition(), block.getColor());
            } else if (block.getStorageId().getId() == 2) {
                int position = block.getPosition();
                String newProductionOrder = block.getProductionOrders().getProductionOrder();
                if (expedicaoOrdersByPosition.containsKey(position)) {
                    String existingProductionOrders = expedicaoOrdersByPosition.get(position);
                    String updatedProductionOrders = existingProductionOrders + ", " + newProductionOrder;
                    expedicaoOrdersByPosition.put(position, updatedProductionOrders);
                } else {
                    expedicaoOrdersByPosition.put(position, newProductionOrder);
                }
            }
        }

        for (int i = 1; i <= 28; i++) {
            estoque.add(estoqueColorsByPosition.getOrDefault(i, 0));
        }

        for (int i = 1; i <= 12; i++) {
            expedicao.add(expedicaoOrdersByPosition.getOrDefault(i, ""));
        }

        model.addAttribute("estoque", estoque);
        model.addAttribute("expedicao", expedicao);
        model.addAttribute("editMode", true);

        return "index";
    }

    @PostMapping("estoque/editar")
    public String saveBlock(@RequestParam List<String> listBlocks) {
        Long storageId = storageRepository.findAll().get(0).getId();

        List<Block> existingBlocks = blockRepository.findAll();
        Map<Integer, Block> blocksByPosition = new HashMap<>();
        for (Block block : existingBlocks) {
            if (block.getStorageId() != null && block.getStorageId().getId() == storageId) {
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
                    block.setStorageId(storageRepository.findAll().get(0));
                    block.setPosition(position);
                }
                block.setColor(color);
                blockRepository.save(block);
            }
        }

        return "redirect:/estoque";
    }

}
