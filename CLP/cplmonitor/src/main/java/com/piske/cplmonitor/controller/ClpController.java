package com.piske.cplmonitor.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.servlet.mvc.method.annotation.SseEmitter;

import com.piske.cplmonitor.model.Tag;
import com.piske.cplmonitor.model.TagWriteRequest;
import com.piske.cplmonitor.service.ClpSimulatorService;
import com.piske.cplmonitor.service.PlcConnector;


@Controller
public class ClpController {

    @Autowired
    private ClpSimulatorService simulatorService;

    @GetMapping("/")
    public String index(Model model) {
        model.addAttribute("tag", new TagWriteRequest());
        return "index";
    }

    @GetMapping("/clp-data-stream")

    public SseEmitter streamClpData() {
        return simulatorService.subscribe();
    }

    @GetMapping("/write-tag")
    public String showWriteForm(Model model) {
        model.addAttribute("tag", new Tag());
        return "clp-write-fragment"; 
    }

    @PostMapping("/write-tag")
    public String writeTag(@ModelAttribute Tag tag, Model model) {
        try {
            PlcConnector plc = new PlcConnector(tag.getIp().trim(), tag.getPort());
            plc.connect();

            boolean success = false;
            
            switch (tag.getType().toUpperCase()) {
                case "STRING":
                    success = plc.writeString(tag.getDb(), tag.getOffset(), tag.getSize(), tag.getValue().trim());
                    break;
                case "BLOCK":
                    byte[] bytes = PlcConnector.hexStringToByteArray(tag.getValue().trim());
                    success = plc.writeBlock(tag.getDb(), tag.getOffset(), tag.getSize(), bytes);
                    break;
                case "FLOAT":
                    success = plc.writeFloat(tag.getDb(), tag.getOffset(), Float.parseFloat(tag.getValue().trim()));
                    break;
                case "INTEGER":
                    success = plc.writeInt(tag.getDb(), tag.getOffset(), Integer.parseInt(tag.getValue().trim()));
                    break;
                case "BYTE":
                    success = plc.writeByte(tag.getDb(), tag.getOffset(), Byte.parseByte(tag.getValue().trim()));
                    break;
                case "BIT":
                    if (tag.getBitNumber() == null) {
                        throw new IllegalArgumentException("Bit Number é obrigatório para tipo BIT");
                    }
                    success = plc.writeBit(tag.getDb(), tag.getOffset(), tag.getBitNumber(), 
                                         Boolean.parseBoolean(tag.getValue().trim()));
                    break;
                default:
                    throw new IllegalArgumentException("Tipo não suportado: " + tag.getType());
            }

            plc.disconnect();

            if (success) {
                model.addAttribute("mensagem", "Escrita no CLP realizada com sucesso!");
            } else {
                model.addAttribute("erro", "Erro de escrita no CLP!");
            }
        } catch (Exception ex) {
            model.addAttribute("erro", "Erro: " + ex.getMessage());
        }

        return "clp-write-fragment"; // ou o nome da sua página que contém o fragmento
    }

    @GetMapping("/fragmento-formulario")
    public String carregarFragmentoFormulario(Model model) {
        model.addAttribute("tag", new TagWriteRequest());
        return "fragments/formulario :: clp-write-fragment";
    }

     @PostMapping("/update-stock")
     public String updateStock() {
        simulatorService.updateStock();
        return "redirect:/fragmento-formulario";
     }

     @PostMapping("/update-expedition")
     public String updateExpedition() {
        simulatorService.updateExpedition();
        return "redirect:/fragmento-formulario";
     }
     
     @PostMapping("/update")
     public String update() {
        simulatorService.startSimulation();
        return "redirect:/fragmento-formulario";
     }
}