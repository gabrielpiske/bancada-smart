let pingInterval = null;

function getValueIP() {
    let faixa = document.getElementById("faixa").value;
    let parse = faixa.split('.');
    let ipFormat;

    const resultado = document.getElementById("resultado");

    if (parse.length !== 3 && parse.length !== 4 || parse.some(p => isNaN(p) || p < 0 || p > 255)) {
        showErrorMessage("IP inválido!");
        return;
    }

    ipFormat = parse.length === 4 ? `${parse[0]}.${parse[1]}.${parse[2]}` : faixa;

    saveClpStatusToLocalStorage({
        ip: ipFormat
    });

    // Atualiza os textos visíveis
    document.getElementById("ipEstoque").textContent = `${ipFormat}.10`;
    document.getElementById("ipProcesso").textContent = `${ipFormat}.20`;
    document.getElementById("ipMontagem").textContent = `${ipFormat}.30`;
    document.getElementById("ipExpedicao").textContent = `${ipFormat}.40`;

    // Atualiza os valores dos inputs hidden
    document.getElementById("hostIpEstoque").value = `${ipFormat}.10`;
    document.getElementById("hostIpProcesso").value = `${ipFormat}.20`;
    document.getElementById("hostIpMontagem").value = `${ipFormat}.30`;
    document.getElementById("hostIpExpedicao").value = `${ipFormat}.40`;

    showSuccessMessage("Endereços IP atualizados!");
}

let conectado = false;

function connect() {
    const btn = document.getElementById("connect");
    const ips = {
        estoque: document.getElementById("hostIpEstoque").value,
        processo: document.getElementById("hostIpProcesso").value,
        montagem: document.getElementById("hostIpMontagem").value,
        expedicao: document.getElementById("hostIpExpedicao").value
    };

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
            document.getElementById("button-connect").disabled = true;
            document.getElementById("faixa").disabled = true;
            showSuccessMessage("Conexão estabelecida com sucesso!");

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
            document.getElementById("button-connect").disabled = false;
            document.getElementById("faixa").disabled = false;

            clearInterval(pingInterval);
            pingInterval = null;

            ["Estoque", "Processo", "Montagem", "Expedicao"].forEach(nome => {
                const ipElement = document.getElementById(`ip${nome}`);
                const tiElement = document.getElementById(`ti${nome}`);
                ipElement.style.color = "";
                ipElement.style.borderColor = "";
                tiElement.style.color = "";
            });

            saveClpStatusToLocalStorage({
                estoque: false,
                processo: false,
                montagem: false,
                expedicao: false
            });

            showInfoMessage("Conexão encerrada");
        })
        .catch(error => {
            console.error("Erro ao desconectar:", error);
            showErrorMessage(`Falha ao desconectar: ${error.message}`);
        });
    }
}

// Funções auxiliares
function capitalize(str) {
    return str.charAt(0).toUpperCase() + str.slice(1);
}

function showSuccessMessage(message) {
    const resultado = document.getElementById("resultado");
    resultado.textContent = message;
    resultado.style.color = "green";
    resultado.style.display = 'block';
    setTimeout(() => {
        resultado.style.display = 'none';
    }, 3000);
}

function showErrorMessage(message) {
    const resultado = document.getElementById("resultado");
    resultado.textContent = message;
    resultado.style.color = "red";
    resultado.style.display = 'block';
    setTimeout(() => {
        resultado.style.display = 'none';
    }, 3000);
}

function showInfoMessage(message) {
    const resultado = document.getElementById("resultado");
    resultado.textContent = message;
    resultado.style.color = "blue";
    resultado.style.display = 'block';
    setTimeout(() => {
        resultado.style.display = 'none';
    }, 3000);
}


function saveClpStatusToLocalStorage(status) {
    // Pega o que já está salvo, ou inicia com objeto vazio
    const stored = getClpStatusFromLocalStorage() || {};

    // Atualiza apenas os campos fornecidos
    const clpStatusAtualizado = {
        ...stored,
        ...status
    };

    localStorage.setItem("clpStatus", JSON.stringify(clpStatusAtualizado));
    console.log(clpStatusAtualizado);
}

function getClpStatusFromLocalStorage() {
    const stored = localStorage.getItem("clpStatus");
    if (!stored) return null;
    return JSON.parse(stored);
}

window.addEventListener("DOMContentLoaded", () => {
    fetch("/status-leituras")
        .then(res => res.json())
        .then(status => {
            const clpStatus = getClpStatusFromLocalStorage();

            // Se estiver conectado, atualiza botão e salva status
            if (status.ativo) {
                conectado = true;
                document.getElementById("connect").textContent = "Desconectar";
                document.getElementById("button-connect").disabled = true;
                document.getElementById("faixa").disabled = true;
                saveClpStatusToLocalStorage(status);

                // Inicia o ping periódico
                const ips = {
                    estoque: document.getElementById("hostIpEstoque").value,
                    processo: document.getElementById("hostIpProcesso").value,
                    montagem: document.getElementById("hostIpMontagem").value,
                    expedicao: document.getElementById("hostIpExpedicao").value
                };

                pingInterval = setInterval(() => {
                    pingAndUpdate(ips);
                }, 2000);
            }

            // Sempre restaura IPs com base no localStorage
            if (clpStatus?.ip) {
                ["Estoque", "Processo", "Montagem", "Expedicao"].forEach((modulo, idx) => {
                    const lastOctet = (idx + 1) * 10;
                    document.getElementById(`ip${modulo}`).textContent = `${clpStatus.ip}.${lastOctet}`;
                    document.getElementById(`hostIp${modulo}`).value = `${clpStatus.ip}.${lastOctet}`;
                });
            }

            // Aplica cores baseado no status ou localStorage
            const modulos = ["estoque", "processo", "montagem", "expedicao"];
            modulos.forEach(modulo => {
                const ipElement = document.getElementById(`ip${capitalize(modulo)}`);
                const tiElement = document.getElementById(`ti${capitalize(modulo)}`);
                const ok = status.ativo ? status[modulo] : clpStatus?.[modulo];

                if (ipElement && tiElement) {
                    ipElement.style.color = ok ? "#388E3C" : "#E74C3C";
                    ipElement.style.borderColor = ok ? "#388E3C" : "#E74C3C";
                    tiElement.style.color = ok ? "#388E3C" : "#E74C3C";
                }
            });
        });
});

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
        console.warn("Erro no ping periódico:", error);
    });
}

function updateStatusUI(status) {
    const modulosValidos = ["estoque", "processo", "montagem", "expedicao"];
    Object.entries(status).forEach(([nome, ok]) => {
        if (!modulosValidos.includes(nome)) return;
    
        const ipElement = document.getElementById(`ip${capitalize(nome)}`);
        const tiElement = document.getElementById(`ti${capitalize(nome)}`);
        const navIpElement = document.getElementById(`nav-ip${capitalize(nome)}`);
    
        if (ipElement) {
            ipElement.style.color = ok ? "#388E3C" : "#E74C3C";
            ipElement.style.borderColor = ok ? "#388E3C" : "#E74C3C";
        }
        if (tiElement) {
            tiElement.style.color = ok ? "#388E3C" : "#E74C3C";
        }
        if (navIpElement) {
            navIpElement.style.color = ok ? "#388E3C" : "#E74C3C";
        }
    });
}