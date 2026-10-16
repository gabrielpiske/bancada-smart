# CONTEXT.md — Contexto Técnico Completo do Sistema (Bancada Smart 4.0)

> **Última atualização:** 2026-10-16  
> **Público-alvo:** Agentes de IA e Desenvolvedores realizando manutenção ou refatoração completa do sistema.  
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
| **WebSocket** | `spring-boot-starter-websocket` | Declarado no `pom.xml`, mas **ainda não utilizado** (oportunidade para refatoração) |
| **Banco de Dados (Dev)** | MySQL 8.0 | `jdbc:mysql://localhost:3306/smartweb` (`ddl-auto=update`, porta HTTP `8090`) |
| **Banco de Dados (Prod)** | Microsoft SQL Server | `jdbc:sqlserver://10.74.241.200\SQLEXPRESS;databaseName=GestorEstoqueDB1` (`ddl-auto=none`) |
| **Protocolo CLP** | Siemens S7 Comm (ISO-on-TCP) | Implementação própria em `S7Client.java` via `java.net.Socket` na porta TCP `102` |
| **Integração IoT** | ESP32 HTTP Server | Requisição `POST http://10.74.241.245:80/api/move_pos` para posicionamento de tampa |
| **Frontend** | HTML5 + CSS3 + JS (ES6 + jQuery 3.6) | Renderização inicial via Thymeleaf + interações assíncronas via `fetch` e `localStorage` |

---

## 3. Mapa Completo do Código-Fonte (AS-IS)

### 3.1. Backend (`AppSmartWeb/appsmartweb/src/main/java/com/smart1/appsmartweb/`)

| Arquivo | Linhas | Responsabilidade Atual |
|---|---|---|
| `AppsmartwebApplication.java` | 13 | Entry point `@SpringBootApplication`. |
| `controller/MainController.java` | 124 | Rotas de páginas (`GET /`, `/loja`, `/view`, `/monitoramento`) e ações de pedido (`POST /pedidoTeste`, `POST /api/moverTampa`). |
| `controller/BlockController.java` | 145 | Visualização e edição manual do magazine de estoque (`GET /estoque`, `GET /estoque/editar`, `POST /estoque/editar`). |
| `controller/ConnectionController.java` | 162 | Gerencia `ScheduledExecutorService` (4 threads, 800ms) para leitura contínua dos CLPs (`/start-leituras`, `/stop-leituras`, `/status-leituras`, `/smart/ping`). |
| `controller/SmartServiceController.java` | 30 | Endpoint `GET /api/estado` (atualmente retorna apenas `recebidoOpEst` e `finishOPEst`). |
| `service/connection/SmartService.java` | 1.306 | **Núcleo do sistema (God Class):** mantém variáveis de estado das 4 estações, processa pedidos (`processarPedidoCompleto`) e executa a lógica de handshake de `clpEstoque`, `clpProcesso`, `clpMontagem` e `clpExpedicao`. |
| `service/connection/PlcConnectionManager.java` | 80 | Pool estático (`ConcurrentHashMap<String, PlcConnector>`) de conexões TCP S7 por IP e utilitário `pingIp` (`InetAddress.isReachable(1000)`). |
| `service/connection/PlcReaderTask.java` | 35 | `Runnable` executado a cada 800ms que chama `plcConnector.readBlock(db, offset, size)` e invoca um `Consumer<byte[]>`. |
| `service/entities/BlockService.java` | 44 | CRUD básico de `Block` e busca da última posição inserida por `Storage` (`getLastPositionByStorageId`). |
| `service/entities/OrdersService.java` | 35 | CRUD básico de `Orders`. |
| `service/entities/PedidoTesteService.java` | 254 | **Classe legada/desativada** (comentada no `MainController`): enviava pedido abrindo conexões diretas sem usar `PlcConnectionManager`. |
| `repository/BlockRepository.java` | 129 | Queries JPQL e métodos `default` para mapa de posições e busca de posições livres no estoque/expedição. |
| `repository/OrdersRepository.java` | 10 | Interface `JpaRepository<Orders, Long>`. |
| `repository/StorageRepository.java` | 10 | Interface `JpaRepository<Storage, Long>`. |
| `model/Storage.java` | 42 | Entidade JPA: `id`, `name`, `capacity`. |
| `model/Block.java` | 67 | Entidade JPA: `id`, `position`, `color`, `storageId` (`@ManyToOne Storage`), `productionOrder` (`@ManyToOne Orders`). |
| `model/Orders.java` | 32 | Entidade JPA: `id`, `productionOrder` (`Long`). |
| `util/S7Client.java` | 419 | Implementação de baixo nível do protocolo S7 Siemens (TPKT + COTP + S7 Read/Write Var). |
| `util/PlcConnector.java` | 181 | Wrapper sobre `S7Client` expondo `readBit`, `readByte`, `readInt`, `readFloat`, `readString`, `readBlock` e métodos `write*` equivalentes. |
| `util/DataInitializer.java` | 35 | `@PostConstruct` que cria `Storage(1, "Stock", 28)` e `Storage(2, "Expedition", 12)` se `storageRepository.count() == 0`. |
| `exception/*.java` | 4 arq. | Exceções customizadas (`BlockNotFoundException`, `ExpeditionFullException`, `PLCCommunicationException`, `StorageNotFoundException`) — criadas mas ainda não utilizadas nos Services. |

