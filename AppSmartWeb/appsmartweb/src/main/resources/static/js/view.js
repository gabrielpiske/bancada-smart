var isSpun = false;

function verBlocosMontados() {
    const bloco1 = document.getElementById("bloco1");
    const bloco2 = document.getElementById("bloco2");
    const bloco3 = document.getElementById("bloco3");

    const tampa = document.getElementById("tampa");

    const lamina1 = Array.from(document.querySelectorAll('[id^="lamina1"]'));
    const lamina2 = Array.from(document.querySelectorAll('[id^="lamina2"]'));
    const lamina3 = Array.from(document.querySelectorAll('[id^="lamina3"]'));

    const padrao1 = Array.from(document.querySelectorAll('[id^="padrao1"]'));
    const padrao2 = Array.from(document.querySelectorAll('[id^="padrao2"]'));
    const padrao3 = Array.from(document.querySelectorAll('[id^="padrao3"]'));

    const montagem = getBlocosMontagem();
    console.log(montagem);

    const b1Color = montagem.cor1;
    const b2Color = montagem.cor2;
    const b3Color = montagem.cor3;

    const laminas = montagem.laminas || {};
    const padroes = montagem.padroes || {};

    bloco1.style.zIndex = "4";
    bloco2.style.zIndex = "3";
    bloco3.style.zIndex = "2";
    tampa.style.top = "0px";

    bloco1.onload = () => {
        const alturaImagem = bloco1.offsetHeight;
        console.log("Altura imagem:", alturaImagem);

        posicionarBlocos(alturaImagem, {
            bloco1, bloco2, bloco3,
            lamina1, lamina2, lamina3,
            padrao1, padrao2, padrao3
        });
    };

    bloco1.src = b1Color ? `assets/bloco/rBlocoCor${b1Color}.png` : '#';
    bloco2.src = b2Color ? `assets/bloco/rBlocoCor${b2Color}.png` : '#';
    bloco3.src = b3Color ? `assets/bloco/rBlocoCor${b3Color}.png` : '#';

    lamina1.forEach((el, i) => {
        const cor = laminas["1"]?.[i];
        el.src = cor ? `assets/laminas/lamina${i + 1}-${cor}.png` : "#";
    });

    lamina2.forEach((el, i) => {
        const cor = laminas["2"]?.[i];
        el.src = cor ? `assets/laminas/lamina${i + 1}-${cor}.png` : "#";
    });

    lamina3.forEach((el, i) => {
        const cor = laminas["3"]?.[i];
        el.src = cor ? `assets/laminas/lamina${i + 1}-${cor}.png` : "#";
    });

    padrao1.forEach((el, i) => {
        const cor = padroes["1"]?.[i];
        el.src = cor ? `assets/padroes/padrao${i + 1}-${cor}.png` : "#";
    });

    padrao2.forEach((el, i) => {
        const cor = padroes["2"]?.[i];
        el.src = cor ? `assets/padroes/padrao${i + 1}-${cor}.png` : "#";
    });

    padrao3.forEach((el, i) => {
        const cor = padroes["3"]?.[i];
        el.src = cor ? `assets/padroes/padrao${i + 1}-${cor}.png` : "#";
    });

}

function posicionarBlocos(altura, elementos) {
    const { bloco1, bloco2, bloco3, lamina1, lamina2, lamina3, padrao1, padrao2, padrao3 } = elementos;

    const fator = 0.445;
    const dif = 40;

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

function spin() {
    const montagem = getBlocosMontagem();

    const $view = $(`#pedido-view`);
    const isSpun = !$view.data("isSpun");
    $view.toggleClass("spin").data("isSpun", isSpun);

    const laminas = montagem.laminas;
    const padroes = montagem.padroes;

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

        // Atualiza as lâminas
        if (isSpun) {
            // Inverte as lâminas 1 e 3
            $lamina1.attr("src", l[2] ? `assets/laminas/lamina1-${l[2]}.png` : "#");
            $lamina3.attr("src", l[0] ? `assets/laminas/lamina3-${l[0]}.png` : "#");

            // Inverte padrões 1 e 3
            $padrao1.attr("src", p[2] ? `assets/padroes/padrao${p[2]}-1.png` : "#").prop("hidden", true);
            $padrao3.attr("src", p[0] ? `assets/padroes/padrao${p[0]}-1.png` : "#").prop("hidden", false);
        } else {
            // Valores normais, sem inversão
            $lamina1.attr("src", l[0] ? `assets/laminas/lamina1-${l[0]}.png` : "#");
            $lamina3.attr("src", l[2] ? `assets/laminas/lamina3-${l[2]}.png` : "#");

            $padrao1.attr("src", p[0] ? `assets/padroes/padrao${p[0]}-1.png` : "#").prop("hidden", false);
            $padrao3.attr("src", p[2] ? `assets/padroes/padrao${p[2]}-1.png` : "#").prop("hidden", true);
        }

        // Lâmina e padrão do meio (fixos)
        $lamina2.attr("src", l[1] ? `assets/laminas/lamina2-${l[1]}.png` : "#");
        $padrao2.attr("src", p[1] ? `assets/padroes/padrao${p[1]}-2.png` : "#");
    }
}


window.onload = verBlocosMontados;


function getBlocosMontagem() {
    try {
        const data = localStorage.getItem("montagem");
        return JSON.parse(data) || {};
    } catch (e) {
        console.error("Erro ao ler montagem do localStorage", e);
        return {};
    }
}

let timerEst = 0;
let intervalEst = null;

function iniciarProcesso(data) {
    const recebidoOpEst = data.recebidoOpEst;
    const finishOPEst = data.finishOPEst;
  
    if (recebidoOpEst && !finishOPEst && intervalEst === null) {
      intervalEst = setInterval(() => {
        timerEst++;
        document.getElementById("tempo-estoque").textContent = timerEst;
      }, 1000);
    }
  
    if (finishOPEst && intervalEst !== null) {
      clearInterval(intervalEst);
      intervalEst = null;
      document.getElementById("tempo-estoque").textContent = timerEst;
      timerEst = 0;
    }
  }

async function getFlags() {
    try {
        const response = await fetch('/api/estado');
        if (!response.ok) throw new Error('Erro na resposta: ' + response.status);
        const data = await response.json();
        iniciarProcesso(data);
    } catch (error) {
        console.error ('Erro ao buscar estado: ', error);
    }
}

setInterval(getFlags, 1000);

getFlags();