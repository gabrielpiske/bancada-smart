var click = false;

function openEditMode() {
  document.querySelector(".color-button-box").classList.add("enabled");

  const buttons = document.querySelector(".button-box").querySelectorAll("div");
  buttons[0].classList.remove("enabled");
  buttons[1].classList.add("enabled");
  buttons[2].classList.add("enabled");
}

document
  .querySelector(".view-estoque")
  .querySelectorAll(".space")
  .forEach((space) => {
    space.addEventListener("mousedown", handleSpaceEvent);
  });

document.addEventListener("mousedown", handleMouseDown);
document.addEventListener("mouseup", handleMouseUp);

document
  .querySelector(".view-estoque")
  .querySelectorAll(".space")
  .forEach((space) => {
    space.addEventListener("mouseenter", () => {
      if (click) space.classList.add("selected");
    });
  });

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
    block.classList.remove("color-0");
    block.classList.remove("color-1");
    block.classList.remove("color-2");
    block.classList.remove("color-3");
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
}

function cleanEstoque() {
  document
    .querySelector(".view-estoque")
    .querySelectorAll(".space")
    .forEach((space) => {
      space.classList.remove("selected");
      const block = space.querySelector(".block");

      if (block.classList.contains("color-1"))
        block.classList.remove("color-1");
      if (block.classList.contains("color-2"))
        block.classList.remove("color-2");
      if (block.classList.contains("color-3"))
        block.classList.remove("color-3");

      space.querySelector("input[type='hidden']").value = 0;
    });
}