### 3.2. Frontend (`AppSmartWeb/appsmartweb/src/main/resources/`)

| Arquivo | Linhas | Responsabilidade |
|---|---|---|
| `templates/fragments.html` | 51 | Header (navegação + dropdown de status Wi-Fi/Ping dos 4 CLPs + script `connection.js`) e Footer. |
| `templates/main.html` | 64 | Página inicial (`/`): exibição dos 4 IPs, input de faixa de rede e botão Conectar/Desconectar. |
| `templates/store.html` | 215 | Loja (`/loja`): montagem visual de até 3 blocos (cor + 3 lâminas + 3 padrões + rotação 180°), tampa e modal de confirmação. |
| `templates/stock.html` | 56 | Estoque (`/estoque` e `/estoque/editar`): grid 6x5 do Estoque (28 posições) e grid 4x3 da Expedição (12 posições). |
| `templates/view.html` | 101 | Visualização (`/view`): peça montada + imagem da bancada com indicadores e cronômetros por estação. |
| `templates/monitor.html` | 759 | Monitoramento (`/monitoramento`): painel de diagnóstico com ~110 variáveis do `SmartService` atualizadas a cada 1s. |
| `static/js/utils.js` | 53 | Persistência de `clpStatus` no `localStorage`, função `capitalize` e notificações toast (`showMessage`). |
| `static/js/connection.js` | 221 | Conectar/desconectar CLPs, polling `/smart/ping` a cada 2s, sincronização entre abas via evento `storage`. |
| `static/js/main.js` | 42 | Validação de faixa de IP e atualização dos campos de IP dos 4 módulos. |
| `static/js/store.js` | 554 | Lógica de adição/remoção dinâmica de blocos (1 a 3), preview de lâminas/padrões com rotação (`spin`), modal, disparo do ESP32 (`enviarTampa`) e submissão do pedido (`enviarPedido`). |
| `static/js/stock.js` | 96 | Seleção múltipla de slots por clique/arraste (`mousedown`/`mouseenter`) e pintura de cores no modo de edição do estoque. |
| `static/js/view.js` | 201 | Renderização empilhada do último pedido salvo em `localStorage.montagem` e cronômetro da estação Estoque via `/api/estado`. |
| `static/css/*.css` | 6 arq. | `main.css` (variáveis globais e tela inicial), `fragments.css`, `store.css`, `stock.css`, `view.css`, `message.css`. |

