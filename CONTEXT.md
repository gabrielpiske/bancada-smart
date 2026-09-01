# CONTEXT.md — Bancada Smart (AI Agent Reference)

> Arquivo gerado em 2026-08-28. Use como ponto de partida para entender e modificar este projeto.

---

## 1. IDENTIDADE DO PROJETO

- **Nome:** Bancada Smart — Sistema de Gestão Industrial (SIG)
- **Tipo:** Aplicação web SCADA (supervisão e controle de linha de produção industrial)
- **Contexto:** Trabalho final SENAI — Técnico em Desenvolvimento de Sistemas (3º semestre)
- **Autores:** Gabriel Schweder Piske (backend) · Julio Henrique Busarello (frontend)
- **Repositório:** `gabrielpiske/bancada-smart`

---

## 2. STACK

| Camada | Tecnologia |
|---|---|
| Linguagem | Java 17 |
| Framework | Spring Boot 3.3.4 |
| Persistência | Spring Data JPA + Hibernate |
| View | Thymeleaf |
| Banco (dev) | MySQL 8.0 — `localhost:3306/smartweb` |
| Banco (prod) | SQL Server — `10.74.241.200\SQLEXPRESS/GestorEstoqueDB1` |
| Build | Maven (mvnw) |
| Frontend | HTML5 + CSS3 + JavaScript (ES Modules) + jQuery |
| Protocolo Industrial | S7 Siemens via TCP/IP (porta 102) — implementado manualmente |
| Hardware extra | ESP32 via HTTP REST (porta 80) |

**Porta da aplicação:** `8090` (dev)
**Perfil ativo:** `spring.profiles.active=dev` (em `application.properties`)

---

## 3. MAPA DE ARQUIVOS CRITICOS

```
AppSmartWeb/appsmartweb/src/main/java/com/smart1/appsmartweb/

AppsmartwebApplication.java              <- Entry point (@SpringBootApplication)

controller/
  MainController.java                    <- GET /, /loja, /view, /monitoramento; POST /pedidoTeste, /api/moverTampa
  BlockController.java                   <- GET+POST /estoque, /estoque/editar
  ConnectionController.java              <- POST /start-leituras, /stop-leituras, /smart/ping; GET /status-leituras
  SmartServiceController.java            <- GET /api/estado

model/
  Block.java                             <- id, position, color, storageId(FK), productionOrder(FK)
  Orders.java                            <- id, productionOrder(numero sequencial)
  Storage.java                           <- id, name, capacity

repository/
  BlockRepository.java                   <- JpaRepository + queries JPQL customizadas + metodos default
  OrdersRepository.java                  <- JpaRepository simples
  StorageRepository.java                 <- JpaRepository simples

service/
  connection/
    SmartService.java                    <- CORE (1306 linhas): maquina de estados, handshake CLP
    PlcConnectionManager.java            <- Pool ConcurrentHashMap<String, PlcConnector>
    PlcReaderTask.java                   <- Runnable: le bloco de bytes e chama callback
  entities/
    BlockService.java                    <- CRUD blocos + getLastPositionByStorageId()
    OrdersService.java                   <- CRUD ordens
    PedidoTesteService.java              <- Envio pedido teste (cria nova conexao — nao usa pool!)

exception/
  BlockNotFoundException.java
  ExpeditionFullException.java
  PLCCommunicationException.java
  StorageNotFoundException.java

util/
  S7Client.java                          <- Implementacao raw protocolo S7 (419 linhas, ByteBuffer)
  PlcConnector.java                      <- Wrapper: readString/Int/Float/Bit, writeString/Int/Bit
  DataInitializer.java                   <- @PostConstruct: cria Storage id=1 e id=2

src/main/resources/
  application.properties                 <- spring.profiles.active=dev
  application-dev.properties             <- MySQL, porta 8090, ddl-auto=update
  application-prod.properties            <- SQL Server, ddl-auto=none
  createDB_test.sql                      <- Script MySQL: schema + tabelas + 28 blocos seed
  static/assets/                         <- blocos/rBlocoCor{0-3}.png, laminas/, padroes/, tampas/
  static/css/                            <- fragments, main, message, stock, store, view
  static/js/                             <- connection, main, stock, store, utils, view
  templates/                             <- fragments, main, monitor, stock, store, view
```

