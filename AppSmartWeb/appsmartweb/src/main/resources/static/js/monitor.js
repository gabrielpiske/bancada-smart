import { initNavigationAndCredits } from './utils.js';

let pollingActive = true;
let activeFilter = "all";
let searchQuery = "";

function applySearchAndFilters() {
    const q = searchQuery.toLowerCase().trim();

    document.querySelectorAll(".section[data-station-group]").forEach(section => {
        const group = section.getAttribute("data-station-group");
        const groupMatches = activeFilter === "all"
            || activeFilter === "true"
            || activeFilter === "false"
            || activeFilter === group;

        if (!groupMatches) {
            section.hidden = true;
            return;
        }
        section.hidden = false;

        section.querySelectorAll(".var").forEach(varEl => {
            const labelTxt = varEl.querySelector(".label")?.textContent?.toLowerCase() || "";
            const valEl = varEl.querySelector(".value");
            const valTxt = valEl?.textContent?.toLowerCase() || "";

            const matchesQuery = !q || labelTxt.includes(q) || valTxt.includes(q);
            let matchesBool = true;
            if (activeFilter === "true") {
                matchesBool = valEl?.classList.contains("boolean-true") || false;
            } else if (activeFilter === "false") {
                matchesBool = valEl?.classList.contains("boolean-false") || false;
            }

            varEl.hidden = !(matchesQuery && matchesBool);
        });
    });
}

export function atualizarEstado() {
    if (!pollingActive) return;

    fetch("/api/estado")
        .then((res) => res.json())
        .then((data) => {
            let totalKeys = 0;
            let trueSignals = 0;

            for (const key in data) {
                totalKeys++;
                const value = data[key];
                if (value === true || value === "true") {
                    trueSignals++;
                }

                const el = document.getElementById(key);
                if (el) {
                    el.classList.remove("boolean-true", "boolean-false");
                    el.textContent = Array.isArray(value) ? JSON.stringify(value) : value;

                    if (value === true || value === "true") {
                        el.classList.add("boolean-true");
                    } else if (value === false || value === "false") {
                        el.classList.add("boolean-false");
                    }
                }
            }

            const totalEl = document.getElementById("monitor-total-vars");
            const trueEl = document.getElementById("monitor-true-vars");
            const timeEl = document.getElementById("monitor-last-update");

            if (totalEl) totalEl.textContent = String(totalKeys);
            if (trueEl) trueEl.textContent = String(trueSignals);
            if (timeEl) {
                const now = new Date();
                timeEl.textContent = now.toLocaleTimeString("pt-BR");
            }

            applySearchAndFilters();
        })
        .catch((err) => console.error("Erro ao buscar estado:", err));
}

export function toggleSection(header) {
    const section = header.closest(".section");
    if (section) section.classList.toggle("closed");
}

document.addEventListener("DOMContentLoaded", () => {
    initNavigationAndCredits();

    const searchInput = document.getElementById("monitor-search");
    searchInput?.addEventListener("input", (e) => {
        searchQuery = e.target.value || "";
        applySearchAndFilters();
    });

    document.querySelectorAll(".filter-chip").forEach(chip => {
        chip.addEventListener("click", () => {
            document.querySelectorAll(".filter-chip").forEach(c => c.classList.remove("active"));
            chip.classList.add("active");
            activeFilter = chip.getAttribute("data-filter") || "all";
            applySearchAndFilters();
        });
    });

    const pollBtn = document.getElementById("btn-toggle-polling");
    pollBtn?.addEventListener("click", () => {
        pollingActive = !pollingActive;
        pollBtn.classList.toggle("paused", !pollingActive);
        pollBtn.innerHTML = pollingActive
            ? '<i class="fa-solid fa-bolt"></i> Leitura Ativa (1s)'
            : '<i class="fa-solid fa-pause"></i> Leitura Pausada';
        if (pollingActive) atualizarEstado();
    });

    atualizarEstado();
    setInterval(atualizarEstado, 1000);
});

window.toggleSection = toggleSection;
