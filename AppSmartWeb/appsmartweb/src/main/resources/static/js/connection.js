import { showMessage, saveClpStatusToLocalStorage, getClpStatusFromLocalStorage, capitalize, initNavigationAndCredits } from './utils.js';

export let conectado = false;
let pingInterval = null;

function setConnectButtonVisual(isConnected) {
    const btn = document.getElementById("connect");
    const badge = document.getElementById("main-connection-badge");
    const badgeTxt = document.getElementById("main-connection-text");
    const headerDot = document.getElementById("header-ping-dot");
    const headerLabel = document.getElementById("header-ping-label");

    if (btn) {
        btn.classList.toggle("connected", isConnected);
        btn.innerHTML = isConnected
            ? '<i class="fa-solid fa-power-off"></i> <span>Desconectar CLPs</span>'
            : '<i class="fa-solid fa-plug"></i> <span>Conectar aos CLPs</span>';
    }
    if (badge && badgeTxt) {
        badge.classList.toggle("online", isConnected);
        badge.classList.toggle("offline", !isConnected);
        badgeTxt.textContent = isConnected ? "CLPs Conectados" : "Desconectado";
    }
    if (headerDot) {
        headerDot.classList.toggle("online", isConnected);
    }
    if (headerLabel) {
        headerLabel.textContent = isConnected ? "CLPs Online" : "Rede CLP";
    }
}

export function connect() {
    const ips = {
        estoque: document.getElementById("hostIpEstoque").value,
        processo: document.getElementById("hostIpProcesso").value,
        montagem: document.getElementById("hostIpMontagem").value,
        expedicao: document.getElementById("hostIpExpedicao").value
    };

    let faixa = document.getElementById("hostIpEstoque").value;
    let parse = faixa.split('.');
    let ipFormat = parse.length === 4 ? `${parse[0]}.${parse[1]}.${parse[2]}` : faixa;

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
                conectado = true;
                setConnectButtonVisual(true);
                document.getElementById("confirm").disabled = true;
                document.getElementById("faixa").disabled = true;

                showMessage("success", "Conexão S7 estabelecida com sucesso!");
                saveClpStatusToLocalStorage({ ativo: true, ip: ipFormat });

                pingInterval = setInterval(() => {
                    pingAndUpdate(ips);
                }, 2000);
            })
            .catch(error => {
                console.error("Erro ao conectar:", error);
                showMessage("error", `Falha na conexão: ${error.message}`);
            });
    } else {
        fetch("/stop-leituras", { method: "POST" })
            .then(() => {
                conectado = false;
                setConnectButtonVisual(false);
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

                const currentStatus = getClpStatusFromLocalStorage() || {};

                saveClpStatusToLocalStorage({
                    ...currentStatus,
                    estoque: false,
                    processo: false,
                    montagem: false,
                    expedicao: false,
                    ativo: false
                });

                showMessage("info", "Conexão com os CLPs encerrada.");
                updateStatusUI(getClpStatusFromLocalStorage());
            })
            .catch(error => {
                console.error("Erro ao desconectar:", error);
                showMessage("error", `Falha ao desconectar: ${error.message}`);
            });
    }
}

export function updateStatusUI(status) {
    if (!status) return;
    const modulos = ["estoque", "processo", "montagem", "expedicao"];
    modulos.forEach(modulo => {
        const ip = document.getElementById(`ip${capitalize(modulo)}`);
        const ti = document.getElementById(`ti${capitalize(modulo)}`);
        const nav = document.getElementById(`nav-ip${capitalize(modulo)}`);
        const ok = Boolean(status[modulo]);

        if (ip) {
            ip.style.color = ok ? "#34D399" : "#F87171";
            ip.style.borderColor = ok ? "rgba(16, 185, 129, 0.5)" : "rgba(239, 68, 68, 0.45)";
        }
        if (ti) {
            ti.style.color = ok ? "#34D399" : "#F87171";
        }
        if (nav) {
            nav.style.color = ok ? "#34D399" : "#F87171";
        }
    });

    const allOnline = modulos.every(m => Boolean(status[m]));
    setConnectButtonVisual(Boolean(status.ativo) && allOnline);
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
    initNavigationAndCredits();

    const clpStatus = getClpStatusFromLocalStorage();

    if (clpStatus) {
        updateStatusUI(clpStatus);
    }

    if (clpStatus?.ip) {
        const faixaInput = document.getElementById("faixa");
        if (faixaInput) faixaInput.value = `${clpStatus.ip}.0`;

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
        setConnectButtonVisual(true);

        const faixaInput = document.getElementById("faixa");
        const confirmBtn = document.getElementById("confirm");
        if (faixaInput) faixaInput.disabled = true;
        if (confirmBtn) confirmBtn.disabled = true;

        if (!pingInterval) {
            pingInterval = setInterval(() => {
                const ips = getIpsFromLocalStorage();
                if (ips) pingAndUpdate(ips);
            }, 2000);
        }
    }

    window.addEventListener("storage", (event) => {
        if (event.key === "clpStatus") {
            const novoStatus = JSON.parse(event.newValue || "{}");
            updateStatusUI(novoStatus);

            const faixaInput = document.getElementById("faixa");
            const confirmBtn = document.getElementById("confirm");

            if (novoStatus.ativo === false) {
                conectado = false;
                setConnectButtonVisual(false);

                if (pingInterval) {
                    clearInterval(pingInterval);
                    pingInterval = null;
                }

                if (faixaInput) faixaInput.disabled = false;
                if (confirmBtn) confirmBtn.disabled = false;
            } else {
                conectado = true;
                setConnectButtonVisual(true);
                if (faixaInput) faixaInput.disabled = true;
                if (confirmBtn) confirmBtn.disabled = true;
            }
        }
    });
});