---

## 4. BANCO DE DADOS

```sql
-- Storage: armazem fisico
storage(id PK, name, capacity)
  id=1 -> "Stock"      (estoque, 28 posicoes)
  id=2 -> "Expedition" (expedicao, 12 posicoes)

-- Orders: ordem de producao
orders(id PK, production_order BIGINT)
  production_order = ordersRepository.count() + 1

-- Block: bloco fisico
block(id PK, position INT, color INT, storage_id FK, production_order_id FK)
  color: 0=livre, 1=preto, 2=vermelho, 3=azul
  storage_id=1 -> estoque (posicoes 1-28)
  storage_id=2 -> expedicao (posicoes 1-12)
  production_order=NULL -> bloco disponivel
```

### Queries importantes (BlockRepository)
- `findAvailableBlocksByColor(color)` — blocos livres por cor no estoque (storageId=1)
- `findOccupiedPositionsInExpedicao()` — posicoes ocupadas na expedicao (storageId=2)
- `findFirstFreePosition(storageId)` — primeira posicao livre (metodo default na interface)
- `findByStorageIdAndPosition(storageId, position)` — bloco por armazem+posicao

---

## 5. ESTACOES DE PRODUCAO

| Estacao | IP | DB S7 | Bytes Lidos | Intervalo |
|---|---|---|---|---|
| Estoque | 10.74.241.10 | DB9 | 111 | 800ms |
| Processo | 10.74.241.20 | DB2 | 9 | 800ms |
| Montagem | 10.74.241.30 | DB57 | 9 | 800ms |
| Expedicao | 10.74.241.40 | DB9 | 48 | 800ms |
| ESP32 Tampa | 10.74.241.245 | — | — | sob demanda |

Porta S7: sempre 102

### Mapa de Bytes CLP Estoque (DB9)
```
Byte 0      -> recebidoOpEst (bit 0)
Bytes 2-61  -> InformacaoPedido: 3 andares x 9 shorts (cor, pos, l1c, l2c, l3c, l1p, l2p, l3p, proc)
Byte 62     -> iniciarPedido (bit 0)
Byte 64     -> recebidoEstoque (bit 0), iniciarGuardarEst (bit 1)
Bytes 66-67 -> posicaoGuardarEst (int 16-bit)
Bytes 68-95 -> posicoesOcupadas[28]
Bytes 96-97 -> numeroOPEst
Byte 98     -> cancelOPEst(b0), finishOPEst(b1), startOPEst(b2)
Byte 100    -> ocupadoEst(b0), aguardandoEst(b1), manualEst(b2), emergenciaEst(b3)
Byte 102    -> pedirPosicaoEst (bit 0)
Bytes 104-105 -> posicaoEstoque
Byte 106    -> adicionarEstoque(b0), removerEstoque(b1), retornoEstoqueCheio(b2)
Bytes 108-109 -> corGuardarEstoque
```

### Buffer de Pedido (60 bytes = 30 shorts Big-Endian, enviado a DB9 offset 2)
```
[0-8]  Andar1: cor_bloco, pos_estoque, l1cor, l2cor, l3cor, l1padrao, l2padrao, l3padrao, processamento(=1)
[9-17] Andar2: mesmo esquema
[18-26] Andar3: mesmo esquema
[27] numero_pedido  [28] total_blocos  [29] posicao_expedicao
```

---

## 6. API REST

