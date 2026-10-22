# Plano Diretor e Arquitetura do Frontend (SIG — Bancada Smart 4.0)

> **Status de Execução:** ✅ **CONCLUÍDO (Fases 1 a 5 implementadas e validadas em 2026-10-21 — Sistema 100% 2D com Correções de Camadas e Empilhamento).**  
> **Propósito:** Documentar a arquitetura visual 2D, os fluxos de usuário, as correções da montagem isométrica 2D em camadas e a preservação da identidade SENAI Timbó no frontend da aplicação **Bancada Smart 4.0**.

---

## 1. Visão Geral da Arquitetura Frontend

O frontend foi reestruturado sob o paradigma de **Supervisório SCADA Indústria 4.0 (100% 2D)**, combinando:

1. **Identidade Visual SENAI 4.0 (`design-system.css`):**
   - Paleta baseada no Azul Institucional SENAI (`#006BB7` / `#004A80` / `#38BDF8`) sobre superfícies escuras em *Glassmorphism* (`#0B1120`, `#0F172A`, `#1E293B`).
   - Tipografia dupla: `Inter` para interface e `JetBrains Mono` para endereços IP, Data Blocks, cronômetros e telemetria CLP.
2. **Montagem 2D Isométrica em Camadas (`store.js` e `view.js`):**
   - Renderiza blocos (`rBlocoCor0..3.png`), lâminas (`lamina1..3-1..6.png`), padrões (`padrao1..3-1..2.png`) e tampas (`rTampa0..3.png`) em camadas PNG (`320x145` / `960x434`) com contextos de empilhamento (`z-index`) isolados por andar (`.modal-bloco1..3` e `.bloco1..3`).
   - Corrige bugs históricos de sobreposição de lâminas entre andares, requisições 404 para `padrao*-3.png` inexistentes, resquícios visuais ao desmarcar lâminas/padrões e desalinhamento vertical entre tampa e blocos empilhados.
3. **Visualização 2D HD da Bancada com Indicadores Responsivos (`main.html` e `view.html`):**
   - Na aba **Conexão (`/`)**, exibe a fotografia oficial 2D da bancada (`banqueida.png`).
   - Na aba **Produção Ao Vivo (`/view`)**, exibe a fotografia 2D em alta definição da linha de produção (`bancada-hd.png`) com indicadores luminosos posicionados em porcentagem (`%`) sobre as 4 estações (**1. Estoque**, **2. Processo**, **3. Montagem** e **4. Expedição**) sincronizados em tempo real com `/api/estado`.
4. **Rodapé Institucional SENAI com Créditos Mantidos e Editáveis (`fragments.html` + `utils.js`):**
   - Presente em todas as 5 páginas (`/`, `/estoque`, `/loja`, `/view`, `/monitoramento`), mantendo os créditos a **Gabriel Schweder Piske** e **Julio Henrique Busarello** (`Técnico em Desenvolvimento de Sistemas — © SENAI Timbó`) e permitindo customização via modal persistido em `localStorage`.

---

## 2. Mapa de Arquivos do Frontend (`src/main/resources/`)

| Arquivo | Responsabilidade |
|---|---|
| `templates/fragments.html` | Header SENAI com navegação nas 5 rotas (`/`, `/estoque`, `/loja`, `/view`, `/monitoramento`), dropdown de ping S7 e rodapé com créditos editáveis. |
| `templates/main.html` | Centro de Conexão S7 (`.10`, `.20`, `.30`, `.40`) + Imagem 2D Oficial da Bancada (`banqueida.png`). |
| `templates/stock.html` | Painel de KPIs de ocupação por cor + Magazine de Estoque (28 posições) e Expedição (12 posições). |
| `templates/store.html` | Configurador 2D de pedidos (1 a 3 andares + tampa ESP32) e Modal 2D de Inspeção Final Empilhada. |
| `templates/view.html` | Supervisório de Produção Ao Vivo: Bloco Montado 2D empilhado + Linha de Produção 2D (`bancada-hd.png`) + Cronômetros. |
| `templates/monitor.html` | Dashboard de Telemetria CLP (~114 variáveis de `/api/estado`) com busca instantânea, filtros rápidos e pausa de leitura. |
| `static/css/design-system.css` | Tokens CSS da identidade SENAI 4.0, utilitários Glassmorphism e badges. |
| `static/css/fragments.css` | Estilos do cabeçalho sticky, menu de navegação, dropdown de ping e modal de créditos do rodapé. |
| `static/css/main.css` | Layout da tela inicial de conexão S7 e vitrine 2D da bancada. |
| `static/css/stock.css` | Estilos dos KPIs de armazém, barra de distribuição por cor e grids 2D de Estoque e Expedição. |
| `static/css/store.css` | Estilos dos cards de andares, seletores, camadas `z-index` e modal 2D empilhado da Loja. |
| `static/css/view.css` | Layout de Produção Ao Vivo, contextos de empilhamento `.bloco1..3`, hotspots responsivos em `%` e cards de cronômetros das 4 estações. |
| `static/css/monitor.css` | Estilos da barra de busca/filtros e grades de telemetria booleana/numérica do monitoramento. |
| `static/js/utils.js` | Toasts, persistência em `localStorage`, ativação de rota no menu e gerenciador de créditos editáveis. |
| `static/js/connection.js` | Gerenciamento de ping S7 (`/smart/ping`, `/start-leituras`, `/stop-leituras`) e indicadores globais. |
| `static/js/main.js` | Configuração de faixa de IP na tela inicial. |
| `static/js/stock.js` | Seleção/arraste de slots no modo de edição do estoque e cálculo dinâmico dos KPIs de ocupação. |
| `static/js/store.js` | Orquestração dos cards de andares, renderização 2D em camadas, giro 180° (`spin`), envio da tampa ao ESP32 e submissão sequencial do pedido. |
| `static/js/view.js` | Sincronismo em tempo real com `/api/estado`, cronômetros por estação e visualização 2D empilhada do pedido. |
| `static/js/monitor.js` | Polling de `/api/estado`, contadores de sinais `TRUE` e filtragem instantânea das 114 variáveis. |
