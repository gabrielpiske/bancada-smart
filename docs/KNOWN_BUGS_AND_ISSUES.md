# Catálogo Completo de Bugs, Falhas de Concorrência e Dívidas Técnicas

> **Propósito:** Este documento lista todos os problemas identificados durante a auditoria estática de 100% do código-fonte do projeto **Bancada Smart**. Todo agente de IA que realizar manutenção ou refatoração deve usar este catálogo como checklist de correção.

---

## 🔴 1. Bugs Críticos de Execução e Concorrência

### BUG-01: Condição de Corrida no Socket TCP S7 (`S7Client.java` / `PlcConnector.java`)
- **Localização:** `S7Client.java` (linhas 291–359) e `PlcConnectionManager.java` (linhas 12–32)
- **Causa-Raiz:**
  - `PlcConnectionManager` compartilha **uma única instância** de `PlcConnector` (e um único `Socket` TCP) por endereço IP.
  - Os métodos `sendReadRequest()` e `sendWriteRequest()` em `S7Client` **não são `synchronized`** e compartilham campos de instância mutáveis (`byte[] response`, `int length`, `Object value`).
  - `sendReadRequest()` faz `outputStream.write(packet)`, executa `Thread.sleep(50)` e depois lê `inputStream.read(response)`.
- **Impacto:** Quando uma thread de polling (`PlcReaderTask` a cada 800ms) está no meio do `Thread.sleep(50)` e outra thread (ex.: HTTP `POST /pedidoTeste` ou escrita de handshake) envia um `sendWriteRequest()` no mesmo socket, os pacotes TCP de leitura e escrita se entrelaçam no stream, corrompendo os buffers de resposta e causando falhas intermitentes no CLP.
- **Solução na Refatoração:**
  - Tornar todas as operações de I/O no `S7Client` atômicas por conexão (`synchronized` no nível da instância de `S7Client` ou `ReentrantLock`).
  - Remover variáveis de estado de instância (`response`, `length`, `value`), usando variáveis locais ao método.

---

### BUG-02: Auto-Chamada HTTP para Porta Incorreta e Endpoints Inexistentes (`SmartService.java`)
- **Localização:** `SmartService.java` (linhas 636–650, 1112–1126, 1173–1185)
- **Causa-Raiz:**
  - Ao detectar adição no estoque (`adicionarEstoque == true`) ou adição/remoção na expedição (`adicionarExpedicao` / `removerExpedicao`), o `SmartService` instancia um `new RestTemplate()` e faz `POST` para:
    - `http://localhost:8080/estoque/salvar`
    - `http://localhost:8080/expedicao/salvar`
  - Porém:
    1. O servidor roda na porta **`8090`** (definida em `application-dev.properties`), não `8080`.
    2. **Não existe nenhum `@PostMapping("/estoque/salvar")` nem `@PostMapping("/expedicao/salvar")`** em nenhum Controller do projeto!
- **Impacto:** As operações de adicionar bloco via CLP no estoque e atualizar/remover OP via CLP na expedição sempre lançam `ResourceAccessException` (Connection refused) ou `404 Not Found`.
- **Solução na Refatoração:**
  - Eliminar completamente o `RestTemplate` interno e chamar diretamente os métodos transacionais de `BlockService` / `BlockRepository`.

---

### BUG-03: Blocos da Mesma Cor no Mesmo Pedido Recebem a Mesma Posição de Estoque (`SmartService.java`)
- **Localização:** `SmartService.java` (`montarPedidoParaCLP`, linhas 326–351) e `BlockRepository.java` (linhas 106–112)
- **Causa-Raiz:**
  - Em `montarPedidoParaCLP()`, um `Set<Integer> posicoesUsadas = new HashSet<>()` é instanciado na linha 329, mas **nunca é utilizado**.
  - Para cada um dos 3 andares do pedido, é chamado `blockRepository.findFirstFreePositionByColorAndStorage(blockColor, 1L)`, que sempre retorna `posicoesLivres.get(0)` sem marcar o bloco como reservado ou excluí-lo das próximas buscas.
