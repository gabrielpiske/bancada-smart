# CONTEXT.md — Contexto Técnico Completo do Sistema (Bancada Smart 4.0)

> **Última atualização:** 2026-10-20 (Refatoração Completa — Fases 1 a 6 Concluídas)  
> **Público-alvo:** Agentes de IA e Desenvolvedores mantendo ou evoluindo o sistema.  
> **Veja também:** [`AGENTS.md`](./AGENTS.md) · [`docs/PLC_PROTOCOL_AND_MEMORY_MAP.md`](./docs/PLC_PROTOCOL_AND_MEMORY_MAP.md) · [`docs/KNOWN_BUGS_AND_ISSUES.md`](./docs/KNOWN_BUGS_AND_ISSUES.md) · [`docs/REFACTORING_BLUEPRINT.md`](./docs/REFACTORING_BLUEPRINT.md)

---

## 1. Identidade e Propósito do Sistema

- **Nome Oficial:** Bancada Smart — Sistema de Gestão Industrial (`SIG - BANCADA 4.0`)
- **Natureza:** Sistema Web SCADA / MES (Manufacturing Execution System) educacional para supervisão e controle em tempo real de uma bancada automatizada Indústria 4.0.
- **Origem:** Projeto integrador (3º semestre) do Curso Técnico em Desenvolvimento de Sistemas — SENAI Timbó.
- **Autores:**
  - **Gabriel Schweder Piske** — Backend, Comunicação Industrial S7 e Persistência
  - **Julio Henrique Busarello** — Frontend, UI/UX e Visualização 2D/3D Empilhada

---

## 2. Stack Tecnológica e Ambiente

| Camada | Tecnologia / Biblioteca | Detalhes |
|---|---|---|
| **Linguagem** | Java 17 | Configurado no `pom.xml` (`<java.version>17</java.version>`) |
| **Framework Backend** | Spring Boot 3.3.4 | `spring-boot-starter-web`, `spring-boot-starter-data-jpa`, `spring-boot-starter-thymeleaf` |
| **Banco de Dados (Dev)** | MySQL 8.0 | `jdbc:mysql://localhost:3306/smartweb` (`ddl-auto=update`, porta HTTP `8090`) |
| **Banco de Dados (Test)** | H2 In-Memory | `jdbc:h2:mem:smartweb_test;MODE=MySQL` (`simulation-mode=true`) |
| **Banco de Dados (Prod)** | Microsoft SQL Server | `jdbc:sqlserver://10.74.241.200\SQLEXPRESS;databaseName=GestorEstoqueDB1` (`ddl-auto=none`) |
| **Protocolo CLP** | Siemens S7 Comm (ISO-on-TCP) | `S7ProtocolCodec` + `S7Client` thread-safe via `java.net.Socket` na porta TCP `102` |
| **Simulador CLP** | `InMemorySimulatedPlcClient` | Simulador S7 Big-Endian em memória (`bancada.plc.simulation-mode=true`) |
| **Integração IoT** | ESP32 HTTP Server | `Esp32LidService` (`POST /api/move_pos` para posicionamento de tampa) |
| **Frontend** | HTML5 + CSS3 + JS (ES6 + jQuery 3.6) | Renderização inicial via Thymeleaf + interações assíncronas via `fetch` e `localStorage` |

---

## 3. Mapa Completo do Código-Fonte (Pós-Refatoração)

### 3.1. Backend (`AppSmartWeb/appsmartweb/src/main/java/com/smart1/appsmartweb/`)

