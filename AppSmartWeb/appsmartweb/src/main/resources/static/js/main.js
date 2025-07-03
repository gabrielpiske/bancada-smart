import {saveClpStatusToLocalStorage, showMessage} from './utils.js';

export function getValueIP() {
    let faixa = document.getElementById("faixa").value;
    let parse = faixa.split('.');
    let ipFormat;

    let faixaUsada = document.getElementById("hostIpEstoque").value;
    let parseUsado = faixaUsada.split('.');
    let ipUsado;

    if (parse.length !== 3 && parse.length !== 4 || parse.some(p => isNaN(p) || p < 0 || p > 255)) {
        showMessage("error", "IP inválido!");
        return;
    }

    ipUsado = `${parseUsado[0]}.${parseUsado[1]}.${parseUsado[2]}`;

    ipFormat = parse.length === 4 ? `${parse[0]}.${parse[1]}.${parse[2]}` : faixa;

    saveClpStatusToLocalStorage({
        ip: ipFormat
    });

    //Textos
    document.getElementById("ipEstoque").textContent = `${ipFormat}.10`;
    document.getElementById("ipProcesso").textContent = `${ipFormat}.20`;
    document.getElementById("ipMontagem").textContent = `${ipFormat}.30`;
    document.getElementById("ipExpedicao").textContent = `${ipFormat}.40`;

    //Inputs
    document.getElementById("hostIpEstoque").value = `${ipFormat}.10`;
    document.getElementById("hostIpProcesso").value = `${ipFormat}.20`;
    document.getElementById("hostIpMontagem").value = `${ipFormat}.30`;
    document.getElementById("hostIpExpedicao").value = `${ipFormat}.40`;

    if(ipUsado !== ipFormat) {
        showMessage("success", "Endereços IP atualizados!");
    } else{
        showMessage("error", "Os endereços IP já estão assim!");
    }
}