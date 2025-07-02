import {showSuccessMessage, showErrorMessage, showInfoMessage, saveClpStatusToLocalStorage, getClpStatusFromLocalStorage, capitalize} from './utils.js';

export let conectado = false;
let pingInterval = null;

export function connect() {
    const btn = document.getElementById("connect");
    const ips = {
        estoque: document.getElementById("hostIpEstoque").value,
        processo: document.getElementById("hostIpProcesso").value,
        montagem: document.getElementById("hostIpMontagem").value,
        expedicao: document.getElementById("hostIpExpedicao").value
    };

    let faixa = document.getElementById("hostIpEstoque").value;
    let parse = faixa.split('.');
    let ipFormat;

    ipFormat = parse.length === 4 ? `${parse[0]}.${parse[1]}.${parse[2]}` : faixa;

    if (!conectado) {
        fetch("/smart/ping", {
            method: "POST",
            headers: { "Content-Type": "application/json" },
            body: JSON.stringify(ips)
        })
            .then(res => res.json())
            .then(status => {
                saveClpStatusToLocalStorage(status);
                updateStatusUI(status);

                if (Object.values(status).every(Boolean)) {
                    return fetch("/start-leituras", {
                        method: "POST",
                        headers: { "Content-Type": "application/json" },
                        body: JSON.stringify(ips)
                    });
                } else {
                    throw new Error("Um ou mais CLPs não responderam ao ping");
                }
            })
            .then(() => {
                btn.textContent = "Desconectar";
                conectado = true;
                document.getElementById("confirm").disabled = true;
                document.getElementById("faixa").disabled = true;

                showSuccessMessage("Conexão estabelecida com sucesso!");

                saveClpStatusToLocalStorage({ativo: true, ip: ipFormat});

                pingInterval = setInterval(() => {
                    pingAndUpdate(ips);
                }, 2000);
            })
            .catch(error => {
                console.error("Erro ao conectar:", error);
                showErrorMessage(`Falha na conexão: ${error.message}`);
            });
    } else {
        fetch("/stop-leituras", { method: "POST" })
            .then(() => {
                btn.textContent = "Conectar";
                conectado = false;
                document.getElementById("confirm").disabled = false;
                document.getElementById("faixa").disabled = false;

                clearInterval(pingInterval);
                pingInterval = null;

                ["Estoque", "Processo", "Montagem", "Expedicao"].forEach(nome => {
                    const ipElement = document.getElementById(`ip${nome}`);
                    const tiElement = document.getElementById(`ti${nome}`);
                    if (ipElement) {
                        ipElement.style.color = "";
                        ipElement.style.borderColor = "";
                    }
                    if (tiElement) {
                        tiElement.style.color = "";
                    }
                });

                saveClpStatusToLocalStorage({
                    estoque: false,
                    processo: false,
                    montagem: false,
                    expedicao: false,
                    ativo: false
                });

                showInfoMessage("Conexão encerrada");
            })
            .catch(error => {
                console.error("Erro ao desconectar:", error);
                showErrorMessage(`Falha ao desconectar: ${error.message}`);
            });
    }
}

export function updateStatusUI(status) {
    const modulos = ["estoque", "processo", "montagem", "expedicao"];
    modulos.forEach(modulo => {
        const ip = document.getElementById(`ip${capitalize(modulo)}`);
        const ti = document.getElementById(`ti${capitalize(modulo)}`);
        const nav = document.getElementById(`nav-ip${capitalize(modulo)}`);
        const ok = status[modulo];

        if (ip) {
            ip.style.color = ok ? "#388E3C" : "#E74C3C";
            ip.style.borderColor = ok ? "#388E3C" : "#E74C3C";
        }
        if (ti) {
            ti.style.color = ok ? "#388E3C" : "#E74C3C";
        }
        if (nav) {
            nav.style.color = ok ? "#388E3C" : "#E74C3C";
        }
    });
}

function pingAndUpdate(ips) {
    fetch("/smart/ping", {
        method: "POST",
        headers: { "Content-Type": "application/json" },
        body: JSON.stringify(ips)
    })
        .then(res => res.json())
        .then(status => {
            updateStatusUI(status);
            saveClpStatusToLocalStorage(status);
        })
        .catch(error => {
            console.warn("Erro no ping:", error);
        });
}

function getIpsFromLocalStorage() {
    const baseIp = getClpStatusFromLocalStorage()?.ip;
    if (!baseIp) return null;

    return {
        estoque: `${baseIp}.10`,
        processo: `${baseIp}.20`,
        montagem: `${baseIp}.30`,
        expedicao: `${baseIp}.40`
    };
}

document.addEventListener("DOMContentLoaded", () => {
    const clpStatus = getClpStatusFromLocalStorage();

    if(clpStatus) {
        updateStatusUI(clpStatus);
    }

    if (clpStatus?.ip) {
        ["Estoque", "Processo", "Montagem", "Expedicao"].forEach((modulo, idx) => {
            const lastOctet = (idx + 1) * 10;
            const ipFinal = `${clpStatus.ip}.${lastOctet}`;
            const ipEl = document.getElementById(`ip${modulo}`);
            const inputEl = document.getElementById(`hostIp${modulo}`);
            if (ipEl) ipEl.textContent = ipFinal;
            if (inputEl) inputEl.value = ipFinal;
        });
    }

    if (clpStatus?.ativo === true) {
        conectado = true;

        const btn = document.getElementById("connect");
        const faixaInput = document.getElementById("faixa");
        const confirmBtn = document.getElementById("confirm");

        if (btn) btn.textContent = "Desconectar";
        if (faixaInput) faixaInput.disabled = true;
        if (confirmBtn) confirmBtn.disabled = true;

        if (!pingInterval) {
            pingInterval = setInterval(() => {
                const ips = getIpsFromLocalStorage();
                if (ips) pingAndUpdate(ips);
            }, 2000);
        }
    }
});

