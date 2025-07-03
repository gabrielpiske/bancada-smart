package com.smart1.appsmartweb.service.connection;

import java.nio.ByteBuffer;
import java.nio.ByteOrder;
import java.util.HashMap;
import java.util.HashSet;
import java.util.List;
import java.util.Map;
import java.util.Optional;
import java.util.Set;
import java.util.concurrent.ConcurrentHashMap;

import org.springframework.http.HttpEntity;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.smart1.appsmartweb.model.Block;
import com.smart1.appsmartweb.model.Orders;
import com.smart1.appsmartweb.model.Storage;
import com.smart1.appsmartweb.repository.BlockRepository;
import com.smart1.appsmartweb.repository.OrdersRepository;
import com.smart1.appsmartweb.repository.StorageRepository;
import com.smart1.appsmartweb.util.PlcConnector;

import jakarta.transaction.Transactional;

@Service
public class SmartService {

    private final BlockRepository blockRepository;
    private final OrdersRepository ordersRepository;
    private final StorageRepository storageRepository;

    public SmartService(BlockRepository blockRepository,
            OrdersRepository ordersRepository,
            StorageRepository storageRepository) {
        this.blockRepository = blockRepository;
        this.ordersRepository = ordersRepository;
        this.storageRepository = storageRepository;
    }

    // Variáveis globais do programa
    public static boolean readOnly = false;
    public static boolean aux_expedicao = false; // Aux Expedição

    public static boolean pedidoEmCurso = false;

    public static byte statusProducao = 0;

    public static byte statusEstoque = 0;
    public static byte statusProcesso = 0;
    public static byte statusMontagem = 0;
    public static byte statusExpedicao = 0;

    public static int posicaoEstoqueSolicitada = 0;
    public static int posicaoExpedicaoSolicitada = 0;

    public static boolean blockFinished = false;

    // Variáveis de cada estação
    // ********************** Estoque ***************************
    // ----------------------- NodeToPlc ------------------------
    /*---- StatusOP -------*/
    public boolean recebidoOpEst = false;

    /*---- InformacaoPedido -------*/
    // InfoPedido
    public int cor_Andar_1 = 0;
    public int posicao_Estoque_Andar_1 = 0;
    public int cor_Lamina_1_Andar_1 = 0;
    public int cor_Lamina_2_Andar_1 = 0;
    public int cor_Lamina_3_Andar_1 = 0;
    public int padrao_Lamina_1_Andar_1 = 0;
    public int padrao_Lamina_2_Andar_1 = 0;
    public int padrao_Lamina_3_Andar_1 = 0;
    public int processamento_Andar_1 = 0;

    public int cor_Andar_2 = 0;
    public int posicao_Estoque_Andar_2 = 0;
    public int cor_Lamina_1_Andar_2 = 0;
    public int cor_Lamina_2_Andar_2 = 0;
    public int cor_Lamina_3_Andar_2 = 0;
    public int padrao_Lamina_1_Andar_2 = 0;
    public int padrao_Lamina_2_Andar_2 = 0;
    public int padrao_Lamina_3_Andar_2 = 0;
    public int processamento_Andar_2 = 0;

    public int cor_Andar_3 = 0;
    public int posicao_Estoque_Andar_3 = 0;
    public int cor_Lamina_1_Andar_3 = 0;
    public int cor_Lamina_2_Andar_3 = 0;
    public int cor_Lamina_3_Andar_3 = 0;
    public int padrao_Lamina_1_Andar_3 = 0;
    public int padrao_Lamina_2_Andar_3 = 0;
    public int padrao_Lamina_3_Andar_3 = 0;
    public int processamento_Andar_3 = 0;

    public int numeroPedidoEst = 0;
    public int andares = 0;
    public int posicaoExpedicaoEst = 0;

    public boolean iniciarPedido = false;

    /*---- GerenciamentoEstoque -------*/
    public boolean recebidoEstoque = false;
    public boolean iniciarGuardarEst = false;
    public int posicaoGuardarEst = 0;

    /*---- PosicoesOcupadas -------*/
    public byte[] posicoesOcupadas = new byte[28];

    // ----------------------- PlcToNode ------------------------
    /*---- StatusOP ------------------*/
    public int numeroOPEst = 0;
    public boolean cancelOPEst = false;
    public boolean finishOPEst = false;
    public boolean startOPEst = false;

    /*---- StatusEstacao -------------*/
    public boolean ocupadoEst = false;
    public boolean aguardandoEst = false;
    public boolean manualEst = false;
    public boolean emergenciaEst = false;

    /*---- GerenciamentoEstoque ------*/
    public boolean pedirPosicaoEst = false;
    public int posicaoEstoque = 0;
    public boolean adicionarEstoque = false;
    public boolean removerEstoque = false;
    public boolean retornoEstoqueCheio = false;
    public int corGuardarEstoque = 0;

    /*---- RealidadeAumentada */
    public boolean xEmergenciaAtivadaEst = false;
    public boolean xComutadorAutomaticoEst = false;
    public boolean xNecessitaHomeEixoVerticalEst = false;
    public boolean xNecessitaHomeEixoGiroEst = false;
    public boolean xServoDesligadoEixoVerticalEst = false;
    public boolean xServoDesligadoEixoGiroEst = false;
    public boolean xCondicaoIniciarEst = false;

    // **********************************************************
    // ********************** Processo **************************
    // ----------------------- NodeToPlc ------------------------
    /*---- StatusOP -------*/
    public boolean recebidoOpPro = false;
    // ----------------------- PlcToNode ------------------------
    /*---- StatusOP -------*/
    public int numeroOPPro = 0;
    public boolean cancelOPPro = false;
    public boolean finishOPPro = false;
    public boolean startOPPro = false;

    /*---- StatusEstacao -------*/
    public boolean ocupadoPro = false;
    public boolean aguardandoPro = false;
    public boolean manualPro = false;
    public boolean emergenciaPro = false;

    /*---- RealidadeAumentada --------*/
    public boolean xEmergenciaAtivadaPro = false;
    public boolean xComutadorAutomaticoPro = false;
    public boolean xNecessitaHomeEixoXPro = false;
    public boolean xNecessitaHomeEixoYPro = false;
    public boolean xServoDesligadoEixoXPro = false;
    public boolean xServoDesligadoEixoYPro = false;
    public boolean xCondicaoIniciarPro = false;

    // **********************************************************
    // ********************** Monatgem **************************
    // ----------------------- NodeToPlc ------------------------
    /*---- StatusOP -------*/
    public boolean recebidoOpMon = false;
    // ----------------------- PlcToNode ------------------------
    /*---- StatusOP -------*/
    public int numeroOPMon = 0;
    public boolean cancelOPMon = false;
    public boolean finishOPMon = false;
    public boolean startOPMon = false;

