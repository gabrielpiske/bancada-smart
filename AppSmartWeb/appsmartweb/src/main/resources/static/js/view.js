function verBlocosMontados() {
    var andares = "3";

    document.getElementById("tampa").style.zIndex = "22";
    
    document.getElementById("lamina1-2").style.zIndex = "21";
    document.getElementById("lamina1-1").style.zIndex = "20";
    document.getElementById("bloco-1").style.zIndex = "19";
    document.getElementById("lamina1-3").style.zIndex = "18";
    document.getElementById("padrao1-3").style.zIndex = "17";
    document.getElementById("padrao1-3").style.zIndex = "16";
    document.getElementById("padrao1-3").style.zIndex = "15";
    
    document.getElementById("lamina2-2").style.zIndex = "14";
    document.getElementById("lamina2-1").style.zIndex = "13";
    document.getElementById("bloco-2").style.zIndex = "12";
    document.getElementById("lamina2-3").style.zIndex = "11";
    document.getElementById("padrao2-3").style.zIndex = "10";
    document.getElementById("padrao2-3").style.zIndex = "9";
    document.getElementById("padrao2-3").style.zIndex = "8";
    
    document.getElementById("lamina3-2").style.zIndex = "7";
    document.getElementById("lamina3-1").style.zIndex = "6";
    document.getElementById("bloco-3").style.zIndex = "5";
    document.getElementById("lamina3-3").style.zIndex = "4";
    document.getElementById("padrao3-3").style.zIndex = "3";
    document.getElementById("padrao3-3").style.zIndex = "2";
    document.getElementById("padrao3-3").style.zIndex = "1";
    
    document.getElementById("tampa").style.top = "42px";

    var alturaimagem = document.getElementById("bloco-1").offsetHeight;

    var fatorMultiplicador = 0.45;

    var altura1 = 1 * fatorMultiplicador * alturaimagem + "px";
    var altura2 = 2 * fatorMultiplicador * alturaimagem + "px";
    var altura3 = 3 * fatorMultiplicador * alturaimagem + "px";

    switch(andares) {
        case "3":
            document.getElementById("bloco-1").style.top = altura1;
            document.getElementById("lamina1-1").style.top = altura1;
            document.getElementById("lamina1-2").style.top = altura1;
            document.getElementById("lamina1-3").style.top = altura1;
            document.getElementById("padrao1-1").style.top = altura1;
            document.getElementById("padrao1-2").style.top = altura1;
            document.getElementById("padrao1-3").style.top = altura1;

            document.getElementById("bloco-2").style.top = altura2;
            document.getElementById("lamina2-1").style.top = altura2;
            document.getElementById("lamina2-2").style.top = altura2;
            document.getElementById("lamina2-3").style.top = altura2;
            document.getElementById("padrao2-1").style.top = altura2;
            document.getElementById("padrao2-2").style.top = altura2;
            document.getElementById("padrao2-3").style.top = altura2;

            document.getElementById("bloco-3").style.top = altura3;
            document.getElementById("lamina3-1").style.top = altura3;
            document.getElementById("lamina3-2").style.top = altura3;
            document.getElementById("lamina3-3").style.top = altura3;
            document.getElementById("padrao3-1").style.top = altura3;
            document.getElementById("padrao3-2").style.top = altura3;
            document.getElementById("padrao3-3").style.top = altura3;
    }
}

window.onload = verBlocosMontados;