- **Impacto:** Se o usuário montar um pedido com 2 ou 3 blocos da **mesma cor** (ex.: Andar 1 = Preto, Andar 2 = Preto), o buffer enviado ao CLP mandará o robô buscar ambos os blocos **na mesma posição física do estoque** (que já estará vazia no 2º bloco!).
- **Solução na Refatoração:**
  - Filtrar as posições já alocadas nos andares anteriores do mesmo pedido (`!posicoesUsadas.contains(pos)`) e reservar/vincular os blocos à `Orders` dentro da mesma transação `@Transactional`.

---

### BUG-04: Duplo Disparo de Pedido no Frontend (`store.js` + `store.html`)
- **Localização:** `static/js/store.js` (linha 351) e `templates/store.html` (linha 211)
- **Causa-Raiz:**
  - `store.js` registra: `document.getElementById("modal-confirm").addEventListener("click", async () => { await submitOrder(); });`
  - `store.html` importa `store.js` e registra novamente: `document.getElementById('modal-confirm').addEventListener('click', submitOrder);`
- **Impacto:** Cada clique em "Confirmar Pedido" no modal executa `submitOrder()` **duas vezes simultaneamente**, enviando 2 comandos HTTP para o ESP32 (`/api/moverTampa`) e 2 requisições `POST /pedidoTeste` concorrentes para o backend/CLP.
- **Solução na Refatoração:**
  - Remover o `<script type="module">` duplicado no final de `store.html` e desabilitar o botão `#modal-confirm` durante o processamento da requisição.

---

### BUG-05: Variáveis de Controle Reinicializadas a Cada Ciclo em `clpExpedicao` (`SmartService.java`)
- **Localização:** `SmartService.java` (linhas 895–896 e 1226–1261)
- **Causa-Raiz:**
  - `long lastProcessedOpTimestamp = 0;` e `int lastProcessedOp = 0;` foram declarados como **variáveis locais** dentro do método `clpExpedicao()`, que é chamado a cada 800ms.
  - Na linha 1233, `boolean isNewOrder = (opGuardadoExpedicao != lastProcessedOp) || (currentTime - lastProcessedOpTimestamp > 5000);` é sempre `true` quando `opGuardadoExpedicao > 0`.
- **Impacto:** Enquanto `opGuardadoExpedicao > 0`, a aplicação entra repetidamente no bloco de "Novo pedido detectado" a cada 800ms, chamando `resetExpedicaoState()` e reescrevendo no CLP da Expedição sem parar.
- **Solução na Refatoração:**
  - Mover `lastProcessedOp` e `lastProcessedOpTimestamp` para o estado da instância da estação Expedição (ou remover a lógica redundante unificando o fluxo de máquina de estados).

---

### BUG-06: Código Morto / Duplicado em `clpExpedicao` (`SmartService.java`)
- **Localização:** `SmartService.java` (linhas 1084–1134 vs. linhas 1217–1224)
- **Causa-Raiz:**
  - Na linha 1084: `if ((adicionarExpedicao == true) & aux_expedicao == false)` define `aux_expedicao = true`.
  - Na linha 1217: `if (adicionarExpedicao && !aux_expedicao)` verifica exatamente a mesma condição, que nunca será verdadeira pois `aux_expedicao` já foi alterada para `true` acima.
- **Impacto:** Lógica confusa, conflitante (linha 1109 grava `opGuardadoExpedicao`, enquanto linha 1222 gravaria `numeroOPExp`) e código inalcançável.

---

## 🟠 2. Problemas de Arquitetura, Estado e Banco de Dados

### BUG-07: Inconsistência entre `createDB_test.sql`, `DataInitializer.java` e Entidades JPA
- **Localização:** `createDB_test.sql`, `DataInitializer.java` e `Block.java`
- **Causa-Raiz:**
  1. `createDB_test.sql` define a coluna `production_order varchar(255)` na tabela `block`, sem chave estrangeira para `orders`. Já a entidade `Block.java` define `@ManyToOne @JoinColumn(name = "ProductionOrderId") private Orders productionOrder;`, que o Hibernate mapeia como `production_order_id BIGINT`.
  2. `createDB_test.sql` insere apenas 1 registro em `storage` (`'teste', 28`), omitindo o armazém `id = 2` (Expedição).
  3. Como `storageRepository.count() == 0` retorna `false` após rodar `createDB_test.sql`, o `DataInitializer.java` **não executa** e o `Storage` de Expedição (`id = 2`) nunca é criado!