    /*---- StatusEstacao -------*/
    public boolean ocupadoMon = false;
    public boolean aguardandoMon = false;
    public boolean manualMon = false;
    public boolean emergenciaMon = false;

    /*---- RealidadeAumentada --------*/
    public boolean xEmergenciaAtivadaMon = false;
    public boolean xComutadorAutomaticoMon = false;
    public boolean xCondicaoIniciarMon = false;

    // **********************************************************
    // ********************** Expedição *************************
    // ----------------------- NodeToPlc ------------------------
    /*---- StatusOP -------*/
    public boolean recebidoOpExp = false;
    /*---- GerenciamentoExpedicao -------*/
    public boolean recebidoExpedicao = false;
    public boolean iniciarGuardarExp = false;
    public int posicaoGuardarExp = 0;

    /*---- RemoverPedido -------*/
    public int[] orderExpedicao = new int[12];

    // ----------------------- PlcToNode ------------------------
    /*---- StatusOP ------------------*/
    public int numeroOPExp = 0;
    public boolean cancelOPExp = false;
    public boolean finishOPExp = false;
    public boolean startOPExp = false;

    /*---- StatusEstacao -------*/
    public boolean ocupadoExp = false;
    public boolean aguardandoExp = false;
    public boolean manualExp = false;
    public boolean emergenciaExp = false;

    /*---- GerenciamentoEstoque ------*/
    public boolean pedirPosicaoExp = false;
    public int posicaoGuardadoExpedicao = 0;
    public int posicaoRemovidoExpedicao = 0;
    public boolean adicionarExpedicao = false;
    public boolean removerExpedicao = false;
    public int opGuardadoExpedicao = 0;

    /*---- RealidadeAumentada */
    public boolean xEmergenciaAtivadaExp = false;
    public boolean xComutadorAutomaticoExp = false;
    public boolean xNecessitaHomeEixoVerticalExp = false;
    public boolean xNecessitaHomeEixoGiroExp = false;
    public boolean xNecessitaHomeEixoHorizontalExp = false;
    public boolean xServoDesligadoEixoHorizontalExp = false;
    public boolean xServoDesligadoEixoGiroExp = false;
    public boolean xServoDesligadoEixoVerticalExp = false;
    public boolean xCondicaoIniciarExp = false;

    private Map<String, List<String>> eventosCLP = new ConcurrentHashMap<>();

    @Transactional
    public void processarPedidoCompleto(Map<String, String> formData) throws Exception {
        try {
            // 1. Validação do pedido
            if (formData == null || formData.isEmpty()) {
                throw new IllegalArgumentException("Dados do formulário não podem ser nulos ou vazios");
            }

            // 2. Contar blocos preenchidos
            int totalBlocos = contarBlocosPreenchidos(formData);
            if (totalBlocos == 0) {
                throw new IllegalArgumentException("Nenhum bloco foi especificado no pedido");
            }

            // 3. Criar ordem no banco de dados
            Orders novaOrdem = criarNovaOrdemProducao();
            System.out.println("Nova ordem criada: " + novaOrdem.getProductionOrder());

            // 4. Encontrar posição livre na expedição
            int posicaoExpedicao = findFirstAvailablePosition(); // ID do armazém de expedição
            if (posicaoExpedicao == -1) {
                throw new IllegalStateException("Armazém de expedição está cheio (12 posições)");
            }

            // 5. Preparar e enviar dados para o CLP de Estoque
            byte[] dadosPedido = montarPedidoParaCLP(formData, totalBlocos, posicaoExpedicao,
                    novaOrdem.getProductionOrder());
            enviarBlocoBytesAoClp("10.74.241.10", 9, 2, dadosPedido, dadosPedido.length);

            // 6. Gravar na expedição (banco + CLP)
            gravarPedidoNaExpedicao(novaOrdem, posicaoExpedicao);

            // 7. Iniciar pedido (ativa flag no CLP Estoque)
            PlcConnector plcEstoque = PlcConnectionManager.getConexao("10.74.241.10");
            plcEstoque.writeBit(9, 62, 0, true); // IniciarPedido = TRUE

            // O processamento continua via leitura das flags (assíncrono)
        } catch (Exception e) {
            System.out.println("Erro ao processar pedido: " + e);
            throw e;
        }
    }

    private int contarBlocosPreenchidos(Map<String, String> formData) {
        int total = 0;
        for (int i = 1; i <= 3; i++) {
            if (formData.containsKey("block-color-" + i) && !formData.get("block-color-" + i).isEmpty()) {
                total++;
            }
        }
        return total;
    }

    private Orders criarNovaOrdemProducao() {
        try {

            Orders novaOrdem = new Orders();
            novaOrdem.setProductionOrder(ordersRepository.count() + 1);

            return ordersRepository.save(novaOrdem);
        } catch (Exception e) {
            System.err.println("Erro ao criar nova ordem: " + e.getMessage());
            throw e;
        }
    }

    private void gravarPedidoNaExpedicao(Orders ordem, int posicaoExpedicao) {
        // 1. Atualizar banco de dados
        Block bloco = new Block();
        bloco.setStorageId(storageRepository.findById(2L).orElseThrow());
        bloco.setPosition(posicaoExpedicao);
        bloco.setProductionOrder(ordem);
        blockRepository.save(bloco);

        // 2. Atualizar CLP Expedição
        // PlcConnector plcExp = PlcConnectionManager.getConexao("10.74.241.40");
        // int offset = 6 + (posicaoExpedicao - 1) * 2;
        // plcExp.writeInt(9, offset, ordem.getProductionOrder().intValue());
        // plcExp.writeBit(9, 2, 0, true); // RecebidoExpedicao = TRUE
    }

