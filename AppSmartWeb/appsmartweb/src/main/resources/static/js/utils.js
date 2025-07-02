export function showSuccessMessage(msg) {
    const el = document.getElementById("resultado");
    if (!el) return;
    el.textContent = msg;
    el.style.color = "green";
    el.style.display = 'block';
    setTimeout(() => el.style.display = 'none', 3000);
}
export function showErrorMessage(msg) {
    const el = document.getElementById("resultado");
    if (!el) return;
    el.textContent = msg;
    el.style.color = "red";
    el.style.display = 'block';
    setTimeout(() => el.style.display = 'none', 3000);
}
export function showInfoMessage(msg) {
    const el = document.getElementById("resultado");
    if (!el) return;
    el.textContent = msg;
    el.style.color = "blue";
    el.style.display = 'block';
    setTimeout(() => el.style.display = 'none', 3000);
}
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