| Arquivo | Responsabilidade |
|---|---|
| `AppsmartwebApplication.java` | Entry point `@SpringBootApplication`. |
| `config/BancadaProperties.java` | Propriedades externalizadas (`@ConfigurationProperties(prefix = "bancada")`): sub-rede padrão, porta S7, intervalo de polling, timeouts, `readOnly`, `simulationMode` e URL do ESP32. |
| `controller/MainController.java` | Rotas de páginas (`GET /`, `/loja`, `/view`, `/monitoramento`) e ações de pedido (`POST /pedidoTeste`, `POST /api/moverTampa`). |
| `controller/BlockController.java` | Visualização e edição manual transacional do magazine de estoque (`GET /estoque`, `GET /estoque/editar`, `POST /estoque/editar`). |
| `controller/ConnectionController.java` | Gerencia `ScheduledExecutorService` (4 threads, `800ms`) para leitura contínua dos CLPs e registro dinâmico de IPs (`/start-leituras`, `/stop-leituras`, `/status-leituras`, `/smart/ping`). |
| `controller/SmartServiceController.java` | Endpoint `GET /api/estado` retornando o mapa completo (~114 variáveis) do `BancadaStateStore`. |
| `plc/PlcClient.java` | Interface de comunicação com CLP Siemens (real ou simulado). |
| `plc/PlcClientRegistry.java` | Componente Spring que inicializa e sincroniza `PlcConnectionManager` com `BancadaProperties`. |
| `plc/s7/S7ProtocolCodec.java` | Montagem e parsing binário puro (`ByteOrder.BIG_ENDIAN`) de pacotes TPKT/COTP/S7 Read & Write Var (endereçamento 24-bit completo). |
| `plc/simulator/InMemorySimulatedPlcClient.java` | Simulador thread-safe dos Data Blocks (`DB9`, `DB2`, `DB57`) em memória. |
| `plc/mapper/StockDb9Mapper.java` | Mapper binário `byte[111] <-> StockPlcSnapshot` e serialização do buffer de 60 bytes de pedido. |
| `plc/mapper/ProcessDb2Mapper.java` | Mapper binário `byte[9] <-> ProcessPlcSnapshot`. |
| `plc/mapper/AssemblyDb57Mapper.java` | Mapper binário `byte[9] <-> AssemblyPlcSnapshot`. |
| `plc/mapper/ExpeditionDb9Mapper.java` | Mapper binário `byte[48] <-> ExpeditionPlcSnapshot`. |
| `service/OrderOrchestratorService.java` | Criação transacional de OP, alocação de posições distintas de blocos da mesma cor no estoque e envio do pedido aos CLPs Estoque e Expedição. |
| `service/InventoryService.java` | Gestão transacional de adição/remoção de blocos e OPs no Estoque (`storageId=1`) e Expedição (`storageId=2`). |
| `service/Esp32LidService.java` | Cliente HTTP para acionamento da tampa no ESP32 (com suporte a modo simulado). |
| `service/station/BancadaStateStore.java` | Armazenamento central thread-safe (`AtomicReference` / `volatile`) do estado das 4 estações e variáveis globais. |
| `service/station/OpHandshakeHelper.java` | Lógica reutilizável do ciclo `StartOP / FinishOP / CancelOP ↔ RecebidoOP`. |
| `service/station/StockStationHandler.java` | Máquina de estados da Estação 1 — Estoque (`DB9`, `111B`). |
| `service/station/ProcessStationHandler.java` | Máquina de estados da Estação 2 — Processo (`DB2`, `9B`). |
| `service/station/AssemblyStationHandler.java` | Máquina de estados da Estação 3 — Montagem (`DB57`, `9B`). |
| `service/station/ExpeditionStationHandler.java` | Máquina de estados da Estação 4 — Expedição (`DB9`, `48B`). |
| `service/connection/SmartService.java` | Fachada compatível que delega aos `StationHandlers` e `OrderOrchestratorService`. |
| `service/connection/PlcConnectionManager.java` | Pool thread-safe de conexões por IP e registro dinâmico de IPs por `StationType`. |
| `service/connection/PlcReaderTask.java` | `Runnable` de leitura periódica de blocos de memória via `PlcClient`. |
| `model/*.java` & `model/enums/*.java` | Entidades JPA (`Storage`, `Block`, `Orders`) e enums (`StationType`, `BlockColor`, `LaminaColor`, `LaminaPattern`). |
| `util/S7Client.java` & `util/PlcConnector.java` | Cliente S7 TCP sincronizado por instância (`synchronized`) com timeouts de conexão e leitura. |
| `util/DataInitializer.java` | Inicializa individualmente `Storage(1L, "Stock", 28)` e `Storage(2L, "Expedition", 12)`. |

---

## 4. Status dos 18 Bugs Mapeados

Todos os 18 bugs catalogados em [`docs/KNOWN_BUGS_AND_ISSUES.md`](./docs/KNOWN_BUGS_AND_ISSUES.md) foram **resolvidos e cobertos por testes automatizados**:

| Código | Status | Solução Aplicada |
|---|---|---|
| **BUG-01** | ✅ Resolvido | `S7Client` e `PlcConnector` sincronizados por instância, sem buffers compartilhados e sem `Thread.sleep(50)`. |
| **BUG-02** | ✅ Resolvido | `RestTemplate` interno substituído por chamadas transacionais diretas ao `InventoryService`. |
| **BUG-03** | ✅ Resolvido | `OrderOrchestratorService` reserva posições distintas em `posicoesReservadas` para blocos da mesma cor no mesmo pedido. |
| **BUG-04** | ✅ Resolvido | Removido listener duplicado em `store.html` e adicionado bloqueio contra duplo clique em `store.js`. |
| **BUG-05** | ✅ Resolvido | `lastProcessedOp` e `lastProcessedOpTimestamp` movidos para campos de instância em `ExpeditionStationHandler`. |
| **BUG-06** | ✅ Resolvido | Bloco duplicado/inalcançável removido em `ExpeditionStationHandler`. |
| **BUG-07** | ✅ Resolvido | `DataInitializer` verifica individualmente `existsById(1L)` e `existsById(2L)`; `createDB_test.sql` alinhado ao schema JPA. |
| **BUG-08** | ✅ Resolvido | `GET /api/estado` retorna todas as ~114 variáveis via `BancadaStateStore.toStateMap()`. |
| **BUG-09** | ✅ Resolvido | Estado encapsulado em `BancadaStateStore` com `AtomicReference`, records imutáveis e `volatile`. |
| **BUG-10** | ✅ Resolvido | Endereço de 24 bits completo (`(startAddress >> 16) & 0xFF`) em `S7ProtocolCodec.createReadRequest`. |
| **BUG-11** | ✅ Resolvido | Geração de OP sincronizada usando `COALESCE(MAX(o.productionOrder), 0) + 1`. |
| **BUG-12** | ✅ Resolvido | `BlockController.saveBlock` busca `storageRepository.findById(1L)` uma única vez fora do loop. |
| **BUG-13** | ✅ Resolvido | `PedidoTesteService.java` removido e consolidado em `OrderOrchestratorService`. |
| **BUG-14** | ✅ Resolvido | IPs das estações registrados dinamicamente em `PlcConnectionManager` e URL do ESP32 externalizada em `BancadaProperties`. |
| **BUG-15** | ✅ Resolvido | Corrigido caminho para `assets/blocos/rBlocoCor${blockColor}.png` em `store.js`. |
| **BUG-16** | ✅ Resolvido | `MainController.goToStore` e `store.html` filtram apenas cores válidas (`1..3`). |
| **BUG-17** | ✅ Resolvido | Credenciais externalizadas via variáveis de ambiente (`${DB_PASSWORD:...}`). |
| **BUG-18** | ✅ Resolvido | Operadores bitwise `&` e `\|` substituídos por operadores lógicos `&&` e `\|\|`. |