    private byte[] montarPedidoParaCLP(Map<String, String> formData, int totalBlocos, int posicaoExpedicao,
            Long numeroOrdem) {
        int[] dados = new int[30];
        Set<Integer> posicoesUsadas = new HashSet<>();

        for (int blocoNum = 1; blocoNum <= 3; blocoNum++) {
            String blockColorKey = "block-color-" + blocoNum;

            if (formData.containsKey(blockColorKey) && !formData.get(blockColorKey).isEmpty()) {
                int blockColor = Integer.parseInt(formData.get(blockColorKey));
                int indexBase = (blocoNum - 1) * 9;

                // Buscar posição no estoque para essa cor (storageId = 1)
                int posicaoEstoque = blockRepository.findFirstFreePositionByColorAndStorage(blockColor, 1L);

                dados[indexBase] = blockColor;
                dados[indexBase + 1] = posicaoEstoque;
                dados[indexBase + 2] = getValueOrDefault(formData, "l1-color-" + blocoNum, 0);
                dados[indexBase + 3] = getValueOrDefault(formData, "l2-color-" + blocoNum, 0);
                dados[indexBase + 4] = getValueOrDefault(formData, "l3-color-" + blocoNum, 0);
                dados[indexBase + 5] = getValueOrDefault(formData, "l1-pattern-" + blocoNum, 0);
                dados[indexBase + 6] = getValueOrDefault(formData, "l2-pattern-" + blocoNum, 0);
                dados[indexBase + 7] = getValueOrDefault(formData, "l3-pattern-" + blocoNum, 0);
                dados[indexBase + 8] = 1; // processamento_Andar_X
            }
        }

        // Número do pedido e informações adicionais
        dados[27] = numeroOrdem.intValue();
        dados[28] = totalBlocos;
        dados[29] = posicaoExpedicao;

        ByteBuffer buffer = ByteBuffer.allocate(60).order(ByteOrder.BIG_ENDIAN);
        for (int valor : dados) {
            buffer.putShort((short) valor);
        }

        return buffer.array();
    }

    public void enviarBlocoBytesAoClp(String ipClp, int db, int offset, byte[] dados, int size) throws Exception {
        // Use o IP e porta corretos do CLP de destino

        // Imprime os dados em hexadecimal antes do envio
        // System.out.println("Enviando dados para o CLP " + ipClp + " (offset: " +
        // offset + ", size: " + size + "):");
        // System.out.print("Bytes em hexadecimal: ");
        for (int i = 0; i < size; i++) {
            // System.out.printf("%02X ", dados[i]);
        }
        System.out.println(); // quebra de linha final

        PlcConnector plcConnector = PlcConnectionManager.getConexao(ipClp);
        if (plcConnector == null) {
            return;
        }
        plcConnector.writeBlock(db, offset, size, dados); // escreve no bloco de dados

    }

    public void iniciarExecucaoPedido(String ipClp) {

        // Etapas a desenvolver:
        // 1 - ATUALIZAR O PRÓXIMO NÚMERO DE PEDIDO
        // MainFrame.posExpedArray[12] = MainFrame.posExpedArray[12] + 1;
        // int orderProduction = obterProximoPedido();
        // PlcConnector plcConnector = new PlcConnector(ipClp, 102); // ajuste o IP se
        // necessário
        PlcConnector plcConnector = PlcConnectionManager.getConexao(ipClp);
        if (plcConnector == null) {
            return;
        }

        posicaoExpedicaoSolicitada = findFirstAvailablePosition();

        try {

            // Inicializa as flags da estação ESTOQUE
            // plcConnector.connect();
            plcConnector.writeBit(9, 0, 0, Boolean.parseBoolean("FALSE"));
            plcConnector.writeBit(9, 64, 0, Boolean.parseBoolean("FALSE"));
            plcConnector.writeBit(9, 64, 1, Boolean.parseBoolean("FALSE"));
            plcConnector.writeBit(9, 62, 0, Boolean.parseBoolean("FALSE"));

            // plcConnector.writeBit(9, 62, 0, Boolean.parseBoolean("FALSE"));
            // Iniciar pedido
            plcConnector.writeBit(9, 62, 0, Boolean.parseBoolean("TRUE"));

        } catch (Exception ex) {

        }
        // Setar flag de PEDIDO EM CURSO

        // 1 - ATUALIZAR O PRÓXIMO NÚMERO DE PEDIDO (Verificar se é necessário)
        // MainFrame.posExpedArray[12] = MainFrame.posExpedArray[12] + 1;
        // try {
        // plcConnector.disconnect();
        // } catch (Exception e) {
        // // TODO Auto-generated catch block
        // e.printStackTrace();
        // }
    }

