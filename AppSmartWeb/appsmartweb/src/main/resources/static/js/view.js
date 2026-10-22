import { initNavigationAndCredits, showMessage } from './utils.js';

function verBlocosMontados() {
    const bloco1 = document.getElementById("bloco1");
    const bloco2 = document.getElementById("bloco2");
    const bloco3 = document.getElementById("bloco3");
    const tampa = document.getElementById("tampa");

    const lamina1 = Array.from(document.querySelectorAll('[id^="lamina1-"]'));
    const lamina2 = Array.from(document.querySelectorAll('[id^="lamina2-"]'));
    const lamina3 = Array.from(document.querySelectorAll('[id^="lamina3-"]'));

    const padrao1 = Array.from(document.querySelectorAll('[id^="padrao1-"]'));
    const padrao2 = Array.from(document.querySelectorAll('[id^="padrao2-"]'));
    const padrao3 = Array.from(document.querySelectorAll('[id^="padrao3-"]'));

    if (!bloco1 || !bloco2 || !bloco3 || !tampa) return;

    const montagem = getBlocosMontagem();
    const b1Color = montagem.cor1 || "3";
    const b2Color = montagem.cor2 || "";
    const b3Color = montagem.cor3 || "";

    const laminas = montagem.laminas || {};
    const padroes = montagem.padroes || {};

    tampa.style.top = "0px";
    const tampaColor = montagem.tampa || "1";
    tampa.src = `assets/tampas/rTampa${tampaColor}.png`;

    const applyPositioning = () => {
        const container = document.querySelector(".pedido-view .modal-bloco-container");
        const containerWidth = container?.clientWidth || 350;
        const alturaImagem = bloco1.offsetHeight || (containerWidth * (145 / 320));
        posicionarBlocos(alturaImagem, {
            bloco1, bloco2, bloco3,
            lamina1, lamina2, lamina3,
            padrao1, padrao2, padrao3
        });
    };

    bloco1.onload = applyPositioning;

    bloco1.src = b1Color ? `assets/blocos/rBlocoCor${b1Color}.png` : '#';
    bloco2.src = b2Color ? `assets/blocos/rBlocoCor${b2Color}.png` : '#';
    bloco3.src = b3Color ? `assets/blocos/rBlocoCor${b3Color}.png` : '#';

    const isSpun = Boolean($(`#pedido-view`).data("isSpun"));
    updateLaminaImages(isSpun, laminas, padroes);
    applyPositioning();
}

function posicionarBlocos(altura, elementos) {
    const { bloco1, bloco2, bloco3, lamina1, lamina2, lamina3, padrao1, padrao2, padrao3 } = elementos;

    const fator = 0.445;
    const dif = 0.255 * altura;

    const altura1 = `${(1 * fator * altura) - dif}px`;
    const altura2 = `${(2 * fator * altura) - dif}px`;
    const altura3 = `${(3 * fator * altura) - dif}px`;

    bloco1.style.top = altura1;
    lamina1.forEach(el => el.style.top = altura1);
    padrao1.forEach(el => el.style.top = altura1);

    bloco2.style.top = altura2;
    lamina2.forEach(el => el.style.top = altura2);
    padrao2.forEach(el => el.style.top = altura2);

    bloco3.style.top = altura3;
    lamina3.forEach(el => el.style.top = altura3);
    padrao3.forEach(el => el.style.top = altura3);
}

export function spin() {
    const montagem = getBlocosMontagem();

    const $view = $(`#pedido-view`);
    const isSpun = !$view.data("isSpun");
    $view.toggleClass("spin", isSpun).data("isSpun", isSpun);

    const laminas = montagem.laminas || {};
    const padroes = montagem.padroes || {};

    updateLaminaImages(isSpun, laminas, padroes);
}

function updateLaminaImages(isSpun, laminas, padroes) {
    for (let id = 1; id <= 3; id++) {
        const $lamina1 = $(`#lamina${id}-1`);
        const $lamina2 = $(`#lamina${id}-2`);
        const $lamina3 = $(`#lamina${id}-3`);
        const $padrao1 = $(`#padrao${id}-1`);
        const $padrao2 = $(`#padrao${id}-2`);
        const $padrao3 = $(`#padrao${id}-3`);

        const l = laminas?.[id.toString()] || [];
        const p = padroes?.[id.toString()] || [];

        if (isSpun) {
            $lamina1.attr("src", l[2] ? `assets/laminas/lamina1-${l[2]}.png` : "#");
            $lamina3.attr("src", l[0] ? `assets/laminas/lamina3-${l[0]}.png` : "#");

            $padrao1.attr("src", p[0] ? `assets/padroes/padrao${p[0]}-1.png` : "#").prop("hidden", true);
            $padrao3.attr("src", p[2] ? `assets/padroes/padrao${p[2]}-1.png` : "#").prop("hidden", false);
        } else {
            $lamina1.attr("src", l[0] ? `assets/laminas/lamina1-${l[0]}.png` : "#");
            $lamina3.attr("src", l[2] ? `assets/laminas/lamina3-${l[2]}.png` : "#");

            $padrao1.attr("src", p[0] ? `assets/padroes/padrao${p[0]}-1.png` : "#").prop("hidden", false);
            $padrao3.attr("src", p[2] ? `assets/padroes/padrao${p[2]}-1.png` : "#").prop("hidden", true);
        }

        $lamina2.attr("src", l[1] ? `assets/laminas/lamina2-${l[1]}.png` : "#");
        $padrao2.attr("src", p[1] ? `assets/padroes/padrao${p[1]}-2.png` : "#").prop("hidden", false);
    }
}

