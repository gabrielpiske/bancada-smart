const DEFAULT_CREDITS = {
    role: "Desenvolvedores - Técnico em Desenvolvimento de Sistemas:",
    dev1Name: "Gabriel Schweder Piske",
    dev1Url: "https://github.com/gabrielpiske",
    dev2Name: "Julio Henrique Busarello",
    dev2Url: "https://github.com/JulioBusarello",
    copyright: "© SENAI Timbó"
};

export function saveClpStatusToLocalStorage(status) {
    const atual = JSON.parse(localStorage.getItem("clpStatus") || "{}");
    localStorage.setItem("clpStatus", JSON.stringify({ ...atual, ...status }));
}

export function getClpStatusFromLocalStorage() {
    const stored = localStorage.getItem("clpStatus");
    if (!stored) return null;
    try {
        return JSON.parse(stored);
    } catch (e) {
        return null;
    }
}

export function capitalize(str) {
    return str.charAt(0).toUpperCase() + str.slice(1);
}

export function showMessage(type, text) {
    let container = document.getElementById("message-container");
    if (!container) {
        container = document.createElement("div");
        container.id = "message-container";
        container.className = "message-container";
        document.body.appendChild(container);
    }

    const msg = document.createElement("div");
    msg.className = `message ${type}`;

    const icon = document.createElement("span");
    icon.className = "material-symbols-rounded icon";

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

    const textNode = document.createTextNode(text);
    msg.appendChild(icon);
    msg.appendChild(textNode);
    container.appendChild(msg);

    setTimeout(() => {
        msg.remove();
    }, 5000);
}

export function initNavigationAndCredits() {
    // Destaca o link ativo na barra de navegação superior
    const currentPath = window.location.pathname;
    document.querySelectorAll(".nav-btns a[data-nav-path]").forEach(link => {
        const navPath = link.getAttribute("data-nav-path");
        if (navPath === "/" ? currentPath === "/" : currentPath.startsWith(navPath)) {
            link.classList.add("active");
        } else {
            link.classList.remove("active");
        }
    });

    // Gerencia créditos editáveis do rodapé SENAI
    const roleEl = document.getElementById("footer-role-label");
    const dev1El = document.getElementById("footer-dev1-link");
    const dev2El = document.getElementById("footer-dev2-link");
    const copyEl = document.getElementById("footer-copyright");
    const modal = document.getElementById("credits-modal");
    const btnEdit = document.getElementById("btn-edit-credits");

    if (!roleEl || !dev1El || !dev2El || !copyEl) return;

    function loadCredits() {
        try {
            const saved = JSON.parse(localStorage.getItem("bancadaCredits") || "null");
            return { ...DEFAULT_CREDITS, ...(saved || {}) };
        } catch (e) {
            return { ...DEFAULT_CREDITS };
        }
    }

    function applyCredits(credits) {
        roleEl.textContent = credits.role;
        dev1El.textContent = credits.dev1Name;
        dev1El.href = credits.dev1Url || "#";
        dev2El.textContent = credits.dev2Name;
        dev2El.href = credits.dev2Url || "#";
        copyEl.textContent = credits.copyright;
    }

    applyCredits(loadCredits());

    if (btnEdit && modal) {
        const inputRole = document.getElementById("input-credits-role");
        const inputDev1Name = document.getElementById("input-credits-dev1-name");
        const inputDev1Url = document.getElementById("input-credits-dev1-url");
        const inputDev2Name = document.getElementById("input-credits-dev2-name");
        const inputDev2Url = document.getElementById("input-credits-dev2-url");
        const inputCopy = document.getElementById("input-credits-copyright");

        btnEdit.addEventListener("click", () => {
            const current = loadCredits();
            if (inputRole) inputRole.value = current.role;
            if (inputDev1Name) inputDev1Name.value = current.dev1Name;
            if (inputDev1Url) inputDev1Url.value = current.dev1Url;
            if (inputDev2Name) inputDev2Name.value = current.dev2Name;
            if (inputDev2Url) inputDev2Url.value = current.dev2Url;
            if (inputCopy) inputCopy.value = current.copyright;
            modal.hidden = false;
        });

        document.getElementById("close-credits-modal")?.addEventListener("click", () => {
            modal.hidden = true;
        });

        document.getElementById("save-credits-btn")?.addEventListener("click", () => {
            const updated = {
                role: inputRole?.value?.trim() || DEFAULT_CREDITS.role,
                dev1Name: inputDev1Name?.value?.trim() || DEFAULT_CREDITS.dev1Name,
                dev1Url: inputDev1Url?.value?.trim() || DEFAULT_CREDITS.dev1Url,
                dev2Name: inputDev2Name?.value?.trim() || DEFAULT_CREDITS.dev2Name,
                dev2Url: inputDev2Url?.value?.trim() || DEFAULT_CREDITS.dev2Url,
                copyright: inputCopy?.value?.trim() || DEFAULT_CREDITS.copyright
            };
            localStorage.setItem("bancadaCredits", JSON.stringify(updated));
            applyCredits(updated);
            modal.hidden = true;
            showMessage("success", "Créditos do rodapé atualizados!");
        });

        document.getElementById("reset-credits-btn")?.addEventListener("click", () => {
            localStorage.removeItem("bancadaCredits");
            applyCredits(DEFAULT_CREDITS);
            modal.hidden = true;
            showMessage("info", "Créditos originais restaurados.");
        });

        modal.addEventListener("click", (e) => {
            if (e.target === modal) modal.hidden = true;
        });
    }
}
