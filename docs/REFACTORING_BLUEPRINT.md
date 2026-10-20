# Plano Diretor de Refatoração Guiada por IA (Refactoring Blueprint)

> **Status de Execução:** ✅ **CONCLUÍDO (Fases 1 a 6 finalizadas e validadas com 17 testes automatizados em 2026-10-20).**  
> **Propósito:** Este documento é o guia arquitetural e roteiro passo a passo (Playbook) utilizado para refatorar o sistema **Bancada Smart** de forma incremental, segura e 100% testável por agentes de IA, preservando a compatibilidade binária com os CLPs Siemens e o ESP32.

---

## 1. Visão Arquitetural: AS-IS vs. TO-BE

### Problemas da Arquitetura Atual (AS-IS)
- **God Object (`SmartService.java` — 1.306 linhas):** Mistura parsing binário de arrays de bytes, lógica de máquina de estados de 4 CLPs distintos, chamadas de banco de dados JPA, auto-chamadas HTTP (`RestTemplate`) e estado global estático.
- **Acoplamento Direto com Socket TCP:** Não existe interface abstraindo o `PlcConnector`/`S7Client`, impossibilitando testes automatizados sem os 4 CLPs físicos conectados na rede.
- **Concorrência Frágil:** 4 threads de polling (800ms) + threads HTTP acessam os mesmos sockets TCP e variáveis mutáveis sem sincronização.

### Arquitetura Alvo Proposta (TO-BE)

```mermaid
graph TD
    subgraph Web Layer [Camada Web / API]
        UI[Templates Thymeleaf + JS]
        CTRL[REST & MVC Controllers]
        WS[SSE / WebSocket State Broadcaster]
    end

    subgraph Application Layer [Camada de Aplicação e Orquestração]
        ORCH[OrderOrchestratorService\nCriação e Envio de Pedidos]
        COORD[PlcPollingCoordinator\nGerencia Ciclo de 800ms]
        INV[InventoryService\nGestão de Estoque e Expedição]
    end

    subgraph Station Handlers [Máquinas de Estado por Estação]
        H_EST[StockStationHandler\nDB9 - 111B]
        H_PRO[ProcessStationHandler\nDB2 - 9B]
        H_MON[AssemblyStationHandler\nDB57 - 9B]
        H_EXP[ExpeditionStationHandler\nDB9 - 48B]
        STATE[BancadaStateStore\nThread-Safe State Snapshot]
    end

    subgraph Infrastructure Layer [Infraestrutura Industrial e Persistência]
        PLC_IF[<<interface>>\nPlcClient]
        S7_IMPL[SiemensS7Client\nThread-Safe Per-IP Lock]
        MOCK_IMPL[SimulatedPlcClient\nPerfil dev/test sem hardware]
        ESP_CLIENT[Esp32LidClient\nHTTP Client Tampa]
        REPO[Spring Data JPA Repositories]
    end

    UI --> CTRL
    WS --> UI
    CTRL --> ORCH & COORD & INV & STATE
    COORD --> H_EST & H_PRO & H_MON & H_EXP
    H_EST & H_PRO & H_MON & H_EXP --> STATE
    H_EST & H_EXP --> INV
    ORCH & H_EST & H_PRO & H_MON & H_EXP --> PLC_IF
    ORCH --> ESP_CLIENT
    PLC_IF <|.. S7_IMPL
    PLC_IF <|.. MOCK_IMPL
    INV & ORCH --> REPO
```

---

## 2. Estrutura de Pacotes Recomendada (TO-BE)