---

## 4. Modelo de Dados e Regras de Persistência

### Entidades JPA
1. **`Storage` (`storage`)**
   - `id = 1`: `"Stock"` — Estoque de matéria-prima, capacidade = **28 posições** (`1..28`).
   - `id = 2`: `"Expedition"` — Expedição de produtos acabados, capacidade = **12 posições** (`1..12`).
2. **`Orders` (`orders`)**
   - `id`: PK auto-incremento.
   - `productionOrder`: Número sequencial da Ordem de Produção (`OP`), enviado aos CLPs como inteiro de 16 bits.
3. **`Block` (`block`)**
   - Quando no **Estoque (`storageId = 1`)**:
     - `position`: `1..28`
     - `color`: `1` (Preto), `2` (Vermelho) ou `3` (Azul). *(Nota: no `BlockController.saveBlock`, cor `0` deleta o registro do banco).*
     - `productionOrder`: `null` (disponível para uso).
   - Quando na **Expedição (`storageId = 2`)**:
     - `position`: `1..12`
     - `productionOrder`: FK apontando para a `Orders` que ocupa aquela posição da expedição.

---

## 5. Contratos de Endpoints HTTP Atuais

| Método | Endpoint | Controller | Entrada | Retorno |
|---|---|---|---|---|
| `GET` | `/` | `MainController.goToMain` | — | View `main.html` |
| `GET` | `/loja` | `MainController.goToStore` | — | View `store.html` + model `colorAvailability` (`Map<Integer,Integer>`) |
| `GET` | `/view` | `MainController.goToView` | — | View `view.html` |
| `GET` | `/monitoramento` | `MainController.goToMonitoring` | — | View `monitor.html` |
| `GET` | `/estoque` | `BlockController.listBlocks` | — | View `stock.html` (`editMode=false`, `estoque[28]`, `expedicao[12]`) |
| `GET` | `/estoque/editar` | `BlockController.editBlocks` | — | View `stock.html` (`editMode=true`, `estoque[28]`, `expedicao[12]`) |
| `POST` | `/estoque/editar` | `BlockController.saveBlock` | Form `listBlocks` (28 valores `0..3`) | Redirect `/estoque` |
| `POST` | `/pedidoTeste` | `MainController.peditoTeste` | FormData (`block-color-{1..3}`, `l{1..3}-color-{1..3}`, `l{1..3}-pattern-{1..3}`, `total-blocks`) | Redirect `/loja?success` ou `/loja?error=...` |
| `POST` | `/api/moverTampa` | `MainController.moverTampa` | `application/x-www-form-urlencoded` (`pos=1\|2\|3`) | Proxy da resposta do ESP32 (`200 OK` ou `500`) |
| `POST` | `/start-leituras` | `ConnectionController.startLeituras` | JSON `{"estoque":"IP","processo":"IP","montagem":"IP","expedicao":"IP"}` | Texto `"Leituras com PlcReaderTask iniciadas."` |
| `POST` | `/stop-leituras` | `ConnectionController.stopLeituras` | — | Texto `"Leituras interrompidas."` |
| `GET` | `/status-leituras` | `ConnectionController.getStatusLeituras` | — | JSON `{"ativo":bool,"estoque":bool,"processo":bool,"montagem":bool,"expedicao":bool}` |
| `POST` | `/smart/ping` | `ConnectionController.pingPlcs` | JSON `{"estoque":"IP","processo":"IP","montagem":"IP","expedicao":"IP"}` | JSON `{"estoque":bool,"processo":bool,"montagem":bool,"expedicao":bool}` |
| `GET` | `/api/estado` | `SmartServiceController.getEstadoAtual` | — | JSON com estado das variáveis do `SmartService` |

---

## 6. Resumo dos 18 Bugs e Problemas Mapeados