    public void clpEstoque(String ip, byte[] dadosClp1) {
        // -------------- Apresentação no console -----------------
        StringBuilder leituraClp1 = new StringBuilder();
        for (byte b : dadosClp1) {
            leituraClp1.append(String.format("%02X ", b));
        }
        String clp1 = leituraClp1.toString().trim();

        // System.out.println("[CLP ESTOQUE] " + clp1);
        PlcConnector plcConnectorEst = PlcConnectionManager.getConexao(ip);
        if (plcConnectorEst == null) {
            return;
        }
        // -------------- Leitura das variáveis -------------------
        recebidoOpEst = (dadosClp1[0] & 0x01) != 0;

        iniciarPedido = (dadosClp1[62] & (byte) 0x01) != 0;
        recebidoEstoque = (dadosClp1[64] & 0x01) != 0;
        iniciarGuardarEst = (dadosClp1[64] & 0x02) != 0;
        posicaoGuardarEst = ((dadosClp1[66] & 0xFF) << 8) | (dadosClp1[67] & 0xFF);
        for (int c = 0; c < 28; c++) {
            posicoesOcupadas[c] = dadosClp1[68 + c];
        }
        numeroOPEst = ((dadosClp1[96] & 0xFF) << 8) | (dadosClp1[97] & 0xFF);
        cancelOPEst = (dadosClp1[98] & 0x01) != 0;
        finishOPEst = (dadosClp1[98] & 0x02) != 0;
        startOPEst = (dadosClp1[98] & 0x04) != 0;

        ocupadoEst = (dadosClp1[100] & 0x01) != 0;
        aguardandoEst = (dadosClp1[100] & 0x02) != 0;
        manualEst = (dadosClp1[100] & 0x04) != 0;
        emergenciaEst = (dadosClp1[100] & 0x08) != 0;

        pedirPosicaoEst = (dadosClp1[102] & 0x01) != 0;
        posicaoEstoque = ((dadosClp1[104] & 0xFF) << 8) | (dadosClp1[105] & 0xFF);
        adicionarEstoque = (dadosClp1[106] & 0x01) != 0;

        removerEstoque = (boolean) ((dadosClp1[106] & 0x02) != 0);

        retornoEstoqueCheio = (dadosClp1[106] & 0x04) != 0;
        corGuardarEstoque = ((dadosClp1[108] & 0xFF) << 8) | (dadosClp1[109] & 0xFF);

        removerEstoque = (dadosClp1[106] & 0x02) != 0;

        // --------------------------------------------------------
        // Se o pedido foi iniciado e a estação ESTOQUE informou que iniciou a operação
        // ficando no estado OCUPADO
        // então a flag iniciarPedido fica em FALSE
        if (iniciarPedido == true && ocupadoEst == true) {
            pedidoEmCurso = true;
            statusEstoque = 0;
            statusProducao = 0;
            // updateDisplayStation();
            // eventos.add("Seq " + seq++ + ": iniciarPedido == true & ocupadoEst == true");
            if (!readOnly) {
                // System.out.println("Flag: IniciarPedido: Verificando se a estação ESTOQUE
                // iniciou o pedido...");

                // System.out.println("Flag: IniciarPedido: ESTOQUE iniciou o pedido...");
                try {
                    // System.out.println("Flag IniciarPedido: " + plcConnector.readBit(9, 62, 0));
                    // System.out.println("colocando IniciarPedido em FALSE");
                    // eventos.add("Seq " + seq++ + ": coloca IniciarPedido em FALSE");
                    plcConnectorEst.writeBit(9, 62, 0, Boolean.parseBoolean("FALSE")); // coloca IniciarPedido em FALSE

                    // System.out.println("Flag IniciarPedido: " + plcConnector.readBit(9, 62, 0));
                    // System.out.println("Flag ocupadoEst: " + plcConnector.readBit(9, 100, 0));
                } catch (Exception e) {
                    System.out.println(
                            "ERRO [iniciarPedido == true & ocupadoEst == true]: Atualização da Flag IniciarPedido [DB9:62.0] para FALSE");
                }
            }

        }

        // Se as três flags (StartOP, FinishOP e CancelOP) estão em FALSE, então a flag
        // RecebidoOP fica em FALSE
        if (startOPEst == false & finishOPEst == false & cancelOPEst == false) {
            // eventos.add("Seq " + seq++ + ": startOPEst == false & finishOPEst == false &
            // cancelOPEst == false");
            if (readOnly == false) {

                try {
                    // System.out.println("Seq " + seq++ + ": colocando RecebidoOPEst em FALSE");
                    // eventos.add("Seq " + seq++ + ": coloca RecebidoOPEst em FALSE");
                    plcConnectorEst.writeBit(9, 0, 0, Boolean.parseBoolean("FALSE")); // coloca RecebidoOPEst em FALSE

                } catch (Exception e) {
                    System.out.println("ERRO: Atualização da Flag RecebidoOPEstoque [DB9:0.0] para FALSE");
                }
            }
        }

        // Se a estação ESTOQUE sinalizou o início da operação e ficou OCUPADO, então a
        // flag RecebidoOP fica em TRUE
        if (startOPEst == true & recebidoOpEst == false) {
            if (statusProducao == 0 & pedidoEmCurso == true) {
                statusEstoque = 1;
            } else {
                // statusEstoque = 0;
            }
            // updateDisplayStation();
            // eventos.add("Seq " + seq++ + ": startOPEst == true & recebidoOpEst ==
            // false");
            if (readOnly == false) {
                // System.out.println("Flag: RecebidoOPEstoque_TRUE");
                try {
                    // System.out.println("StartOP: colocando RecebidoOPEstoque em TRUE");
                    // eventos.add("Seq " + seq++ + ": coloca RecebidoOPEst em TRUE");
                    plcConnectorEst.writeBit(9, 0, 0, Boolean.parseBoolean("TRUE")); // coloca RecebidoOPEst em TRUE

                } catch (Exception e) {
                    System.out.println(
                            "ERRO [startOp]: Atualização da Flag RecebidoOPEstoque [DB9:0.0] para TRUE");
                }
            }
        }

        // Se a estação ESTOQUE sinalizou o témino da operação e ficou OCUPADO, então a
        // flag RecebidoOP fica em TRUE
        if (finishOPEst == true & recebidoOpEst == false) {
            // eventos.add("Seq " + seq++ + ": finishOPEst == true & recebidoOpEst ==
            // false");
            if (readOnly == false) {
                // System.out.println("Flag: RecebidoOPEstoque_TRUE");
                try {
                    // System.out.println("FinishOP: colocando RecebidoOPEstoque em TRUE");
                    // eventos.add("Seq " + seq++ + ": coloca RecebidoOPEst em TRUE");
                    plcConnectorEst.writeBit(9, 0, 0, Boolean.parseBoolean("TRUE")); // coloca RecebidoOPEst em TRUE

                } catch (Exception e) {
                    System.out.println(
                            "ERRO [finishOp]: Atualização da Flag RecebidoOPEstoque [DB9:0.0] para TRUE");
                }
                if (statusProducao == 0 & pedidoEmCurso == true) {
                    statusEstoque = 2;
                } else {
                    // statusEstoque = 0;
                }
            }
        }

        // Se as flags de remover ou adicionar no estoque estão em FALSE então a flag
        // RecebidoEstoque fica em FALSE
        if (removerEstoque == false & adicionarEstoque == false) {
            // eventos.add("Seq " + seq++ + ": removerEstoque == false & adicionarEstoque ==
            // false");
            if (readOnly == false) {

                // System.out.println("colocando RecebidoEstoque em FALSEe");
                try {
                    // eventos.add("Seq " + seq++ + ": coloca RecebidoEstoquet em FALSE");
                    plcConnectorEst.writeBit(9, 64, 0, Boolean.parseBoolean("FALSE")); // coloca RecebidoEstoque em
                                                                                       // FALSE

                } catch (Exception e) {
                    System.out.println("ERRO: Atualização da Flag RecebidoEstoque [DB9:64.0] para FALSE");
                }
            }
        }

        // Atualiza a posição removida na tabela Estoque e na memória do clp Estoque
        if ((posicaoEstoque > 0) && removerEstoque == true) {
            if (!readOnly) {
                try {
                    plcConnectorEst.writeBit(9, 64, 0, true);

                    // Atualiza no CLP
                    byte offset = (byte) (68 + (posicaoEstoque - 1));
                    plcConnectorEst.writeByte(9, offset, (byte) 0);

                    // Atualiza no banco de dados
                    Optional<Block> blockToRemove = blockRepository.findByStorageIdAndPosition(1L, posicaoEstoque);
                    if (blockToRemove.isPresent()) {
                        blockRepository.delete(blockToRemove.get());
                    }

                } catch (Exception e) {
                    System.out.println("ERRO: Atualização da Flag RecebidoEstoque [DB9:64.0] para TRUE");
                }
            }
        }

        // Atualiza na posição a cor do bloco adicionado na tabela Estoque e na memória
        // do clp Estoque
        if ((posicaoEstoque > 0) && adicionarEstoque == true) {
            // eventos.add("Seq " + seq++ + ": (posicaoEstoque > 0) & adicionarEstoque ==
            // true");
            if (readOnly == false) {

                // System.out.println("Flag: RecebidoEstoque_TRUE - ADICIONAR ESTOQUE");
                // Coloca a flag RecebidoEstoque em TRUE
                try {
                    // eventos.add("Seq " + seq++ + ": coloca RecebidoEstoque em TRUE");
                    plcConnectorEst.writeBit(9, 64, 0, Boolean.parseBoolean("TRUE")); // coloca RecebidoEstoque em TRUE
                } catch (Exception e) {
                    System.out.println("ERRO: Atualização da Flag RecebidoEstoque [DB9:64.0] para TRUE");
                }

                // eventos.add("Seq " + seq++ + ": Adicionando bloco na posição: " +
                // posicaoEstoque);
                byte offset = (byte) (68 + (posicaoEstoque - 1));

                try {
                    plcConnectorEst.writeByte(9, offset, (byte) corGuardarEstoque);

                    // === CHAMAR ENDPOINT /estoque/salvar PARA ZERAR POSIÇÃO NO BANCO ===
                    RestTemplate restTemplate = new RestTemplate();

                    // Cria mapa de dados com apenas uma posição a ser zerada
                    Map<String, Integer> dadosMap = new HashMap<>();
                    dadosMap.put("posicao:" + posicaoEstoque, corGuardarEstoque);

                    // Prepara headers e request
                    HttpHeaders headers = new HttpHeaders();
                    headers.setContentType(MediaType.APPLICATION_JSON);
                    HttpEntity<Map<String, Integer>> request = new HttpEntity<>(dadosMap, headers);

                    // Envia a requisição
                    ResponseEntity<String> response = restTemplate.postForEntity("http://localhost:8080/estoque/salvar",
                            request, String.class);
                    System.out.println("Resposta ao salvar estoque no banco: " + response.getBody());

                } catch (Exception e) {
                    System.out.println("ERRO: Na tentativa de adicionar no Estoque");
                    e.printStackTrace();
                }
            }
        }

        // --------------------------------------------------------------------------------------------------------------------------------------
        // Se as flags ocupadoEst ou retornoEstoqueCheio estão em TRUE E a flag
        // iniciarGuardarEst foi ativada então a flag iniciarGuardarEst fica em FALSE
        if ((ocupadoEst == true | retornoEstoqueCheio == true) & iniciarGuardarEst == true) {
            // eventos.add("Seq " + seq++ + ": (ocupadoEst == true | retornoEstoqueCheio ==
            // true) & iniciarGuardarEst == true");
            if (readOnly == false) {
                // System.out.println("Flag: IniciarGuardar_FALSE");

                // Coloca a flag IniciarGuardar em FALSE
                try {
                    // eventos.add("Seq " + seq++ + ": Coloca iniciarGuardarEst em FALSE");
                    plcConnectorEst.writeBit(9, 64, 1, Boolean.parseBoolean("FALSE")); // Coloca iniciarGuardarEst em
                                                                                       // FALSE

                } catch (Exception e) {
                    System.out.println(
                            "ERRO: Atualização da Flag IniciarGuardarEstoque [DB9:64.1] para FALSE");
                }
            }

        }

        // Verificar se a estação Estoque está livre E pede posição para guardar
        // Aqui deve ser implementada/chamada a função que verifica qual a situação de
        // ocupação de cada
        // posição do Magazine de Estoque
        if (pedirPosicaoEst == true & ocupadoEst == false) {
            // System.out.println("ESTOU AQUI- (pedirPosicaoEst == true) & ocupadoEst ==
            // false");
            // eventos.add("Seq " + seq++ + ": pedirPosicaoEst == true & ocupadoEst ==
            // false");
            // Rotina para verificar qual posição está disponível para guardar
            if (!readOnly) {
                // Solicita posição disponível para guardar (0-LIVRE 1-PRETO 2-VERMELHO 3-AZUL)
                // Certifique-se de que posEstoqueLivre é seguro para acesso
                Set<Integer> posicoesUsadas = new HashSet<>(); // Para evitar duplicidade

                // int posEstoqueLivre = buscarPrimeiraPosicaoPorCor(0, posicoesUsadas) /*
                // getPositionEstoque(0) */;
                int posEstoqueLivre = blockRepository.findFirstFreePosition(1);
                // System.out.println("Posição disponível no Magazine Estoque: " +
                // posEstoqueRequest);
                if (posEstoqueLivre > 0) {

                    try {
                        // Atualiza a variável PosicaoGuardar no CLP ESTOQUE
                        // eventos.add("Seq " + seq++ + ": Atualiza a variável PosicaoGuardar no CLP
                        // ESTOQUE");
                        plcConnectorEst.writeInt(9, 66, posEstoqueLivre);
                    } catch (Exception e) {
                        System.out.println("ERRO: Atualização da PosicaoGuardarEstoque [DB9:66]");
                    }

                    try {
                        // Coloca a flag IniciarGuardar em TRUE
                        // eventos.add("Seq " + seq++ + ": Coloca a flag IniciarGuardar em TRUE");
                        plcConnectorEst.writeBit(9, 64, 1, Boolean.parseBoolean("TRUE")); // coloca IniciarGuardar em
                                                                                          // TRUE
                    } catch (Exception e) {
                        System.out.println("ERRO: Atualização da Flag IniciarGuardarEstoque [DB9:64.1]");
                    }
                } else {
                    System.out.println("ERRO: Nao existe posição livre.");
                }
            }
        }

    }