```text
com.smart1.appsmartweb/
├── config/
│   └── BancadaProperties.java          # @ConfigurationProperties(prefix = "bancada") (IPs, timeouts, modo simulado)
├── controller/
│   ├── PageController.java             # Rotas MVC (/, /loja, /estoque, /view, /monitoramento)
│   ├── OrderApiController.java         # POST /pedidoTeste, POST /api/moverTampa
│   ├── InventoryController.java        # POST /estoque/editar, APIs de estoque/expedição
│   ├── PlcConnectionController.java    # /start-leituras, /stop-leituras, /status-leituras, /smart/ping
│   └── StateMonitorController.java     # GET /api/estado (retorna BancadaStateDto completo)
├── dto/
│   ├── OrderRequestDto.java            # DTO tipado para criação de pedido (substitui Map<String,String>)
│   ├── BancadaStateDto.java            # Snapshot completo de todas as variáveis para /monitoramento e /view
│   └── PlcConnectionStatusDto.java
├── model/
│   ├── Block.java
│   ├── Orders.java
│   ├── Storage.java
│   └── enums/
│       ├── BlockColor.java             # NONE(0), BLACK(1), RED(2), BLUE(3)
│       ├── LaminaColor.java            # NONE(0), RED(1), BLUE(2), YELLOW(3), GREEN(4), BLACK(5), WHITE(6)
│       ├── LaminaPattern.java          # NONE(0), HOUSE(1), SHIP(2), STAR(3)
│       └── StationType.java            # ESTOQUE, PROCESSO, MONTAGEM, EXPEDICAO
├── plc/
│   ├── PlcClient.java                  # Interface de comunicação com CLP
│   ├── PlcClientRegistry.java          # Gerenciador de conexões e IPs dinâmicos (substitui PlcConnectionManager)
│   ├── s7/
│   │   ├── S7ProtocolCodec.java        # Montagem e parsing puro de pacotes TPKT/COTP/S7 (testável unitariamente)
│   │   └── TcpSiemensPlcClient.java    # Implementação com ReentrantLock por socket e timeout configurável
│   ├── simulator/
│   │   └── InMemorySimulatedPlcClient.java # Simulador dos 4 CLPs em memória para desenvolvimento sem bancada
│   └── mapper/
│       ├── StockDb9Mapper.java         # byte[111] <-> StockPlcSnapshot + serialização do buffer de 60 bytes
│       ├── ProcessDb2Mapper.java       # byte[9]   <-> ProcessPlcSnapshot
│       ├── AssemblyDb57Mapper.java     # byte[9]   <-> AssemblyPlcSnapshot
│       └── ExpeditionDb9Mapper.java    # byte[48]  <-> ExpeditionPlcSnapshot
├── service/
│   ├── OrderOrchestratorService.java   # Criação de OP, reserva de blocos no estoque/expedição e envio ao CLP
│   ├── InventoryService.java           # Operações transacionais de adição/remoção de blocos no BD e CLP
│   ├── Esp32LidService.java            # Cliente HTTP para o ESP32 da tampa
│   └── station/
│       ├── PlcPollingCoordinator.java  # Gerencia ScheduledExecutorService
│       ├── BancadaStateStore.java      # Mantém estado atual thread-safe de toda a bancada
│       ├── OpHandshakeHelper.java      # Lógica reutilizável do ciclo StartOP/FinishOP/CancelOP <-> RecebidoOP
│       ├── StockStationHandler.java    # Regras de negócio da estação Estoque
│       ├── ProcessStationHandler.java  # Regras de negócio da estação Processo
│       ├── AssemblyStationHandler.java # Regras de negócio da estação Montagem
│       └── ExpeditionStationHandler.java # Regras de negócio da estação Expedição
└── repository/
    ├── BlockRepository.java            # Apenas queries JPA/JPQL (sem métodos default com System.out)
    ├── OrdersRepository.java
    └── StorageRepository.java
```

---

## 3. Roteiro de Refatoração em 6 Fases (Playbook para Agentes de IA)

Para evitar quebras massivas, a refatoração deve ser executada **fase por fase**, validando a compilação e os testes ao final de cada etapa.

---

### 🔹 FASE 1: Saneamento de Configuração, Banco de Dados e Logging
**Objetivo:** Resolver problemas de inicialização, credenciais hardcoded e inconsistências do schema JPA vs. SQL (`BUG-07`, `BUG-12`, `BUG-17`).

1. **Externalizar Configurações (`BancadaProperties`):**
   - Adicionar propriedades no `application.properties`:
     ```properties
     bancada.plc.default-subnet=10.74.241
     bancada.plc.port=102
     bancada.plc.poll-interval-ms=800
     bancada.plc.read-only=false
     bancada.plc.simulation-mode=false
     bancada.esp32.url=http://10.74.241.245:80/api/move_pos
     ```
   - Substituir senhas fixas em `application-dev.properties` e `application-prod.properties` por `${DB_PASSWORD:12345}`.
2. **Corrigir `DataInitializer.java` e `createDB_test.sql` (`BUG-07`):**
   - Garantir que `DataInitializer` verifique individualmente `storageRepository.existsById(1L)` (Stock, cap=28) e `storageRepository.existsById(2L)` (Expedition, cap=12).
   - Atualizar `createDB_test.sql` para refletir exatamente as colunas geradas pelo Hibernate (`storage_id`, `production_order_id`) e incluir os 2 storages.
3. **Corrigir `BlockController.saveBlock()` (`BUG-12`):**
   - Buscar explicitamente `storageRepository.findById(1L).orElseThrow()` fora do loop.

**Critério de Aceite da Fase 1:** `./mvnw clean test` compila e sobe o contexto Spring com H2/MySQL criando ambos os Storages (`1` e `2`) sem erros.

---

### 🔹 FASE 2: Refatoração Thread-Safe da Camada S7 e Simulador de CLP
**Objetivo:** Resolver `BUG-01`, `BUG-10`, `BUG-13` e permitir testes automatizados sem depender dos CLPs físicos.

