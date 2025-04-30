package com.smart1.appsmartweb.controller;

import java.util.HashMap;
import java.util.List;
import java.util.Map;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;

import com.smart1.appsmartweb.model.Block;
import com.smart1.appsmartweb.repository.BlockRepository;
import com.smart1.appsmartweb.service.PedidoTesteService;

import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestParam;

@Controller
public class MainController {

    @Autowired
    private BlockRepository blockRepository;

    @Autowired
    private PedidoTesteService pedidoTesteService;

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

    @PostMapping("/pedidoTeste")
    public String peditoTeste(@RequestParam Map<String, String> formData) {
        pedidoTesteService.enviarPedidoTeste(formData);
        return "redirect:/loja";
    }

}
