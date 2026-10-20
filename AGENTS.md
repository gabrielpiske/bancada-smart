# AGENTS.md — Guia Operacional para Agentes de IA

> **Propósito:** Este arquivo define as regras, invariantes de hardware, arquitetura e diretrizes que qualquer agente de IA deve seguir ao analisar, manter ou refatorar o projeto **Bancada Smart (SIG - Bancada 4.0)**.

---

## 📚 Índice da Base Documental

Antes de realizar alterações estruturais ou evoluir o sistema, consulte os documentos de referência do projeto:

1. **[`CONTEXT.md`](./CONTEXT.md)** — Visão geral consolidada do sistema pós-refatoração, stack, estrutura de pacotes, serviços, mappers binários e status dos 18 bugs resolvidos.
2. **[`docs/PLC_PROTOCOL_AND_MEMORY_MAP.md`](./docs/PLC_PROTOCOL_AND_MEMORY_MAP.md)** — **Especificação de Hardware (INTOCÁVEL)**: protocolo S7 via TCP/IP, mapa exato de bytes/bits dos Data Blocks (DBs) dos 4 CLPs Siemens, estrutura do buffer de 60 bytes de pedidos, máquinas de estado de handshake e API do ESP32.
3. **[`docs/KNOWN_BUGS_AND_ISSUES.md`](./docs/KNOWN_BUGS_AND_ISSUES.md)** — Catálogo detalhado e histórico de resolução dos 18 bugs, condições de corrida e dívidas técnicas mapeados no código original.
4. **[`docs/REFACTORING_BLUEPRINT.md`](./docs/REFACTORING_BLUEPRINT.md)** — Plano diretor de refatoração em 6 fases (concluído), arquitetura modular (`StationHandlers`, `Mappers`, `BancadaStateStore`, `OrderOrchestratorService`, `InventoryService`) e estratégia de testes com `InMemorySimulatedPlcClient`.

---

## 🚨 Invariantes de Hardware e Negócio (NUNCA QUEBRE ESTAS REGRAS)

A aplicação conversa com **CLPs Siemens reais (S7-1200/S7-1500)** e um **microcontrolador ESP32** já programados em Ladder/TIA Portal. O firmware dos CLPs **não será alterado**. Portanto, qualquer refatoração do software **DEVE preservar rigorosamente**:

1. **Endereçamento e Tamanhos dos Data Blocks (S7 TCP Porta 102):**
   - **Estoque (`*.10`):** `DB9`, leitura de `111 bytes` a partir do offset `0`.
   - **Processo (`*.20`):** `DB2`, leitura de `9 bytes` a partir do offset `0`.
   - **Montagem (`*.30`):** `DB57`, leitura de `9 bytes` a partir do offset `0`.
   - **Expedição (`*.40`):** `DB9`, leitura de `48 bytes` a partir do offset `0`.
2. **Formato do Buffer de Pedido (`DB9`, offset `2`, `60 bytes` no CLP Estoque):**
   - Deve conter exatamente `30` inteiros de 16 bits (`short`) em **Big-Endian** (`ByteOrder.BIG_ENDIAN`).
   - Índices `0..8` (Andar 1), `9..17` (Andar 2), `18..26` (Andar 3), `27` (Nº da OP), `28` (Total de blocos: 1..3), `29` (Posição na expedição: 1..12).
3. **Protocolo de Handshake (StartOP / FinishOP / CancelOP ↔ RecebidoOP):**
   - Os CLPs aguardam confirmação da aplicação web (`RecebidoOP = true`) quando sobem `startOP` ou `finishOP`, e aguardam o reset (`RecebidoOP = false`) quando `startOP`, `finishOP` e `cancelOP` voltam para `false`.
4. **Identificadores Fixos de Armazém:**
   - `Storage` com `id = 1`: **Estoque** (capacidade fixa = `28` posições, numeradas de `1` a `28`).
   - `Storage` com `id = 2`: **Expedição** (capacidade fixa = `12` posições, numeradas de `1` a `12`).