1. **Corrigir `S7Client.java` (`BUG-01` e `BUG-10`):**
   - Remover os campos de instância `byte[] response`, `int length`, `Object value` (torná-los variáveis locais dentro de `sendReadRequest` e `sendWriteRequest`).
   - Adicionar `synchronized` (ou `ReentrantLock`) nos métodos `connect()`, `disconnect()`, `sendReadRequest()` e `sendWriteRequest()` para garantir que nenhuma leitura e escrita se sobreponham no mesmo socket TCP.
   - Configurar `socket.setSoTimeout(2000)` e timeout de conexão `socket.connect(new InetSocketAddress(address, port), 2000)` para evitar travamento infinito de threads.
   - Em `createReadRequest()`, corrigir o byte mais significativo do endereço de 24 bits: `(byte) ((startAddress >> 16) & 0xFF)` (igual ao `createWriteRequest`).
2. **Criar Interface `PlcClient` e `InMemorySimulatedPlcClient`:**
   - Extrair interface `PlcClient` com `readBlock`, `readBit`, `readInt`, `writeBlock`, `writeBit`, `writeByte`, `writeInt`, `ping`.
   - Criar implementação simulada (`InMemorySimulatedPlcClient`) que mantém os arrays de bytes dos DBs em memória (`DB9[111]`, `DB2[9]`, `DB57[9]`, `DB9_Exp[48]`) para uso em testes automatizados e quando `bancada.plc.simulation-mode=true`.
3. **Armazenar IPs Ativos Dinamicamente (`BUG-14`):**
   - Quando `/start-leituras` receber o mapa de IPs da faixa escolhida pelo usuário, registrar o IP de cada estação (`estoque`, `processo`, `montagem`, `expedicao`) no gerenciador de conexões para que o envio de pedidos use o IP configurado, e não `"10.74.241.10"` fixo.

**Critério de Aceite da Fase 2:** Testes unitários validando a geração dos pacotes S7 (`createReadRequest`, `createWriteRequest`) e concorrência de múltiplas threads lendo/escrevendo em um `PlcClient` sem corrupção de buffer.

---

### 🔹 FASE 3: Extração de Mappers Binários e Decomposição do `SmartService`
**Objetivo:** Quebrar o monolito `SmartService.java` (1.306 linhas) e corrigir `BUG-02`, `BUG-05`, `BUG-06`, `BUG-09` e `BUG-18`.

1. **Criar Mappers Binários Puros (Sem I/O):**
   - `StockDb9Mapper`: recebe `byte[111]` e retorna um `record StockPlcSnapshot(...)` com todos os campos decodificados (incluindo o byte `110` de Realidade Aumentada). Também converte os dados do pedido no `byte[60]` (`ByteOrder.BIG_ENDIAN`).
   - `ProcessDb2Mapper`: recebe `byte[9]` e retorna `ProcessPlcSnapshot`.
   - `AssemblyDb57Mapper`: recebe `byte[9]` e retorna `AssemblyPlcSnapshot`.
   - `ExpeditionDb9Mapper`: recebe `byte[48]` e retorna `ExpeditionPlcSnapshot`.
2. **Unificar o Handshake de OP (`OpHandshakeHelper`):**
   - A lógica de handshake (`startOP`, `finishOP`, `cancelOP` ↔ `recebidoOP`) é idêntica nas 4 estações:
     - Se `!startOP && !finishOP && !cancelOP` → escreve `recebidoOP = false`.
     - Se `startOP && !recebidoOP` → escreve `recebidoOP = true` e muda status da estação para `1` (em curso).
     - Se `finishOP && !recebidoOP` → escreve `recebidoOP = true` e muda status da estação para `2` (concluído).
   - Extrair essa lógica para um componente único testável, evitando duplicação nas 4 estações.
3. **Eliminar Auto-Chamadas HTTP (`BUG-02`) e Bugs da Expedição (`BUG-05`, `BUG-06`):**
   - Em vez de chamar `restTemplate.postForEntity("http://localhost:8080/estoque/salvar", ...)` e `/expedicao/salvar`, chamar diretamente um serviço `InventoryService`:
     - `inventoryService.addBlockToStock(posicaoEstoque, corGuardarEstoque)`
     - `inventoryService.removeBlockFromStock(posicaoEstoque)`
     - `inventoryService.confirmOrderInExpedition(posicaoGuardarExp, opGuardadoExpedicao)`
     - `inventoryService.removeOrderFromExpedition(posicaoRemovidoExpedicao)`
   - Transformar `lastProcessedOp` e `lastProcessedOpTimestamp` em atributos de estado persistentes no `ExpeditionStationHandler` e remover o bloco `if (adicionarExpedicao && !aux_expedicao)` duplicado.

