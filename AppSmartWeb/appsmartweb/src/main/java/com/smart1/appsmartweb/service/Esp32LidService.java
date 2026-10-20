package com.smart1.appsmartweb.service;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.smart1.appsmartweb.config.BancadaProperties;
import com.smart1.appsmartweb.service.connection.PlcConnectionManager;

/**
 * Cliente HTTP para o seletor de tampa controlado pelo ESP32.
 * Preserva o contrato POST application/x-www-form-urlencoded com body "pos={1|2|3}"
 * (AGENTS.md Invariante #6) e utiliza URL configurável via BancadaProperties (corrige BUG-14).
 */
@Service
public class Esp32LidService {

    private static final Logger log = LoggerFactory.getLogger(Esp32LidService.class);

    private final BancadaProperties bancadaProperties;
    private final RestTemplate restTemplate = new RestTemplate();

    public Esp32LidService(BancadaProperties bancadaProperties) {
        this.bancadaProperties = bancadaProperties;
    }

    public ResponseEntity<String> moveLidPosition(int pos) {
        String url = bancadaProperties.getEsp32().getUrl();
        String requestBody = "pos=" + pos;

        if (PlcConnectionManager.isSimulationMode()) {
            log.info("[ESP32 SIMULADO] Movendo tampa para posição {} (body='{}')", pos, requestBody);
            return ResponseEntity.ok("Ok - Posição selecionada: " + pos);
        }

        log.info("Enviando comando de tampa para ESP32 ({}) com body '{}'", url, requestBody);
        try {
            HttpHeaders headers = new HttpHeaders();
            headers.setContentType(MediaType.APPLICATION_FORM_URLENCODED);
            HttpEntity<String> requestEntity = new HttpEntity<>(requestBody, headers);

            ResponseEntity<String> resposta = restTemplate.postForEntity(url, requestEntity, String.class);
            log.info("Resposta do ESP32: status={}, body={}", resposta.getStatusCode(), resposta.getBody());

            return ResponseEntity
                    .status(resposta.getStatusCode())
                    .body(resposta.getBody());
        } catch (Exception e) {
            log.error("Erro ao comunicar com ESP32 ({}): {}", url, e.getMessage());
            return ResponseEntity
                    .status(500)
                    .body("{\"error\": \"Erro ao mover tampa: " + e.getMessage() + "\"}");
        }
    }
}
