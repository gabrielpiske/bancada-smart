package com.smart1.appsmartweb.controller;

import java.util.HashMap;
import java.util.Map;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.smart1.appsmartweb.service.connection.SmartService;

@RestController
public class SmartServiceController {

    private final SmartService smartService;

    public SmartServiceController(SmartService smartService) {
        this.smartService = smartService;
    }

    @GetMapping("/api/estado")
    public Map<String, Object> getEstadoAtual() {
        Map<String, Object> estado = new HashMap<>();

        estado.put("recebidoOpEst", smartService.recebidoOpEst);
        estado.put("finishOPEst", smartService.finishOPEst);

        return estado;
    }
}