| Metodo | Rota | Descricao |
|---|---|---|
| GET | / | Pagina inicial (IPs e conexao) |
| GET | /loja | Loja de pedidos |
| GET | /view | Visualizacao do pedido |
| GET | /monitoramento | Dashboard CLP |
| GET | /estoque | Estoque/expedicao |
| GET | /estoque/editar | Edicao do estoque |
| POST | /estoque/editar | Salva edicao (param: List<String> listBlocks) |
| POST | /pedidoTeste | FormData: block-color-N, l1-color-N, l2-color-N, l3-color-N, l1-pattern-N... |
| POST | /start-leituras | Body JSON: {"estoque":"IP","processo":"IP","montagem":"IP","expedicao":"IP"} |
| POST | /stop-leituras | Para threads e desconecta CLPs |
| GET | /status-leituras | {"ativo":bool,"estoque":bool,"processo":bool,"montagem":bool,"expedicao":bool} |
| POST | /smart/ping | Ping nos IPs -> {"estoque":bool,...} |
| GET | /api/estado | Campos do SmartService como JSON |
| POST | /api/moverTampa | Param form: pos=N -> repassa para ESP32 |

---

## 7. FLUXO DE UM PEDIDO

```
1. Usuario seleciona blocos/laminas/tampa em /loja
2. Clica "Enviar Pedido":
   a. enviarTampa() -> POST /api/moverTampa?pos=N -> ESP32 10.74.241.245:80/api/move_pos
   b. enviarPedido() -> POST /pedidoTeste (FormData)
3. SmartService.processarPedidoCompleto():
   a. Cria Orders no BD
   b. Encontra posicao livre na expedicao (1-12)
   c. Monta buffer 60 bytes
   d. Escreve buffer no CLP Estoque DB9 offset 2
   e. Grava pedido na expedicao (BD + CLP Expedicao)
   f. Seta IniciarPedido DB9.62.0 = TRUE
4. Thread de leitura (800ms) detecta mudancas de flag -> handshake automatico
5. Fluxo fisico: Estoque -> Processo -> Montagem -> Expedicao
6. statusProducao = 1 quando finalizado
```

---

## 8. BUGS CONHECIDOS

| ID | Severidade | Arquivo | Descricao |
|---|---|---|---|
| B1 | CRITICO | SmartService.java:648,1121 | RestTemplate chama localhost:8080 mas servidor esta em 8090 |
| B2 | CRITICO | ConnectionController.java:53 | switch com arrow sem break — fall-through acumula todas as tasks |
| B3 | MEDIO | SmartService.java | Variaveis de instancia publicas lidas/escritas por 4 threads sem volatile/synchronized |
| B4 | MEDIO | PedidoTesteService.java:128 | Cria nova conexao TCP a cada chamada, ignora pool |
| B5 | MEDIO | SmartService.java:895 | lastProcessedOpTimestamp declarado dentro do metodo — reinicializado a cada chamada |
| B6 | MEDIO | BlockController.java:109 | storageRepository.findAll().get(0) sem garantia de ordem |
| B7 | SEGURANCA | application-*.properties | Credenciais em texto puro no repositorio |
| B8 | SEGURANCA | Todo o projeto | Sem autenticacao/autorizacao |

---

## 9. CONVENCOES

### Java
- Package base: `com.smart1.appsmartweb`
- Controllers MVC: `@Controller` / REST: `@RestController`
- Injecao: construtores preferencialmente
- Transacoes: `@Transactional` em metodos que modificam BD
- Nomes de entidades em ingles; logica de negocio e comentarios em portugues

### Banco
- ddl-auto=update em dev; ddl-auto=none em prod
- StorageId=1 SEMPRE = Estoque; StorageId=2 SEMPRE = Expedicao

### Frontend
- JS usa ES Modules (import/export) — exceto stock.js e view.js (scripts globais)
- Estado CLP em localStorage.clpStatus
- Estado do pedido em localStorage.montagem
- Mensagens toast: showMessage(type, text) de utils.js
- jQuery em store.js e view.js

### CLP / S7
- SEMPRE usar PlcConnectionManager.getConexao(ip) — NUNCA new PlcConnector() diretamente
- Porta S7: sempre 102
- Booleanos: writeBit(db, byteOffset, bitOffset, valor)
- Integers: 2 bytes Big-Endian (writeInt/readInt)
- Strings S7: 2 bytes de cabecalho (maxLen + actualLen) + conteudo

