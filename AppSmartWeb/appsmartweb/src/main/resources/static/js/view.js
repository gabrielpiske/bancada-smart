var isSpun = false;

function verBlocosMontados() {
    var andares = "3";

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

    spin();

    bloco1.style.zIndex = "4";
    bloco2.style.zIndex = "3";
    bloco3.style.zIndex = "2";

    tampa.style.top = "0px";

    var alturaimagem = bloco1.offsetHeight;

    console.log(alturaimagem);

    const fatorMultiplicador = 0.445;
    const dif = 40;

    var altura1 = (1 * fatorMultiplicador * alturaimagem) - dif + "px";
    var altura2 = (2 * fatorMultiplicador * alturaimagem) - dif + "px";
    var altura3 = (3 * fatorMultiplicador * alturaimagem) - dif + "px";

    console.log(altura1);
    console.log(altura2);
    console.log(altura3);

    switch (andares) {
        case "3":
            bloco1.style.top = altura1;
            lamina1.forEach(el => el.style.top = altura1);
            padrao1.forEach(el => el.style.top = altura1);

            bloco2.style.top = altura2;
            lamina2.forEach(el => el.style.top = altura2);
            padrao2.forEach(el => el.style.top = altura2);

            bloco3.style.top = altura3;
            lamina3.forEach(el => el.style.top = altura3);
            padrao3.forEach(el => el.style.top = altura3);
            break;
    }
}

function spin() {
    const view = $('#pedido-view');

    const isSpun = !view.data("isSpun");

    view.toggleClass("spin").data("isSpun", !isSpun);

    const padrao1 = $('[id^="padrao"][id$="-1"]');
    const padrao3 = $('[id^="padrao"][id$="-3"]');

    padrao1.prop("hidden", !isSpun);
    padrao3.prop("hidden", isSpun);
}

window.onload = verBlocosMontados;