    public void clpProcesso(String ip, byte[] dadosClp2) {
        // -------------- Apresentação no console -----------------
        StringBuilder leituraClp2 = new StringBuilder();
        for (byte b : dadosClp2) {
            leituraClp2.append(String.format("%02X ", b));
        }
        String clp2 = leituraClp2.toString().trim();
        // System.out.println("[CLP2] " + clp2);

        PlcConnector plcConnectorPro = PlcConnectionManager.getConexao(ip);
        if (plcConnectorPro == null) {
            return;
        }

        // -------------- Leitura das variáveis -------------------
        recebidoOpPro = (dadosClp2[0] & 0x01) != 0;

        numeroOPPro = ((dadosClp2[2] & 0xFF) << 8) | (dadosClp2[3] & 0xFF);
        cancelOPPro = (dadosClp2[4] & 0x01) != 0;
        finishOPPro = (dadosClp2[4] & 0x02) != 0;
        startOPPro = (dadosClp2[4] & 0x04) != 0;

        ocupadoPro = (dadosClp2[6] & 0x01) != 0;
        aguardandoPro = (dadosClp2[6] & 0x02) != 0;
        manualPro = (dadosClp2[6] & 0x04) != 0;
        emergenciaPro = (dadosClp2[6] & 0x08) != 0;

        // Se as três flags (StartOP, FinishOP e CancelOP) estão em FALSE, então a flag
        // RecebidoOP fica em FALSE
        if (startOPPro == false && finishOPPro == false && cancelOPPro == false) {
            if (readOnly == false) {

                try {
                    plcConnectorPro.writeBit(2, 0, 0, Boolean.parseBoolean("FALSE")); // coloca RecebidoOPPro em FALSE
                } catch (Exception ex) {
                }
            }
        }
        // Se a estação PROCESSO sinalizou o inicio da operação e recebidoOpPro está em
        // FALSE, então a
        // flag RecebidoOPPRO fica em TRUE
        if (startOPPro == true && recebidoOpPro == false) {
            if (statusProducao == 0 & pedidoEmCurso == true) {
                statusProcesso = 1;
            } else {
                // statusProcesso = 0;
            }

            if (readOnly == false) {
                try {
                    plcConnectorPro.writeBit(2, 0, 0, Boolean.parseBoolean("TRUE")); // coloca RecebidoOPPro em TRUE
                } catch (Exception e) {

                    e.printStackTrace();
                }
            }

        }
        // Se a estação PROCESSO sinalizou o témino da operação e ficou OCUPADO, então a
        // flag RecebidoOP fica em TRUE
        if (finishOPPro == true && recebidoOpPro == false) {
            if (readOnly == false) {

                try {
                    plcConnectorPro.writeBit(2, 0, 0, Boolean.parseBoolean("TRUE")); // coloca RecebidoOPPro em TRUE
                } catch (Exception e) {

                    e.printStackTrace();
                }
                if (statusProducao == 0 & pedidoEmCurso == true) {
                    statusProcesso = 2;
                } else {
                    // statusProcesso = 0;
                }

            }

        }

    }