---

## 10. VARIAVEIS GLOBAIS DO SMARTSERVICE

```java
static boolean readOnly           // TRUE = nao escreve no CLP (modo observacao)
static boolean pedidoEmCurso      // TRUE = ha pedido em processamento
static byte statusProducao        // 0=em andamento, 1=finalizado
static byte statusEstoque         // 0=idle, 1=em curso, 2=finalizado
static byte statusProcesso
static byte statusMontagem
static byte statusExpedicao
static int posicaoEstoqueSolicitada
static int posicaoExpedicaoSolicitada
static boolean blockFinished
static boolean aux_expedicao      // evita duplo processamento na expedicao
```

---

## 11. COMO INICIAR

```bash
# Requisitos: Java 17, MySQL 8.0

# 1. Criar banco
mysql -u root -p < AppSmartWeb/appsmartweb/src/main/resources/createDB_test.sql

# 2. Rodar
cd AppSmartWeb/appsmartweb
./mvnw spring-boot:run

# 3. Acessar em http://localhost:8090

# 4. Na interface: confirmar faixa de rede e clicar "Conectar"
```

---

## 12. O QUE NAO EXISTE (mas pode ser necessario)

- Sem testes automatizados
- Sem Docker/containerizacao
- Sem autenticacao de usuario
- Sem logs estruturados (usa System.out.println em todo o codigo)
- Sem configuracao externalizavel de IPs (hardcoded no Java)
- Rotas /estoque/salvar e /expedicao/salvar NAO existem (chamadas internamente mas nao mapeadas)
- WebSocket declarado no pom.xml mas nao implementado


---

## 13. MAQUINA DE ESTADOS — HANDSHAKE CLP (padrao por estacao)

Cada estacao segue este ciclo de handshake via flags booleanas:

```
CLP sinaliza startOP = TRUE
  -> App le startOP = TRUE e recebidoOP = FALSE
  -> App escreve recebidoOP = TRUE no CLP
  -> statusEstacao = 1 (em curso)

CLP sinaliza finishOP = TRUE
  -> App le finishOP = TRUE e recebidoOP = FALSE (ja resetou apos startOP)
  -> App escreve recebidoOP = TRUE no CLP
  -> statusEstacao = 2 (finalizado)

CLP zera startOP, finishOP e cancelOP
  -> App le todas = FALSE
  -> App escreve recebidoOP = FALSE no CLP
  -> Estacao livre para proximo pedido
```

Mapa de flags por estacao:
```
Estoque:   recebidoOpEst  DB9.0.0  / startOPEst DB9.98.2 / finishOPEst DB9.98.1
Processo:  recebidoOpPro  DB2.0.0  / startOPPro DB2.4.2  / finishOPPro DB2.4.1
Montagem:  recebidoOpMon  DB57.0.0 / startOPMon DB57.4.2 / finishOPMon DB57.4.1
Expedicao: recebidoOpExp  DB9.0.0  / startOPExp DB9.32.2 / finishOPExp DB9.32.1
```

---

## 14. ASSETS ESTATICOS

Todos em src/main/resources/static/assets/

```
blocos/
  rBlocoCor0.png  -> cor 0 (vazio/referencia)
  rBlocoCor1.png  -> preto
  rBlocoCor2.png  -> vermelho
  rBlocoCor3.png  -> azul

laminas/
  lamina{1-3}-{1-6}.png  -> lamina posicao 1/2/3, cor 1 a 6

padroes/
  padrao{1-3}-{1-2}.png  -> padrao 1/2/3, face 1 (frente) ou 2 (verso)

tampas/
  rTampa{0-3}.png  -> tampa cor 0-3

banqueida.png   -> foto da bancada (usada na tela principal)
bancada-hd.jpg / bancada-hd.png  -> versoes HD
senai.png       -> logo SENAI
```