function getBlocosMontagem() {
    try {
        const data = localStorage.getItem("montagem");
        return JSON.parse(data) || {};
    } catch (e) {
        console.error("Erro ao ler montagem do localStorage", e);
        return {};
    }
}

function formatTime(totalSeconds) {
    const hrs = String(Math.floor(totalSeconds / 3600)).padStart(2, "0");
    const mins = String(Math.floor((totalSeconds % 3600) / 60)).padStart(2, "0");
    const secs = String(totalSeconds % 60).padStart(2, "0");
    return `${hrs}:${mins}:${secs}`;
}

function setTimerDisplay(elementId, seconds) {
    const container = document.getElementById(elementId);
    if (!container) return;
    const p = container.querySelector("p") || container;
    p.textContent = formatTime(seconds);
}

function updateIndicatorClass(timerObj, status) {
    const el = document.getElementById(timerObj.indicatorId);
    const card = document.getElementById(timerObj.cardId);
    const pill = document.getElementById(timerObj.pillId);

    [el, card].forEach(node => {
        if (!node) return;
        node.classList.remove("espera", "ativo", "usada");
        if (status === 1) node.classList.add("ativo");
        else if (status === 2) node.classList.add("usada");
        else node.classList.add("espera");
    });

    if (pill) {
        pill.textContent = status === 1 ? "Em Execução" : status === 2 ? "Concluído" : "Em Espera";
    }
}

const stationTimers = {
    est: { seconds: 0, running: false, elementId: "tempo-estoque", indicatorId: "indicador-estoque", cardId: "card-status-estoque", pillId: "pill-estoque" },
    pro: { seconds: 0, running: false, elementId: "tempo-processo", indicatorId: "indicador-processo", cardId: "card-status-processo", pillId: "pill-processo" },
    mon: { seconds: 0, running: false, elementId: "tempo-montagem", indicatorId: "indicador-montagem", cardId: "card-status-montagem", pillId: "pill-montagem" },
    exp: { seconds: 0, running: false, elementId: "tempo-expedicao", indicatorId: "indicador-expedicao", cardId: "card-status-expedicao", pillId: "pill-expedicao" }
};

function updateStationTimer(timerObj, recebidoOp, finishOp, statusValue) {
    updateIndicatorClass(timerObj, statusValue);
    if ((recebidoOp || statusValue === 1) && !finishOp && statusValue !== 2) {
        timerObj.running = true;
    } else if (finishOp || statusValue === 2) {
        timerObj.running = false;
    }
}

setInterval(() => {
    let anyRunning = false;
    Object.values(stationTimers).forEach((t) => {
        if (t.running) {
            t.seconds++;
            anyRunning = true;
            setTimerDisplay(t.elementId, t.seconds);
        }
    });
    if (anyRunning) {
        const total = stationTimers.est.seconds + stationTimers.pro.seconds + stationTimers.mon.seconds + stationTimers.exp.seconds;
        setTimerDisplay("tempo-total", total);
    }
}, 1000);

function iniciarProcesso(data) {
    updateStationTimer(stationTimers.est, data.recebidoOpEst, data.finishOPEst, data.statusEstoque);
    updateStationTimer(stationTimers.pro, data.recebidoOpPro, data.finishOPPro, data.statusProcesso);
    updateStationTimer(stationTimers.mon, data.recebidoOpMon, data.finishOPMon, data.statusMontagem);
    updateStationTimer(stationTimers.exp, data.recebidoOpExp, data.finishOPExp, data.statusExpedicao);

    const opLabel = document.getElementById("op-active-label");
    if (opLabel) {
        const opNum = data.numeroPedidoEst || data.numeroOPEst || 0;
        opLabel.textContent = opNum > 0
            ? `OP #${opNum} • ${data.pedidoEmCurso ? 'Em Execução na Linha' : 'Registrada no CLP'}`
            : "Sincronizado com /api/estado";
    }
}

async function getFlags() {
    try {
        const response = await fetch('/api/estado');
        if (!response.ok) throw new Error('Erro na resposta: ' + response.status);
        const data = await response.json();
        iniciarProcesso(data);
    } catch (error) {
        console.error('Erro ao buscar estado: ', error);
    }
}

document.addEventListener("DOMContentLoaded", () => {
    initNavigationAndCredits();
    verBlocosMontados();
    getFlags();
    setInterval(getFlags, 1000);

    // Feedback quando redirecionado da Loja após envio do pedido
    const params = new URLSearchParams(window.location.search);
    if (params.has("success")) {
        showMessage("success", "Pedido enviado aos CLPs! Acompanhe a produção em tempo real.");
    }
});

window.spin = spin;