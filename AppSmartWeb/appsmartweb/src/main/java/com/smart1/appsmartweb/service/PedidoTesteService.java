package com.smart1.appsmartweb.service;

import java.nio.ByteBuffer;
import java.util.Map;

import org.springframework.stereotype.Service;

import com.smart1.appsmartweb.util.PlcConnector;

@Service
public class PedidoTesteService {

    public void enviarPedidoTeste(Map<String, String> formData) {
        // determinar quantos blocos foram preenchidos
        int totalBlocos = 0;
        for (int i = 1; i <= 3; i++) {
            if (formData.containsKey("block-color-" + i) && !formData.get("block-color-" + i).isEmpty()) {
                totalBlocos++;
            }
        }

        ByteBuffer bfPedido = ByteBuffer.allocate(60);

        // Processar cada bloco
        for (int blocoNum = 1; blocoNum <= 3; blocoNum++) {
            String blockColorKey = "block-color-" + blocoNum;

            if (formData.containsKey(blockColorKey) && !formData.get(blockColorKey).isEmpty()) {
                // Bloco preenchido
                int blockColor = Integer.parseInt(formData.get(blockColorKey));

                // Cores das lâminas (padrão 0 se não selecionado)
                int lamina1Color = getValueOrDefault(formData, "l1-color-" + blocoNum, 0);
                int lamina2Color = getValueOrDefault(formData, "l2-color-" + blocoNum, 0);
                int lamina3Color = getValueOrDefault(formData, "l3-color-" + blocoNum, 0);

                // Padrões das lâminas (padrão 0 se não selecionado)
                int padrao1 = getValueOrDefault(formData, "l1-pattern-" + blocoNum, 0);
                int padrao2 = getValueOrDefault(formData, "l2-pattern-" + blocoNum, 0);
                int padrao3 = getValueOrDefault(formData, "l3-pattern-" + blocoNum, 0);

                // Posição no estoque (simplificado - pode precisar de lógica mais complexa)
                int posicaoEstoque = 1; // Você pode querer calcular isso dinamicamente

                // Escrever dados do bloco
                bfPedido.putShort((short) blockColor); // Cor_Andar_N
                bfPedido.putShort((short) posicaoEstoque); // Posicao_Estoque_Andar_N
                bfPedido.putShort((short) lamina1Color); // Cor_Lamina_1_Andar_N
                bfPedido.putShort((short) lamina2Color); // Cor_Lamina_2_Andar_N
                bfPedido.putShort((short) lamina3Color); // Cor_Lamina_3_Andar_N
                bfPedido.putShort((short) padrao1); // Padrao_Lamina_1_Andar_N
                bfPedido.putShort((short) padrao2); // Padrao_Lamina_2_Andar_N
                bfPedido.putShort((short) padrao3); // Padrao_Lamina_3_Andar_N
                bfPedido.putShort((short) 1); // Processamento_Andar_N (1 = ativo)
            } else {
                // Bloco não preenchido
                for (int i = 0; i < 9; i++) {
                    bfPedido.putShort((short) 0);
                }
            }
        }

        // Número do pedido (pode ser gerado dinamicamente)
        bfPedido.putShort((short) 205); // Numero_Pedido
        bfPedido.putShort((short) totalBlocos); // Andares (quantidade de blocos)
        bfPedido.putShort((short) 12); // Posicao_Expedicao

        // Restante do código de comunicação com o PLC permanece o mesmo
        PlcConnector plc = new PlcConnector("10.74.241.10", 102);
        try {
            plc.connect();

            if (plc.writeBlock(9, 2, 60, bfPedido.array())) {
                System.out.println("Envio do pedido realizado com sucesso");

                plc.writeBit(9, 64, 0, false); // RecebidoEstoque
                plc.writeBit(9, 64, 1, false); // IniciarGuardar
                plc.writeBit(9, 62, 0, false); // IniciarPedido

                // Inicia pedido
                plc.writeBit(9, 62, 0, true); // IniciarPedido

                boolean pedidoProcessado = false;
                int tentativas = 0;
                while (!pedidoProcessado && tentativas < 10) {
                    Thread.sleep(200);
                    pedidoProcessado = plc.readBit(9, 100, 0); // PedidoRecebido
                    tentativas++;
                }

                Thread.sleep(100);

                if (pedidoProcessado) {
                    plc.writeBit(9, 62, 0, false); // desligar sinal do inicio

                    limparBlocoRetirado(plc, formData);

                    plc.writeBit(9, 0, 0, true); // RecebidoOP
                } else {
                    System.out.println("Timeout: Pedido não foi processado");
                }

            } else {
                System.out.println("Falha no envio do pedido.");
            }
        } catch (Exception e1) {
            e1.printStackTrace();
        } finally {
            try {
                if (plc != null) {
                    plc.disconnect();
                }
            } catch (Exception e1) {
                e1.printStackTrace();
            }
        }
    }

    private int getValueOrDefault(Map<String, String> formData, String key, int defaultValue) {
        if (formData.containsKey(key) && !formData.get(key).isEmpty()) {
            return Integer.parseInt(formData.get(key));
        }
        return defaultValue;
    }

    private void limparBlocoRetirado(PlcConnector plc, Map<String, String> formData) throws Exception {
        // Identificar qual bloco foi retirado
        for (int blocoNum = 1; blocoNum <= 3; blocoNum++) {
            if (formData.containsKey("block-color-" + blocoNum) &&
                    !formData.get("block-color-" + blocoNum).isEmpty()) {

                // Calcular offset do bloco na memória (18 bytes por bloco)
                int offset = (blocoNum - 1) * 18;

                // Criar buffer de limpeza (todos zeros)
                byte[] zeros = new byte[18];

                // Sobrescrever a área de memória do bloco
                // Usando a versão com 4 parâmetros:
                plc.writeBlock(9, offset, 18, zeros);

                System.out.println("Bloco " + blocoNum + " removido da memória do CLP (offset: " + offset + ")");
            }
        }
    }
}