Convencao de uso nas views (src):
- Blocos:  `assets/blocos/rBlocoCor${color}.png`
- Laminas: `assets/laminas/lamina${pos}-${color}.png`
- Padroes: `assets/padroes/padrao${pattern}-${face}.png`
- Tampas:  `assets/tampas/rTampa${color}.png`

---

## 15. TEMPLATES THYMELEAF

Todos os templates estendem o fragmento de cabecalho/rodape:
```html
<header th:replace="~{fragments :: header}"></header>
<footer th:replace="~{fragments :: footer}"></footer>
```

| Template | Rota | JS carregado | CSS carregado |
|---|---|---|---|
| main.html | / | connection.js, main.js (ES modules) | main.css, fragments.css |
| store.html | /loja | store.js (jQuery + ES module) | store.css, fragments.css |
| stock.html | /estoque | stock.js (script global) | stock.css, fragments.css |
| view.html | /view | view.js (script global) | view.css, fragments.css |
| monitor.html | /monitoramento | inline script | main.css, fragments.css |
| fragments.html | — | — | — |

Dados passados pelo Thymeleaf para os templates:
- stock.html: `estoque` (List<Integer> tamanho 28), `expedicao` (List<String> tamanho 12), `editMode` (boolean)
- store.html: `colorAvailability` (Map<Integer,Integer> cor -> quantidade disponivel)

---

## 16. LOCALSTORAGE (frontend)

Chave `clpStatus` (JSON):
```json
{
  "ip": "10.74.241",        // faixa de rede (3 octetos)
  "ativo": true,            // conexao ativa
  "estoque": true,          // ping ok
  "processo": false,
  "montagem": true,
  "expedicao": true
}
```

Chave `montagem` (JSON) — salvo apos confirmar pedido:
```json
{
  "cor1": "1",
  "cor2": "2",
  "cor3": "",
  "laminas": {
    "1": ["3","2","1"],
    "2": ["1","",""],
    "3": []
  },
  "padroes": {
    "1": ["2","1",""],
    "2": [],
    "3": []
  }
}
```

---

## 17. DEPENDENCIAS MAVEN (pom.xml)

```xml
spring-boot-starter-data-jpa      <!-- JPA + Hibernate -->
spring-boot-starter-thymeleaf     <!-- Templates HTML -->
spring-boot-starter-web           <!-- REST + MVC -->
mysql-connector-j                 <!-- Driver MySQL (runtime) -->
spring-boot-starter-websocket     <!-- WebSocket (declarado, nao usado) -->
spring-boot-starter-test          <!-- Testes (scope: test) -->
spring-boot-devtools              <!-- Live reload (optional) -->
<!-- NOTA: SQL Server (mssql-jdbc) esta comentado no pom.xml -->
```

---

## 18. PONTOS DE EXTENSAO SUGERIDOS

Se for adicionar funcionalidades, os lugares certos sao:

| Funcionalidade | Onde adicionar |
|---|---|
| Nova rota web | MainController.java ou novo Controller |
| Nova entidade BD | model/ + repository/ + migration SQL |
| Nova logica de CLP | SmartService.java (metodo por estacao) |
| Nova leitura de CLP | ConnectionController.java (case no switch) |
| Novo campo na API /api/estado | SmartServiceController.getEstadoAtual() |
| Novo tipo de dado S7 | S7Client.java (sendReadRequest switch + extractor) |
| Nova pagina web | templates/ + static/css/ + static/js/ + rota no Controller |

---

## 19. CHECKLIST PARA NOVOS AGENTES

Antes de modificar o projeto, confirme:

- [ ] Qual perfil esta ativo? (dev vs prod) — ver application.properties
- [ ] O MySQL esta rodando? — porta 3306, schema "smartweb"
- [ ] Os CLPs estao acessiveis? — se nao, setar SmartService.readOnly = true
- [ ] O bug B1 foi corrigido? (porta 8080 vs 8090) — antes de testar estoque/salvar
- [ ] O bug B2 foi corrigido? (switch fall-through) — antes de testar conexao multipla
- [ ] StorageId=1 e id=2 existem no banco? — DataInitializer so cria se count()==0
