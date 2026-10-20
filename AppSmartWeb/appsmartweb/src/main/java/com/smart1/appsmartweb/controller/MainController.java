package com.smart1.appsmartweb.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;

import com.smart1.appsmartweb.model.Block;
import com.smart1.appsmartweb.repository.BlockRepository;
import com.smart1.appsmartweb.service.Esp32LidService;
import com.smart1.appsmartweb.service.OrderOrchestratorService;
import com.smart1.appsmartweb.util.DataInitializer;

@Controller
public class MainController {

    private static final Logger log = LoggerFactory.getLogger(MainController.class);

    private final BlockRepository blockRepository;
    private final OrderOrchestratorService orderOrchestratorService;
    private final Esp32LidService esp32LidService;

    public MainController(
            BlockRepository blockRepository,
            OrderOrchestratorService orderOrchestratorService,
            Esp32LidService esp32LidService) {
        this.blockRepository = blockRepository;
        this.orderOrchestratorService = orderOrchestratorService;
        this.esp32LidService = esp32LidService;
    }

    @GetMapping("/")
    public String goToMain() {
        return "main";
    }

    @GetMapping("/loja")
    public String goToStore(Model model) {
        List<Block> listBlocks = blockRepository.findAll();
        Map<Integer, Integer> colorAvailability = new HashMap<>();

        for (Block block : listBlocks) {
            // Filtra apenas blocos no Estoque (storageId = 1), disponíveis (productionOrder == null)
            // e com cor válida (1=Preto, 2=Vermelho, 3=Azul), ignorando cor 0 (corrige BUG-16)
            if (block.getStorageId() != null
                    && Long.valueOf(DataInitializer.STOCK_STORAGE_ID).equals(block.getStorageId().getId())
                    && block.getProductionOrder() == null
                    && block.getColor() >= 1 && block.getColor() <= 3) {
                int color = block.getColor();
                colorAvailability.put(color, colorAvailability.getOrDefault(color, 0) + 1);
            }
        }

        model.addAttribute("colorAvailability", colorAvailability);
        return "store";
    }

    @GetMapping("/view")
    public String goToView() {
        return "view";
    }

    @GetMapping("/monitoramento")
    public String goToMonitoring() {
        return "monitor";
    }

    @PostMapping("/pedidoTeste")
    public String peditoTeste(@RequestParam Map<String, String> formData) {
        try {
            orderOrchestratorService.processarPedidoCompleto(formData);
            return "redirect:/loja?success";
        } catch (Exception e) {
            log.warn("Erro ao processar pedido em /pedidoTeste: {}", e.getMessage());
            return "redirect:/loja?error=" + e.getMessage();
        }
    }

    @PostMapping("/api/moverTampa")
    @ResponseBody
    public ResponseEntity<String> moverTampa(@RequestParam("pos") int pos) {
        return esp32LidService.moveLidPosition(pos);
    }

}
