export function saveClpStatusToLocalStorage(status) {
    const atual = JSON.parse(localStorage.getItem("clpStatus") || "{}");
    localStorage.setItem("clpStatus", JSON.stringify({ ...atual, ...status }));
}

export function getClpStatusFromLocalStorage() {
    const stored = localStorage.getItem("clpStatus");
    if (!stored) return null;
    return JSON.parse(stored);
}

export function capitalize(str) {
    return str.charAt(0).toUpperCase() + str.slice(1);
}

export function showMessage(type, text) {
    const container = document.getElementById("message-container");
    const msg = document.createElement("div");
    msg.className = `message ${type}`;

    // Criar o elemento do ícone
    const icon = document.createElement("span");
    icon.className = "material-symbols-rounded icon";

    // Define o ícone com base no tipo da mensagem
    switch (type) {
        case "success":
            icon.textContent = "check_circle";
            break;
        case "error":
            icon.textContent = "error";
            break;
        case "info":
            icon.textContent = "info";
            break;
        default:
            icon.textContent = "notifications";
    }

    // Texto da mensagem
    const textNode = document.createTextNode(text);

    // Adiciona o ícone e o texto na mensagem
    msg.appendChild(icon);
    msg.appendChild(textNode);

    container.appendChild(msg);

    // Remove depois de 5 segundos
    setTimeout(() => {
        msg.remove();
    }, 5000);
}