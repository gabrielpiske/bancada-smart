var click = false;

function updateStockKPIs() {
  const inputs = document.querySelectorAll('#stock-grid input[name="listBlocks"]');
  let black = 0;
  let red = 0;
  let blue = 0;
  let empty = 0;

  inputs.forEach((inp) => {
    const v = Number(inp.value) || 0;
    if (v === 1) black++;
    else if (v === 2) red++;
    else if (v === 3) blue++;
    else empty++;
  });

  const totalOccupied = black + red + blue;
  const totalSlots = inputs.length || 28;

  const setTxt = (id, txt) => {
    const el = document.getElementById(id);
    if (el) el.textContent = txt;
  };

  setTxt("kpi-total-stock", `${totalOccupied} / ${totalSlots}`);
  setTxt("kpi-black-count", String(black));
  setTxt("kpi-red-count", String(red));
  setTxt("kpi-blue-count", String(blue));
  setTxt("kpi-empty-count", String(empty));

  const expOccupied = document.querySelectorAll("#expedition-grid .space.occupied").length;
  setTxt("kpi-exp-count", `${expOccupied} / 12`);

  const setWidth = (id, count) => {
    const el = document.getElementById(id);
    if (el) el.style.width = `${((count / totalSlots) * 100).toFixed(1)}%`;
  };
  setWidth("bar-seg-black", black);
  setWidth("bar-seg-red", red);
  setWidth("bar-seg-blue", blue);
}

const stockGrid = document.querySelector("#stock-grid");
if (stockGrid) {
  const isEditMode = document.querySelector(".color-button-box") !== null;

  if (isEditMode) {
    stockGrid.querySelectorAll(".space").forEach((space) => {
      space.addEventListener("mousedown", handleSpaceEvent);
      space.addEventListener("mouseenter", () => {
        if (click) space.classList.add("selected");
      });
    });

    document.addEventListener("mousedown", handleMouseDown);
    document.addEventListener("mouseup", handleMouseUp);
  }
}

function handleMouseDown() {
  click = true;
}

function handleMouseUp() {
  click = false;
}

function handleSpaceEvent(evt) {
  handleSpace(evt, evt.currentTarget);
}

function handleSpace(evt, space) {
  if (evt.button == 0) {
    space.classList.toggle("selected");
  }
}

function changeBlockColor(color) {
  const selecteds = document.querySelectorAll(".selected");
  selecteds.forEach((space) => {
    const block = space.querySelector(".block");
    block.classList.remove("color-0", "color-1", "color-2", "color-3");
    block.classList.add(color);
    space.classList.remove("selected");
    const input = space.querySelector("input[type='hidden']");
    switch (color) {
      case "color-0":
        input.value = 0;
        break;
      case "color-1":
        input.value = 1;
        break;
      case "color-2":
        input.value = 2;
        break;
      case "color-3":
        input.value = 3;
        break;
    }
  });
  updateStockKPIs();
}

function cleanEstoque() {
  document
    .querySelector("#stock-grid")
    ?.querySelectorAll(".space")
    .forEach((space) => {
      space.classList.remove("selected");
      const block = space.querySelector(".block");
      block.classList.remove("color-1", "color-2", "color-3");
      block.classList.add("color-0");
      space.querySelector("input[type='hidden']").value = 0;
    });
  updateStockKPIs();
}

document.addEventListener("DOMContentLoaded", updateStockKPIs);
updateStockKPIs();