- **Impacto:** Ao tentar criar um pedido (`storageRepository.findById(2L).orElseThrow()`), ocorre `NoSuchElementException` se o banco tiver sido populado via `createDB_test.sql`.
- **Solução na Refatoração:**
  - Padronizar nomes de colunas com `@Column` / `@JoinColumn` explícitos, usar Flyway/Liquibase ou corrigir o `DataInitializer` para verificar individualmente a existência de `Storage(1L)` e `Storage(2L)` e os 28 blocos iniciais.

---

### BUG-08: Endpoint `/api/estado` Incompleto Quebra a Página `/monitoramento` (`SmartServiceController.java`)
- **Localização:** `SmartServiceController.java` (linhas 20–28) e `templates/monitor.html`
- **Causa-Raiz:**
  - `SmartServiceController.getEstadoAtual()` retorna um `Map` com apenas **2 chaves**: `recebidoOpEst` e `finishOPEst`.
  - A página `/monitoramento` (`monitor.html`) faz polling em `/api/estado` a cada 1s esperando **mais de 100 variáveis** (`readOnly`, `pedidoEmCurso`, `statusEstoque`, todas as flags das 4 estações, variáveis dos 3 andares e variáveis de Realidade Aumentada).
- **Impacto:** O dashboard de monitoramento (`/monitoramento`) exibe 98% dos campos vazios. Além disso, os tempos das estações Processo, Montagem e Expedição em `/view` (`view.html` / `view.js`) não funcionam porque dependem dessas flags.
- **Solução na Refatoração:**
  - Retornar um DTO completo e estruturado em `/api/estado` contendo todas as variáveis das 4 estações e variáveis globais (e decodificar os bytes finais de Realidade Aumentada dos 4 CLPs).

---

### BUG-09: Estado Mutável Compartilhado Sem Sincronização (`SmartService.java`)
- **Localização:** `SmartService.java` (linhas 49–242)
- **Causa-Raiz:**
  - Dezenas de campos `public static` e `public` de instância são lidos e modificados concorrentemente por 4 threads do `ScheduledExecutorService` e pelas threads HTTP do Tomcat sem `volatile`, `Atomic*` ou `synchronized`.
- **Impacto:** Problemas de visibilidade de memória entre threads (Java Memory Model) — uma thread pode ler valores em cache de CPU desatualizados.

---

### BUG-10: Endereço de 24 Bits Incompleto em `S7Client.createReadRequest` (`S7Client.java`)
- **Localização:** `S7Client.java` (linha 99)
- **Causa-Raiz:**
  - Em `createReadRequest`, os 3 bytes do endereço S7ANY são montados como:
    `0x00, (byte) ((startAddress >> 8) & 0xFF), (byte) (startAddress & 0xFF)`
  - O byte mais significativo (`(startAddress >> 16) & 0xFF`) foi fixado em `0x00` (diferente de `createWriteRequest` na linha 162, que faz o shift de 16 bits corretamente).
- **Impacto:** Leituras em Data Blocks com `offset >= 8192` bytes endereçam o local errado da memória do CLP.

---

### BUG-11: Geração de Número de Ordem de Produção com Race Condition (`SmartService.java`)
- **Localização:** `SmartService.java` (linha 302) e `PedidoTesteService.java` (linha 46)
- **Causa-Raiz:**
  - `novaOrdem.setProductionOrder(ordersRepository.count() + 1);`
- **Impacto:** Se registros forem deletados ou se duas requisições chegarem juntas, números de OP se repetem.

---

### BUG-12: `BlockController.saveBlock()` Usa `storageRepository.findAll().get(0)` (`BlockController.java`)
- **Localização:** `BlockController.java` (linhas 109 e 133)
- **Causa-Raiz:**
  - Em vez de buscar `storageRepository.findById(1L)`, busca `findAll().get(0)`, assumindo que o primeiro item retornado pelo banco será sempre o Estoque (`id = 1`) e executando `findAll()` dentro de um loop na linha 133 (`N+1 queries`).
- **Impacto:** Baixa performance e risco de gravar blocos de estoque no armazém errado. Also, editar o estoque pelo site **não sincroniza** o array `posicoesOcupadas` (`DB9.DBB68..95`) na memória do CLP Estoque!