**Critério de Aceite da Fase 3:** Testes unitários para cada `StationHandler` passando arrays `byte[]` simulados e verificando as chamadas exatas de `writeBit`/`writeByte`/`writeInt` no `PlcClient` e no `InventoryService`.

---

### 🔹 FASE 4: Refatoração do Fluxo de Criação de Pedidos (`OrderOrchestratorService`)
**Objetivo:** Resolver `BUG-03`, `BUG-11`, `BUG-13` e `BUG-16`.

1. **Alocação Distinta de Blocos da Mesma Cor (`BUG-03`):**
   - Ao montar um pedido com até 3 blocos, manter um `Set<Integer> posicoesReservadas` durante o loop dos 3 andares.
   - Buscar blocos disponíveis da cor solicitada no Estoque (`storageId = 1`) filtrando posições já presentes em `posicoesReservadas`.
   - Se o pedido pedir 2 blocos pretos e só houver 1 bloco preto no estoque, lançar exceção clara (`BlockNotFoundException`) antes de escrever qualquer byte no CLP.
2. **Remover `PedidoTesteService.java` (`BUG-13`):**
   - Consolidar toda a lógica de envio de pedidos no `OrderOrchestratorService`.
3. **Filtrar Cor `0` na Disponibilidade da Loja (`BUG-16`):**
   - Ignorar blocos com `color == 0` ao montar `colorAvailability` para a view `store.html`.

**Critério de Aceite da Fase 4:** Teste de integração criando um pedido com 3 blocos da mesma cor e verificando que o buffer de 60 bytes gerado possui 3 posições de estoque distintas nos índices `1`, `10` e `19`.

---

### 🔹 FASE 5: Completação da API `/api/estado` e Monitoramento em Tempo Real
**Objetivo:** Resolver `BUG-08` e habilitar 100% do funcionamento de `/monitoramento` e `/view`.

1. **Expandir `GET /api/estado` (`SmartServiceController` / `StateMonitorController`):**
   - Retornar todas as variáveis esperadas pelos IDs de elementos DOM de `monitor.html` e `view.js`:
     - Globais (`readOnly`, `aux_expedicao`, `pedidoEmCurso`, `blockFinished`, `statusProducao`, `statusEstoque`, `statusProcesso`, `statusMontagem`, `statusExpedicao`, `posicaoEstoqueSolicitada`, `posicaoExpedicaoSolicitada`).
     - Todas as variáveis de `Estoque` (incluindo os 3 andares e `posicoesOcupadas`), `Processo`, `Montagem` e `Expedição`.
     - Flags de status de todas as estações (`recebidoOpPro`, `finishOPPro`, `recebidoOpMon`, `finishOPMon`, `recebidoOpExp`, `finishOPExp`) para que os cronômetros de `/view` funcionem em todas as 4 estações.

**Critério de Aceite da Fase 5:** Endpoint `GET /api/estado` retorna JSON contendo todas as chaves mapeadas no `monitor.html`.

---

### 🔹 FASE 6: Saneamento e Correções de Frontend
**Objetivo:** Resolver `BUG-04` e `BUG-15`, melhorando a confiabilidade da interface web.

1. **Remover Duplo Listener no Modal da Loja (`BUG-04`):**
   - Em `templates/store.html`, remover as linhas 208–212 (`document.getElementById('modal-confirm').addEventListener('click', submitOrder);`), mantendo apenas o listener registrado dentro de `static/js/store.js` (linha 351).
   - Desabilitar o botão `#modal-confirm` enquanto `submitOrder()` estiver em execução para impedir cliques duplos do usuário.
2. **Corrigir Case-Sensitivity de Asset (`BUG-15`):**
   - Em `static/js/store.js` (linha 388), alterar `assets/blocos/rblocoCor${blockColor}.png` para `assets/blocos/rBlocoCor${blockColor}.png`.
3. **Tratamento de Resposta no Envio de Pedido (`store.js`):**
   - Verificar `response.ok` e parâmetros de erro na URL (`?error=...`) ao enviar `POST /pedidoTeste`, exibindo feedback claro via `showMessage("error", ...)`.
4. **Completar Cronômetros das 4 Estações em `view.js` e `view.html`:**
   - Adicionar IDs para os cronômetros de Processo (`tempo-processo`), Montagem (`tempo-montagem`), Expedição (`tempo-expedicao`) e Tempo Total, atualizando-os com base nas flags retornadas por `/api/estado`.

**Critério de Aceite da Fase 6:** Clicar em "Confirmar Pedido" dispara exatamente 1 requisição para `/api/moverTampa` e 1 requisição para `/pedidoTeste`, sem imagens quebradas no modal.