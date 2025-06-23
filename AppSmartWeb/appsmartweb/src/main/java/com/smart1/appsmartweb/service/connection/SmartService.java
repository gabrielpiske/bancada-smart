package com.smart1.appsmartweb.service.connection;

import java.util.HashMap;

import org.springframework.stereotype.Service;
import java.util.Map;

@Service
public class SmartService {
    

    public void clpEstoque(String ip, byte[] dados){

    }

    public void clpProcesso(String ip, byte[] dados){

    }

    public void clpMontagem(String ip, byte[] dados){

    }

    public void clpExpedicao(String ip, byte[] dados){

    }

    public Map<String, Boolean> pingPlcs(Map<String, String> ips) {
    Map<String, Boolean> status = new HashMap<>();

    for (Map.Entry<String, String> entry : ips.entrySet()) {
        String nome = entry.getKey();
        String ip = entry.getValue();

        try {
            boolean conectado = PlcConnectionManager.pingIp(ip); // supondo que tenha esse método
            status.put(nome, conectado);
        } catch (Exception e) {
            status.put(nome, false); // falha no ping
        }
    }

    return status;
}
}