    public void clpMontagem(String ip, byte[] dadosClp3) {
        // -------------- Apresentação no console -----------------
        StringBuilder leituraClp3 = new StringBuilder();
        for (byte b : dadosClp3) {
            leituraClp3.append(String.format("%02X ", b));
        }
        String clp3 = leituraClp3.toString().trim();
        // System.out.println("[CLP3] " + clp3);

        PlcConnector plcConnectorMon = PlcConnectionManager.getConexao(ip);
        if (plcConnectorMon == null) {
            return;
        }
        // -------------- Leitura das variáveis -------------------
        recebidoOpMon = (dadosClp3[0] & 0x01) != 0;

        numeroOPMon = ((dadosClp3[2] & 0xFF) << 8) | (dadosClp3[3] & 0xFF);
        cancelOPMon = (dadosClp3[4] & 0x01) != 0;
        finishOPMon = (dadosClp3[4] & 0x02) != 0;
        startOPMon = (dadosClp3[4] & 0x04) != 0;

        ocupadoMon = (dadosClp3[6] & 0x01) != 0;
        aguardandoMon = (dadosClp3[6] & 0x02) != 0;
        manualMon = (dadosClp3[6] & 0x04) != 0;
        emergenciaMon = (dadosClp3[6] & 0x08) != 0;

        // Se as três flags (StartOPMon, FinishOPMon e CancelOPMon) estão em FALSE,
        // então a flag
        // RecebidoOPMon fica em FALSE
        if (startOPMon == false && finishOPMon == false && cancelOPMon == false) {
            if (readOnly == false) {

                try {

                    plcConnectorMon.writeBit(57, 0, 0, Boolean.parseBoolean("FALSE")); // coloca RecebidoOPMon em FALSE
                } catch (Exception ex) {
                }
            }
        }
        // Se a estação MONTAGEM sinalizou o inicio da operação e recebidoOpMon está em
        // FALSE, então a
        // flag RecebidoOPMon fica em TRUE
        if (startOPMon == true && recebidoOpMon == false) {
            if (statusProducao == 0 & pedidoEmCurso == true) {
                statusMontagem = 1;
            } else {
                // statusMontagem = 0;
            }

            // updateDisplayStation();
            if (readOnly == false) {

                try {

                    plcConnectorMon.writeBit(57, 0, 0, Boolean.parseBoolean("TRUE")); // coloca RecebidoOPMon em TRUE
                } catch (Exception ex) {
                }

            }

        }

        // Se a estação MONTAGEM sinalizou o témino da operação e ficou OCUPADO, então a
        // flag RecebidoOP fica em TRUE
        if (finishOPMon == true && recebidoOpMon == false) {
            if (readOnly == false) {

                try {
                    plcConnectorMon.writeBit(57, 0, 0, Boolean.parseBoolean("TRUE")); // coloca RecebidoOPMon em TRUE
                } catch (Exception e) {
                    e.printStackTrace();
                } // RecebidoOPMon
                if (statusProducao == 0 & pedidoEmCurso == true) {
                    statusMontagem = 2;
                } else {
                    // statusMontagem = 0;
                }

            }
        }

    }