---

### BUG-13: `PedidoTesteService` Ignora o Pool de Conexões e Abre Novos Sockets TCP (`PedidoTesteService.java`)
- **Localização:** `PedidoTesteService.java` (linhas 128 e 192)
- **Causa-Raiz:**
  - Instancia `new PlcConnector("10.74.241.10", 102)` e `new PlcConnector("10.74.241.40", 102)` diretamente em vez de usar `PlcConnectionManager.getConexao(ip)`.
- **Impacto:** Código legado/alternativo que, se chamado, compete por conexões S7 no CLP e ignora os IPs configurados pelo usuário na interface web.

---

### BUG-14: IPs dos CLPs Hardcoded no Backend (`SmartService.java`, `MainController.java`)
- **Localização:**
  - `SmartService.java` linhas 272, 278 (`"10.74.241.10"`), 320, 1220 (`"10.74.241.40"`)
  - `MainController.java` linha 88 (`"http://10.74.241.245:80/api/move_pos"`)
- **Causa-Raiz:**
  - Embora a tela inicial (`/`) permita ao usuário alterar a faixa de IP (ex.: `192.168.0.X`) e envie os novos IPs para `/start-leituras`, o método `processarPedidoCompleto()` ignora os IPs configurados e usa sempre `10.74.241.10` e `10.74.241.40` fixos!
- **Impacto:** Mudar a faixa de IP na interface web funciona para o polling de leitura, mas falha na hora de enviar um pedido na loja.

---

## 🟡 3. Problemas de Frontend, Segurança e Qualidade de Código

### BUG-15: Case-Sensitivity no Caminho de Imagem do Modal (`store.js`)
- **Localização:** `static/js/store.js` (linha 388)
- **Causa-Raiz:**
  - Usa `assets/blocos/rblocoCor${blockColor}.png` (`b` minúsculo), mas o arquivo real é `rBlocoCor${blockColor}.png` (`B` maiúsculo).
- **Impacto:** Quando empacotado em JAR ou executado em Linux/Docker (filesystem case-sensitive), a imagem do bloco no modal retorna `404 Not Found`.

---

### BUG-16: Filtro de Cores Disponíveis na Loja Inclui Cor `0` (Vazio) Como "Azul" (`MainController.java` + `store.html`)
- **Localização:** `MainController.java` (linhas 49–54) e `store.html` (linhas 41–42)
- **Causa-Raiz:**
  - `MainController` adiciona ao mapa `colorAvailability` qualquer bloco com `storageId == 1`, sem filtrar `block.getColor() > 0`.
  - No `store.html`, a expressão ternária `entry.key == 1 ? 'Preto' : (entry.key == 2 ? 'Vermelho' : 'Azul')` transforma `entry.key == 0` em `"Azul"`!
- **Impacto:** Se houver posições vazias salvas com `color = 0` no banco (como faz o script `createDB_test.sql`), o dropdown de Cor do Bloco exibe duas opções "Azul" (uma com valor `0` e outra com valor `3`).

---

### BUG-17: Credenciais de Produção Expostas no Repositório (`application-dev.properties`, `application-prod.properties`)
- **Localização:** `src/main/resources/application-*.properties`
- **Causa-Raiz:** Senhas de banco de dados (`12345` e `Abcd1234` do usuário `sa`) estão em texto claro no controle de versão.
- **Solução na Refatoração:** Usar variáveis de ambiente (`${DB_PASSWORD:12345}`).

---

### BUG-18: Uso Generalizado de Operadores Bitwise (`&` e `|`) em Condições Lógicas (`SmartService.java`, `S7Client.java`)
- **Localização:** Múltiplas linhas em `SmartService.java` (ex.: linhas 506, 524, 549, 573, 662, 686, 943, 962, 984, 1023, 1063, 1084, 1138, 1198) e `S7Client.java` (linhas 76, 118).
- **Causa-Raiz:** Uso de `&` e `|` em vez dos operadores lógicos de curto-circuito `&&` e `||`.
- **Impacto:** Avalia todas as expressões desnecessariamente e pode causar `NullPointerException` caso alguma condição dependa de checagem prévia.