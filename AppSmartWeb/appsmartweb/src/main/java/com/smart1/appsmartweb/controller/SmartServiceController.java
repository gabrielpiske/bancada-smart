package com.smart1.appsmartweb.controller;

import java.util.Map;

import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.smart1.appsmartweb.service.station.BancadaStateStore;

/**
 * Endpoint REST de monitoramento em tempo real do estado completo da Bancada Smart (corrige BUG-08).
 */
@RestController
public class SmartServiceController {

    private final BancadaStateStore stateStore;

    public SmartServiceController(BancadaStateStore stateStore) {
        this.stateStore = stateStore;
    }

    @GetMapping("/api/estado")
    public Map<String, Object> getEstadoAtual() {
        return stateStore.toStateMap();
    }
}