    public void clpExpedicao(String ip, byte[] dadosClp4) throws Exception {
        // -------------- Apresentação no console -----------------
        StringBuilder leituraClp4 = new StringBuilder();
        for (byte b : dadosClp4) {
            leituraClp4.append(String.format("%02X ", b));
        }
        String clp4 = leituraClp4.toString().trim();
        // System.out.println("[CLP4] " + clp4);

        PlcConnector plcConnectorExp = PlcConnectionManager.getConexao(ip);
        if (plcConnectorExp == null) {
            return;
        }
        // -------------- Leitura das variáveis -------------------
        recebidoOpExp = (dadosClp4[0] & 0x01) != 0;

        recebidoExpedicao = (dadosClp4[2] & 0x01) != 0;
        iniciarGuardarExp = (dadosClp4[2] & 0x02) != 0;
        posicaoGuardarExp = ((dadosClp4[4] & 0xFF) << 8) | (dadosClp4[5] & 0xFF);

        int x = 0;
        for (int c = 0; c < 24; c += 2) {
            orderExpedicao[x] = (int) ((dadosClp4[c + 6] & 0xFF) << 8) | (dadosClp4[c + 7] & 0xFF);
            x++;
        }

        numeroOPExp = ((dadosClp4[30] & 0xFF) << 8) | (dadosClp4[31] & 0xFF);
        cancelOPExp = (dadosClp4[32] & 0x01) != 0;
        finishOPExp = (dadosClp4[32] & 0x02) != 0;
        startOPExp = (dadosClp4[32] & 0x04) != 0;

        ocupadoExp = (dadosClp4[34] & 0x01) != 0;
        aguardandoExp = (dadosClp4[34] & 0x02) != 0;
        manualExp = (dadosClp4[34] & 0x04) != 0;
        emergenciaExp = (dadosClp4[34] & 0x08) != 0;

        pedirPosicaoExp = (dadosClp4[36] & 0x01) != 0;
        posicaoGuardadoExpedicao = ((dadosClp4[38] & 0xFF) << 8) | (dadosClp4[39] & 0xFF);
        posicaoRemovidoExpedicao = ((dadosClp4[40] & 0xFF) << 8) | (dadosClp4[41] & 0xFF);
        adicionarExpedicao = (dadosClp4[42] & 0x01) != 0;
        removerExpedicao = (dadosClp4[42] & 0x02) != 0;
        opGuardadoExpedicao = ((dadosClp4[44] & 0xFF) << 8) | (dadosClp4[45] & 0xFF);

        // Se as três flags (StartOPExp, FinishOPExp e CancelOPExp) estão em FALSE,
        // então a flag
        // RecebidoOPExp fica em FALSE
        if (startOPExp == false & finishOPExp == false & cancelOPExp == false) {
            if (readOnly == false) {
                try {

                    // System.out.println("(startOPExp == false & finishOPExp == false & cancelOPExp
                    // == false): Atualização da Flag RecebidoOPExp [DB9:2.0] para FALSE");
                    plcConnectorExp.writeBit(9, 0, 0, Boolean.parseBoolean("FALSE")); // coloca RecebidoOPExp em FALSE

                } catch (Exception e) {
                    System.out.println(
                            "ERRO [startOp][finishOp]: Atualização da Flag RecebidoOPExp [DB9:0.0] para FALSE");
                }

            }
        }

        // Se o pedido foi finalizado pela estação de MONTAGEM e a estação EXPEDIÇÃO
        // informou que iniciou a operação
        // então a flag recebidoOpExp fica em TRUE
        if (startOPExp == true & recebidoOpExp == false) {
            if (statusProducao == 0 & pedidoEmCurso == true) {
                statusExpedicao = 1;
            } else {
                // statusExpedicao = 0;
            }
            // blockFinished = true;
            // updateDisplayStation();
            if (readOnly == false) {
                try {
                    plcConnectorExp.writeBit(9, 0, 0, Boolean.parseBoolean("TRUE")); // coloca RecebidoOPExp em TRUE

                } catch (Exception e) {
                    System.out.println(
                            "ERRO [startOp]: Atualização da Flag RecebidoOPExp [DB9:0.0] para TRUE");
                }

            }
        }

        // Se a estação EXPEDIÇÃO sinalizou o término da operação e ficou OCUPADO, então
        // a flag RecebidoOP fica em TRUE
        if (finishOPExp == true & recebidoOpExp == false) {
            if (readOnly == false) {
                // JOptionPane.showMessageDialog(null, "1 - Vou iniciar a guarda do BLOCO!!!");

                try {
                    // Panel3.plcWrite = new PlcConnector(ipExpedicao, 9, 0, 1, 0, 1);
                    plcConnectorExp.writeBit(9, 0, 0, Boolean.parseBoolean("TRUE")); // coloca RecebidoOPExp em TRUE
                    blockFinished = true;

                } catch (Exception e) {
                    System.out.println(
                            "ERRO [finishOp]: Atualização da Flag RecebidoOPExp [DB9:0.0] para TRUE");
                }
                if (statusProducao == 0 & pedidoEmCurso == true) {
                    statusExpedicao = 2;
                } else {
                    // statusExpedicao = 0;

                }
            }
        }

        if (pedirPosicaoExp == false) {
            if (!readOnly) {
                aux_expedicao = false;
                // Coloca a flag IniciarGuardar em FALSE
                try {
                    plcConnectorExp.writeBit(9, 2, 1, Boolean.parseBoolean("FALSE")); // coloca IniciarGuardar em FALSE

                } catch (Exception e) {
                    System.out.println(
                            "ERRO [Pedir Posição]: Atualização da Flag IniciarGuardar [DB9:2.1] para FALSE");
                }
            }
        }

        // verifica se Expedição pede posição para guardar
        if ((pedirPosicaoExp == true) & aux_expedicao == false) {

            // System.out.println(
            // "\n\nEstou aqui - if ((pedirPosicaoExp == true) & aux_expedicao ==
            // false)\n\n");
            // Rotina para verificar qual posição está disponível para guardar
            aux_expedicao = true;

            // ROTINA PARA LOCALIZAR POSIÇÃO DISPONÍVEL NO MAGAZINE DA EXPEDIÇÃO PARA
            // ADICIONAR BLOCO CONCLUÍDO
            // Rotina para verificar qual posição está disponível para guardar
            if (!readOnly) {

                // Solicita posição disponível para guardar (0-LIVRE 1-OCUPADA)
                // Certifique-se de que posExpedicaoLivre é seguro para acesso
                // int posExpedicaoLivre = posicaoExpedicaoSolicitada/*getPositionExpedicao()*/;
                // System.out.println("Posição disponível no Magazine Expedição: " +
                // posExpedicaoLivre);
                // Atualiza a variável PosicaoGuardarExpedicao no CLP EXPEDIÇÂO
                try {
                    plcConnectorExp.writeInt(9, 4, posicaoExpedicaoSolicitada); // Atualiza a variável
                                                                                // PosicaoGuardarExpedicao no CLP
                                                                                // EXPEDIÇÂO

                } catch (Exception e) {
                    System.out.println("ERRO: Atualização da PosicaoGuardarExpedicao [DB9:4]");
                }

                try {
                    plcConnectorExp.writeBit(9, 2, 1, Boolean.parseBoolean("TRUE")); // coloca IniciarGuardar em TRUE

                } catch (Exception e) {
                    System.out.println(
                            "ERRO [Pedir Posição]: Atualização da Flag IniciarGuardar [DB9:2.1] para TRUE");
                }

            }

        }

        if (!readOnly & (!adicionarExpedicao || !removerExpedicao)) {
            try {
                // System.out.println("(!readOnly & (!adicionarExpedicao || !removerExpedicao)):
                // Atualização da Flag RecebidoExpedicao [DB9:2.0] para FALSE");
                plcConnectorExp.writeBit(9, 2, 0, false); // coloca RecebidoExpedicao em FALSE
            } catch (Exception e) {
                if (!adicionarExpedicao & !removerExpedicao) {
                    System.out.println(
                            "ERRO [Adicionar e Remover Expedição]: Atualização da Flag RecebidoExpedicao [DB9:2.0] para FALSE");
                } else if (!adicionarExpedicao) {
                    System.out.println(
                            "ERRO [Adicionar Expedição]: Atualização da Flag RecebidoExpedicao [DB9:2.0] para FALSE");
                } else {
                    System.out.println(
                            "ERRO [Remover Expedição]: Atualização da Flag RecebidoExpedicao [DB9:2.0] para FALSE");
                }
            }
        }

        // Se a flag adicionarExpedicao está TRUE E aux_expedicao está FALSE então a
        // flag RecebidoExpedicao fica em TRUE
        if ((adicionarExpedicao == true) & aux_expedicao == false) {
            aux_expedicao = true;

            // Ler as variáveis PosicaoGuardadoExpedicao e opGuardadoExpedicao
            if (readOnly == false) {

                try {
                    // Panel3.plcWrite = new PlcConnector(ipExpedicao, 9, 2, 1, 0, 1);
                    plcConnectorExp.writeBit(9, 2, 0, Boolean.parseBoolean("TRUE")); // coloca RecebidoExpedicao em TRUE

                } catch (Exception e) {
                    System.out.println(
                            "ERRO [Adicionar Expedição]: Atualização da Flag RecebidoExpedicao [DB9:2.0] para TRUE");
                }

            }

            int offset = 6 + (posicaoGuardarExp - 1) * 2;
            System.out.println("Guardando Operacao em posicaoGuardarExp: " + posicaoGuardarExp);
            if (posicaoGuardarExp > 0) {
                try {
                    plcConnectorExp.writeInt(9, offset, opGuardadoExpedicao); // grava operação no CLP

                    // === CHAMAR ENDPOINT /expedicao/salvar PARA ATUALIZAR NO BANCO ===
                    RestTemplate restTemplate = new RestTemplate();

                    Map<String, Integer> dadosExp = new HashMap<>();
                    dadosExp.put("OP:" + posicaoGuardarExp, opGuardadoExpedicao); // exemplo: "OP:3" → valor da ordem

                    HttpHeaders headers = new HttpHeaders();
                    headers.setContentType(MediaType.APPLICATION_JSON);
                    HttpEntity<Map<String, Integer>> request = new HttpEntity<>(dadosExp, headers);

                    ResponseEntity<String> response = restTemplate.postForEntity(
                            "http://localhost:8080/expedicao/salvar",
                            request,
                            String.class);

                    System.out.println("Resposta ao salvar expedição no banco: " + response.getBody());

                } catch (Exception e) {
                    System.out.println("ERRO [ADICIONAR Expedição]: Atualização da posição no banco de dados");
                    e.printStackTrace();
                }
            }

        }

        // Se a flag removerExpedicao está TRUE E aux_expedicao está FALSE então a flag
        // RecebidoExpedicao fica em TRUE
        if ((removerExpedicao == true) & aux_expedicao == false) { // verifica se Expedição pede posição
            // para remover
            aux_expedicao = true;
            // System.out.println("Estou Aqui em => (removerExpedicao == true) &
            // aux_expedicao == false)");

            // Ler a variável PosicaoRemovidoExpedicao
            // posicaoRemovidoExpedicao = ((dadosClp4[40] & 0xFF) << 8) | (dadosClp4[41] &
            // 0xFF);
            // if (readOnly == false) {
            // System.out.println("Flag: RecebidoExpediçcao_TRUE");
            if (readOnly == false) {
                try {
                    // Panel3.plcWrite = new PlcConnector(ipExpedicao, 9, 2, 1, 0, 1);
                    plcConnectorExp.writeBit(9, 2, 0, Boolean.parseBoolean("TRUE")); // coloca RecebidoOPExpedicao em
                                                                                     // TRUE

                } catch (Exception e) {
                    System.out.println(
                            "ERRO [Adicionar Expedição]: Atualização da Flag RecebidoExpedicao [DB9:2.0] para TRUE");
                }
            }
            int offset = 6 + (posicaoRemovidoExpedicao - 1) * 2;

            System.out.println("Removendo Operacao de posicaoREmovidoExpedicao: " + posicaoRemovidoExpedicao);

            if (posicaoRemovidoExpedicao > 0 && !readOnly) {
                try {
                    // Remove do CLP
                    plcConnectorExp.writeInt(9, offset, 0);

                    // Prepara o JSON com apenas a posição a zerar
                    Map<String, Integer> dadosExp = new HashMap<>();
                    dadosExp.put("OP:" + posicaoRemovidoExpedicao, 0);

                    RestTemplate restTemplate = new RestTemplate();
                    HttpHeaders headers = new HttpHeaders();
                    headers.setContentType(MediaType.APPLICATION_JSON);
                    HttpEntity<Map<String, Integer>> request = new HttpEntity<>(dadosExp, headers);

                    // Chama o endpoint que agora deleta do banco quando valor == 0
                    ResponseEntity<String> response = restTemplate.postForEntity(
                            "http://localhost:8080/expedicao/salvar",
                            request,
                            String.class);

                    System.out.println("Resposta ao remover posição da tabela Expedição: "
                            + response.getBody());

                } catch (Exception e) {
                    System.out.println("ERRO [REMOVER Expedição]: Falha ao remover posição do banco");
                    e.printStackTrace();
                }
            }

            // adicionaRemoveMagazineExpedicao(posicaoRemovidoExpedicao, 0);
            // updatePlcExpedicao();
            // }
        }

        if ((posicaoGuardadoExpedicao == posicaoGuardarExp) & (ocupadoExp == false) & (finishOPExp == true)) {

            if (readOnly == false) {

                System.out.println("AQUI: statusProducao == 0 & pedidoEmCurso == true");
                if (statusProducao == 0 & pedidoEmCurso == true) {

                    System.out.println("\n--------------------------------------------------\nOperação OP:"
                            + opGuardadoExpedicao + " Finalizada\n------------------------------------ ");
                    pedidoEmCurso = false;
                    statusProducao = 1;
                }

                System.out.println("Operação OP:" + opGuardadoExpedicao + " Finalizada: ");
            }

            // }
        }

        if (adicionarExpedicao && !aux_expedicao) {
            aux_expedicao = true;

            PlcConnector plcExp = PlcConnectionManager.getConexao("10.74.241.40");
            int offset = 6 + (posicaoGuardarExp - 1) * 2;
            plcExp.writeInt(9, offset, numeroOPExp);
            plcExp.writeBit(9, 2, 0, true); // RecebidoExpedicao = TRUE
        }
    }

    // ping clps
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

    private int findFirstAvailablePosition() {
        List<Integer> posicoesOcupadas = blockRepository.findOccupiedPositionsInExpedicao();
        for (int i = 1; i <= 12; i++) {
            if (!posicoesOcupadas.contains(i)) {
                return i;
            }
        }
        return -1;
    }

    private int getValueOrDefault(Map<String, String> formData, String key, int defaultValue) {
        return formData.containsKey(key) && !formData.get(key).isEmpty()
                ? Integer.parseInt(formData.get(key))
                : defaultValue;
    }
}
