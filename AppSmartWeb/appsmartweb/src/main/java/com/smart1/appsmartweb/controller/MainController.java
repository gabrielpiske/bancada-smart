package com.smart1.appsmartweb.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.client.RestTemplate;

import com.smart1.appsmartweb.model.Block;
import com.smart1.appsmartweb.repository.BlockRepository;
import com.smart1.appsmartweb.service.connection.SmartService;
import com.smart1.appsmartweb.service.entities.PedidoTesteService;

@Controller
public class MainController {

    private BlockRepository blockRepository;
    private PedidoTesteService pedidoTesteService;
    private SmartService smartService;

    public MainController(BlockRepository blockRepository,
            PedidoTesteService pedidoTesteService,
            SmartService smartService) {
        this.blockRepository = blockRepository;
        this.pedidoTesteService = pedidoTesteService;
        this.smartService = smartService;
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
            if (block.getStorageId().getId() == 1) {
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
        /*
         * pedidoTesteService.enviarPedidoTeste(formData);
         * return "redirect:/loja";
         */

        try {
            smartService.processarPedidoCompleto(formData);
            return "redirect:/loja?success";
        } catch (Exception e) {
            return "redirect:/loja?error=" + e.getMessage();
        }
    }

    @GetMapping("/api/moverTampa")
    @ResponseBody
    public ResponseEntity<String> moverTampa(@RequestParam("pos") int pos) {
        String url = "http://10.74.241.245:80/api/move_pos?pos=" + pos;

        try {
            RestTemplate restTemplate = new RestTemplate();
            ResponseEntity<String> resposta = restTemplate.getForEntity(url, String.class);

            return ResponseEntity
                    .status(resposta.getStatusCode())
                    .body(resposta.getBody());
        } catch (Exception e) {
            return ResponseEntity
                    .status(500)
                    .body("{\"error\": \"Erro ao mover tampa: " + e.getMessage() + "\"}");
        }
    }

}
