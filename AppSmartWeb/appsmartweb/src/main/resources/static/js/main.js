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
        // Primeiro fazemos o ping para verificar a conexão
        fetch("/smart/ping", {
            method: "POST",
            headers: { "Content-Type": "application/json" },
            body: JSON.stringify(ips)
        })
            .then(res => res.json())
            .then(status => {
                // Atualiza as cores dos IPs baseado no status
                const modulosValidos = ["estoque", "processo", "montagem", "expedicao"];

                Object.entries(status).forEach(([nome, ok]) => {
                    if (!modulosValidos.includes(nome)) return;

                    const ipElement = document.getElementById(`ip${capitalize(nome)}`);
                    const tiElement = document.getElementById(`ti${capitalize(nome)}`);
                    if (ipElement) {
                        ipElement.style.color = ok ? "#388E3C" : "#E74C3C";
                        ipElement.style.borderColor = ok ? "#388E3C" : "#E74C3C";
                        tiElement.style.color = ok ? "#388E3C" : "#E74C3C";
                    }
                });

                // Se todos os CLPs responderam ao ping, inicia as leituras
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
                // Conexão estabelecida com sucesso
                btn.textContent = "Desconectar";
                conectado = true;
                showSuccessMessage("Conexão estabelecida com sucesso!");
            })
            .catch(error => {
                console.error("Erro ao conectar:", error);
                showErrorMessage(`Falha na conexão: ${error.message}`);
            });
    } else {
        // Desconectando
        fetch("/stop-leituras", {
            method: "POST"
        })
            .then(() => {
                btn.textContent = "Conectar";
                conectado = false;
                // Reseta as cores dos IPs
                ["Estoque", "Processo", "Montagem", "Expedicao"].forEach(nome => {
                    const ipElement = document.getElementById(`ip${nome}`);
                    const tiElement = document.getElementById(`ti${nome}`);
                    ipElement.style.color = "";
                    ipElement.style.borderColor = "";
                    tiElement.style.color = "";
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