> Detalhamento completo em [`docs/KNOWN_BUGS_AND_ISSUES.md`](./docs/KNOWN_BUGS_AND_ISSUES.md).

| Código | Severidade | Resumo |
|---|---|---|
| **BUG-01** | 🔴 Crítico | `S7Client` não é thread-safe: compartilha socket e buffer `response[]` entre thread de polling (800ms) e threads de escrita, além de ter `Thread.sleep(50)` no meio da leitura. |
| **BUG-02** | 🔴 Crítico | `SmartService` faz `POST` via `RestTemplate` para `http://localhost:8080/estoque/salvar` e `/expedicao/salvar` (porta errada, deveria ser `8090`, e rotas não existem em nenhum Controller). |
| **BUG-03** | 🔴 Crítico | `montarPedidoParaCLP` não reserva blocos já escolhidos: se um pedido tiver 2+ blocos da mesma cor, ambos recebem a mesma posição física do estoque. |
| **BUG-04** | 🔴 Crítico | Duplo `addEventListener('click', submitOrder)` em `store.js` e `store.html`: clicar em "Confirmar Pedido" envia o pedido **duas vezes**. |
| **BUG-05** | 🔴 Crítico | `lastProcessedOp` e `lastProcessedOpTimestamp` são variáveis locais em `clpExpedicao()`: reiniciam em `0` a cada 800ms, disparando reescrita infinita quando `opGuardadoExpedicao > 0`. |
| **BUG-06** | 🟠 Médio | Bloco `if (adicionarExpedicao && !aux_expedicao)` duplicado e inalcançável no final de `clpExpedicao()`. |
| **BUG-07** | 🟠 Médio | `createDB_test.sql` diverge das entidades JPA e impede a execução do `DataInitializer`, deixando o banco sem o `Storage` `id=2` (Expedição). |
| **BUG-08** | 🟠 Médio | `/api/estado` retorna apenas 2 variáveis, deixando `/monitoramento` com ~100 campos em branco e `/view` sem cronômetros para Processo, Montagem e Expedição. |
| **BUG-09** | 🟠 Médio | Dezenas de variáveis `public static` e de instância em `SmartService` acessadas por múltiplas threads sem `volatile` ou sincronização. |
| **BUG-10** | 🟠 Médio | `S7Client.createReadRequest` fixa o byte mais significativo do endereço de 24 bits em `0x00` (falha para offsets >= 8192). |
| **BUG-11** | 🟠 Médio | Número da OP gerado com `ordersRepository.count() + 1` (sujeito a colisão). |
| **BUG-12** | 🟠 Médio | `BlockController.saveBlock` usa `storageRepository.findAll().get(0)` dentro de loop e não sincroniza a edição com a memória do CLP Estoque (`DB9.DBB68..95`). |
| **BUG-13** | 🟡 Baixo | `PedidoTesteService` é código morto que abre novos sockets TCP ignorando o `PlcConnectionManager`. |
| **BUG-14** | 🟠 Médio | IPs dos CLPs (`10.74.241.10`, `10.74.241.40`) e ESP32 (`10.74.241.245`) estão hardcoded no backend, ignorando mudanças de faixa de IP feitas na UI ao enviar pedidos. |
| **BUG-15** | 🟡 Baixo | `store.js` usa `rblocoCor` (`b` minúsculo) no modal, quebrando em sistemas de arquivos case-sensitive (Linux/JAR). |
| **BUG-16** | 🟡 Baixo | `MainController.goToStore` inclui blocos com `color = 0` no mapa da loja, que o `store.html` renderiza como uma opção duplicada `"Azul"` de valor `0`. |
| **BUG-17** | 🔴 Segurança | Senhas de banco de dados em texto claro nos arquivos `application-*.properties`. |
| **BUG-18** | 🟡 Qualidade | Uso de operadores bitwise `&` e `|` em vez de `&&` e `||` em expressões booleanas por todo o `SmartService` e `S7Client`. |