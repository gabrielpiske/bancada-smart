# Bancada Smart — Sistema de Gestão Industrial (SIG 4.0)

![Bancada Smart](https://github.com/user-attachments/assets/ca1f99a5-4681-41d4-ab31-2b9702144ae5)

## 📝 Descrição do Projeto
Trabalho final do 3º semestre do Curso Técnico em Desenvolvimento de Sistemas do SENAI Timbó, consistindo em uma aplicação web SCADA/MES que gerencia e supervisiona a **Bancada Smart 4.0**, simulando o controle em tempo real de uma linha de produção industrial com quatro estações: **Estoque**, **Processo**, **Montagem** e **Expedição**, além de um seletor automatizado de tampas via **ESP32**.

---

## 🛠️ Funcionalidades Principais
- **Controle em tempo real** das 4 estações industriais (ciclo de leitura a cada `800ms`).
- **Comunicação bidirecional** com CLPs Siemens (S7-1200 / S7-1500) via protocolo S7 nativo sobre TCP/IP (porta `102`).
- **Acionamento IoT** de servomotor via ESP32 (HTTP REST) para seleção de tampas.
- **Loja de Pedidos interativa** com montagem visual empilhada de até 3 andares (cor do bloco, 3 lâminas, 3 padrões e rotação 180°).
- **Gestão visual de Magazine** para o Estoque (28 posições) e Expedição (12 posições).
- **Dashboard de Monitoramento** das variáveis e flags de operação de cada CLP.

---

## 🖥️ Tecnologias Utilizadas
| Categoria         | Tecnologias                                                                 |
|-------------------|----------------------------------------------------------------------------|
| **Backend**       | Java 17, Spring Boot 3.3.4, Spring Data JPA, Spring Web                    |
| **Frontend**      | HTML5, CSS3, JavaScript (ES6 Modules + jQuery), Thymeleaf                  |
| **Banco de Dados**| MySQL 8.0 (Dev) / Microsoft SQL Server (Prod) / H2 In-Memory (Test)        |
| **Comunicação**   | Protocolo S7 Siemens (ISO-on-TCP), Simulador S7 em Memória, HTTP REST (ESP32) |
| **Ferramentas**   | Maven, JUnit 5, MockMvc, Git, VS Code, Eclipse, TIA Portal                 |

---

## 📚 Documentação Técnica e Base para Agentes de IA

Este repositório conta com uma base documental completa projetada para desenvolvedores e agentes de IA (manutenção, diagnóstico e evolução guiada por IA):

- 🤖 **[`AGENTS.md`](./AGENTS.md)** — Diretrizes operacionais, invariantes de hardware e regras obrigatórias para agentes de IA.
- 🧭 **[`CONTEXT.md`](./CONTEXT.md)** — Visão consolidada da arquitetura atual (pós-refatoração), estrutura de pacotes, modelo de dados e status dos bugs corrigidos.
- 🔌 **[`docs/PLC_PROTOCOL_AND_MEMORY_MAP.md`](./docs/PLC_PROTOCOL_AND_MEMORY_MAP.md)** — Especificação binária do protocolo S7, mapa byte-a-byte dos Data Blocks (`DB9`, `DB2`, `DB57`), estrutura do payload de 60 bytes de pedidos e máquinas de estado de handshake.
- 🐛 **[`docs/KNOWN_BUGS_AND_ISSUES.md`](./docs/KNOWN_BUGS_AND_ISSUES.md)** — Catálogo detalhado e histórico de resolução dos 18 bugs, condições de corrida e dívidas técnicas mapeados no sistema legado.
- 🏗️ **[`docs/REFACTORING_BLUEPRINT.md`](./docs/REFACTORING_BLUEPRINT.md)** — Plano diretor de refatoração do Backend dividido em 6 fases incrementais (100% concluído e validado por testes).
- 🎨 **[`docs/FRONTEND_REFACTORING_BLUEPRINT.md`](./docs/FRONTEND_REFACTORING_BLUEPRINT.md)** — Arquitetura do Frontend 4.0: Design System SENAI, Visualizador 3D de Blocos (`Block3DViewer`), Animação 3D Procedural da Bancada (`BenchAnimator3D`) e Créditos Editáveis.

---

## 🚀 Como Executar Localmente

### Opção 1: Executar Testes Automatizados (Sem necessidade de MySQL ou CLPs físicos)
```bash
cd AppSmartWeb/appsmartweb
./mvnw test
```

### Opção 2: Executar a Aplicação Completa (MySQL)
1. **Pré-requisitos:** Java 17+ e MySQL 8.0 rodando em `localhost:3306`.
2. **Criar o schema no MySQL:**
   ```sql
   CREATE SCHEMA IF NOT EXISTS smartweb;
   ```
   *(Ao iniciar a aplicação com `ddl-auto=update`, o Hibernate criará as tabelas e o `DataInitializer` populará os registros de `Stock` e `Expedition`).*
3. **Executar a aplicação:**
   ```bash
   cd AppSmartWeb/appsmartweb
   ./mvnw spring-boot:run
   ```
   *(Dica: para testar sem os CLPs físicos conectados na rede, defina `BANCADA_PLC_SIMULATION_MODE=true` ou `bancada.plc.simulation-mode=true`).*
4. **Acessar no navegador:** `http://localhost:8090`

---

## 👤 Autores

<p align="center">
  <b>Gabriel Schweder Piske - Desenvolvedor Backend</b>  
  <br/>Cursando Análise e Desenvolvimento de Sistemas e Técnico em Desenvolvimento de Sistemas
  <br/><br/>
  <a href="https://www.linkedin.com/in/gabriel-piske/"><img src="https://img.shields.io/badge/LinkedIn-0077B5?style=for-the-badge&logo=linkedin&logoColor=white" /></a>
  <a href="https://github.com/gabrielpiske"><img src="https://img.shields.io/badge/GitHub-100000?style=for-the-badge&logo=github&logoColor=white" /></a>
</p>

<p align="center">
  <b>Julio Henrique Busarello - Desenvolvedor Frontend</b>  
  <br/>Técnico em Desenvolvimento de Sistemas  
  <br/><br/>
  <a href="https://www.linkedin.com/in/j%C3%BAlio-henrique-busarello-614259212/"><img src="https://img.shields.io/badge/LinkedIn-0077B5?style=for-the-badge&logo=linkedin&logoColor=white" /></a>
  <a href="https://github.com/JulioBusarello"><img src="https://img.shields.io/badge/GitHub-100000?style=for-the-badge&logo=github&logoColor=white" /></a>
</p>