5. **Codificação de Cores e Padrões:**
   - Cores de Bloco/Tampa: `0` = Vazio/Livre, `1` = Preto, `2` = Vermelho, `3` = Azul.
   - Cores de Lâmina: `0` = Nenhum, `1` = Vermelho, `2` = Azul, `3` = Amarelo, `4` = Verde, `5` = Preto, `6` = Branco.
   - Padrões de Lâmina: `0` = Nenhum, `1` = Casa, `2` = Navio, `3` = Estrela.
6. **Comunicação com ESP32 (Tampa):**
   - Requisição `POST http://<ip-esp32>:80/api/move_pos` com header `Content-Type: application/x-www-form-urlencoded` e body `pos={1|2|3}`. Resposta válida contém `"Ok"` ou `"Posição selecionada"`.

---

## 🛠️ Comandos de Build, Execução e Teste

O projeto Maven fica em `AppSmartWeb/appsmartweb`. Sempre execute comandos Maven a partir desse diretório:

```bash
# Compilar o projeto (sem rodar testes)
./mvnw clean compile

# Executar testes automatizados
./mvnw test

# Iniciar a aplicação em modo desenvolvimento (porta 8090)
./mvnw spring-boot:run

# Gerar pacote JAR
./mvnw clean package -DskipTests
```

No Windows PowerShell:
```powershell
cd "AppSmartWeb\appsmartweb"
.\mvnw.cmd spring-boot:run
```

---

## 🧭 Diretrizes para Modificação e Refatoração

1. **Proibido Uso de Estado Global Não-Sincronizado:**
   - O código legado utiliza campos `public static` e variáveis de instância sem sincronização em `SmartService`. Ao refatorar, encapsule o estado das estações em objetos imutáveis (`record`) ou classes thread-safe (`AtomicReference`, `volatile`, locks explícitos).
2. **Sincronização de Socket S7 (`S7Client` / `PlcConnector`):**
   - Uma conexão TCP S7 por CLP **não suporta leituras e escritas simultâneas no mesmo socket sem lock**. Toda operação de leitura/escrita em um mesmo `PlcConnector` deve ser serializada (`synchronized` ou fila de comandos por CLP).
3. **Proibido Auto-Chamada HTTP Interna (`RestTemplate` para `localhost`):**
   - O código legado tenta chamar `http://localhost:8080/estoque/salvar` e `/expedicao/salvar` de dentro do `SmartService`. Substitua isso por chamadas diretas aos métodos de serviço/repositório transacionais (`BlockService` / `BlockRepository`).
4. **Configuração Externalizada:**
   - Nunca adicione IPs (`10.74.241.x`), portas ou senhas hardcoded em classes Java. Utilize `@ConfigurationProperties` mapeadas no `application.properties` / variáveis de ambiente.
5. **Logs Estruturados:**
   - Substitua `System.out.println` e `e.printStackTrace()` por `SLF4J` (`private static final Logger log = LoggerFactory.getLogger(...)`).
6. **Preservação de Compatibilidade ou Migração Limpa:**
   - Caso altere contratos de API REST (`/api/estado`, `/start-leituras`, `/stop-leituras`, `/smart/ping`, `/pedidoTeste`, `/api/moverTampa`), atualize simultaneamente os scripts JS correspondentes em `src/main/resources/static/js/` e templates em `src/main/resources/templates/`.

---

## ✅ Checklist Rápido antes de Concluir Qualquer Tarefa

- [ ] O código compila sem erros via `mvnw clean compile`?
- [ ] Os offsets de DB, bytes e bits do protocolo S7 continuam idênticos a [`docs/PLC_PROTOCOL_AND_MEMORY_MAP.md`](./docs/PLC_PROTOCOL_AND_MEMORY_MAP.md)?
- [ ] A ordem de bytes continua `ByteOrder.BIG_ENDIAN` para todas as palavras de 16 bits e floats enviados aos CLPs?
- [ ] O acesso concorrente às conexões dos 4 CLPs está protegido contra race conditions?
- [ ] A documentação em `docs/` e `CONTEXT.md` foi atualizada caso algum contrato ou arquitetura tenha mudado?