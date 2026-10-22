import { getClpStatusFromLocalStorage, showMessage, initNavigationAndCredits } from './utils.js';

export { changePedidoView, changeTampaView, confirmTampa, confirm, spin, spinModal, openModal, closeModal, enviarTampa };

const MAX_BLOCOS = 3;
let isSubmittingOrder = false;

const $document = $(document);
const $mainContainer = $('main.store-main');

function resetPedidoView(id, $lamina1, $lamina2, $lamina3, $sendBtn) {
  $(`#bloco-${id}`).attr("src", "assets/blocos/rBlocoCor0.png");
  [$lamina1, $lamina2, $lamina3].forEach(el => el.val("").prop("disabled", true));
  ['l1', 'l2', 'l3'].forEach((p, idx) => {
    $(`#${p}-pattern-${id}`).val("").prop("disabled", true);
    const face = idx + 1;
    $(`#lamina${id}-${face}`).attr("src", "#");
    $(`#padrao${id}-${face}`).attr("src", "#").prop("hidden", false);
  });
  $sendBtn.prop("disabled", true);
}

function changePedidoView(id) {
  const blockColor = $(`#block-color-${id}`).val();
  const $lamina1 = $(`#l1-color-${id}`);
  const $lamina2 = $(`#l2-color-${id}`);
  const $lamina3 = $(`#l3-color-${id}`);
  const $sendBtn = $(`#send-${id}`);
  const $pedidoView = $(`#pedido-view${id}`);
  const isSpun = Boolean($pedidoView.data("isSpun"));

  if (blockColor) {
    $(`#bloco-${id}`).attr("src", `assets/blocos/rBlocoCor${blockColor}.png`);
    [$lamina1, $lamina2, $lamina3].forEach(el => el.prop("disabled", false));

    const [l1Color, l2Color, l3Color] = [$lamina1, $lamina2, $lamina3].map(el => el.val());

    // Se a cor da lâmina for limpa ("Nenhum"), limpa e desabilita também o padrão correspondente
    ['l1', 'l2', 'l3'].forEach((p, i) => {
      const hasColor = Boolean([l1Color, l2Color, l3Color][i]);
      const $patternSelect = $(`#${p}-pattern-${id}`);
      if (!hasColor) {
        $patternSelect.val("");
      }
      $patternSelect.prop("disabled", !hasColor);
    });

    const [l1Pattern, l2Pattern, l3Pattern] = ['l1', 'l2', 'l3'].map(p => $(`#${p}-pattern-${id}`).val());

    $sendBtn.prop("disabled", false);
    updateLaminaImages(id, isSpun, l1Color, l2Color, l3Color, l1Pattern, l2Pattern, l3Pattern);
  } else {
    resetPedidoView(id, $lamina1, $lamina2, $lamina3, $sendBtn);
  }

  verificarEstadoBlocos();
}

function updateLaminaImages(id, isSpun, l1Color, l2Color, l3Color, l1Pattern, l2Pattern, l3Pattern) {
  const $lamina1 = $(`#lamina${id}-1`);
  const $lamina3 = $(`#lamina${id}-3`);
  const $padrao1 = $(`#padrao${id}-1`);
  const $padrao3 = $(`#padrao${id}-3`);

  if (isSpun) {
    $lamina3.attr("src", l1Color ? `assets/laminas/lamina3-${l1Color}.png` : "#");
    $lamina1.attr("src", l3Color ? `assets/laminas/lamina1-${l3Color}.png` : "#");

    $padrao3.attr("src", l3Pattern ? `assets/padroes/padrao${l3Pattern}-1.png` : "#").prop("hidden", false);
    $padrao1.attr("src", l1Pattern ? `assets/padroes/padrao${l1Pattern}-1.png` : "#").prop("hidden", true);
  } else {
    $lamina1.attr("src", l1Color ? `assets/laminas/lamina1-${l1Color}.png` : "#");
    $lamina3.attr("src", l3Color ? `assets/laminas/lamina3-${l3Color}.png` : "#");

    $padrao1.attr("src", l1Pattern ? `assets/padroes/padrao${l1Pattern}-1.png` : "#").prop("hidden", false);
    $padrao3.attr("src", l3Pattern ? `assets/padroes/padrao${l3Pattern}-1.png` : "#").prop("hidden", true);
  }

  $(`#lamina${id}-2`).attr("src", l2Color ? `assets/laminas/lamina2-${l2Color}.png` : "#");
  $(`#padrao${id}-2`).attr("src", l2Pattern ? `assets/padroes/padrao${l2Pattern}-2.png` : "#").prop("hidden", false);
}

function confirm(id) {
  const $section = $(`#section-bloco-${id}`);
  const $button = $(`#send-${id}`);
  const $badge = $(`#status-badge-${id}`);
  const isDisabled = $section.hasClass("disabled");

  $section.toggleClass("disabled", !isDisabled);
  $button.text(isDisabled ? "Confirmar Bloco" : "Editar Bloco");
  if ($badge.length) {
    $badge.text(isDisabled ? "Em Edição" : "Confirmado ✓");
  }

  verificarEstadoBlocos();
}

function spin(id) {
  const $view = $(`#pedido-view${id}`);
  const isSpun = !$view.data("isSpun");
  $view.toggleClass("spin", isSpun).data("isSpun", isSpun);

  const l1Color = $(`#l1-color-${id}`).val();
  const l2Color = $(`#l2-color-${id}`).val();
  const l3Color = $(`#l3-color-${id}`).val();

  const l1Pattern = $(`#l1-pattern-${id}`).val();
  const l2Pattern = $(`#l2-pattern-${id}`).val();
  const l3Pattern = $(`#l3-pattern-${id}`).val();

  updateLaminaImages(id, isSpun, l1Color, l2Color, l3Color, l1Pattern, l2Pattern, l3Pattern);
}

function spinModal() {
  const $modalView = $('.modal-pedido-view');
  const nextSpun = !$modalView.hasClass('spin');
  $modalView.toggleClass('spin', nextSpun);
  updateModalImages();
}

function verificarEstadoBlocos() {
  const blocosDesativados = $('section[id^="section-bloco-"].disabled').length;
  const tampaDesativada = $('section.section-tampa.disabled').length;
  $("#enviar-pedido").prop("disabled", blocosDesativados === 0 || tampaDesativada === 0);
}

function addDeleteButton($section) {
  $section.prepend('<span class="delete-btn material-symbols-rounded" title="Remover este andar">close</span>');
}

function checkAndInsertHidden() {
  const currentBlocoCount = $('section[id^="section-bloco-"]').length;
  const hiddenExists = $('.hidden').length > 0;

  if (currentBlocoCount === 1 && !hiddenExists) {
    $('.plus').after('<section class="hidden"></section>');
  }
}

function getNextAvailableBlockId() {
  const usedIds = new Set();
  $('section[id^="section-bloco-"]').each(function () {
    const num = Number(this.id.split('-')[2]);
    if (num) usedIds.add(num);
  });
  for (let i = 1; i <= MAX_BLOCOS; i++) {
    if (!usedIds.has(i)) return i;
  }
  return usedIds.size + 1;
}

function refreshStepBadges() {
  $('section[id^="section-bloco-"]').each(function (idx) {
    $(this).find(".step-badge").text(`Andar ${idx + 1}`);
  });
}

function cloneAndModifyBlock(id) {
  const $original = $("#section-bloco-1").clone(false).removeClass("disabled");
  $original.attr("id", `section-bloco-${id}`);

  $original.find(".step-badge").text(`Andar ${id}`);
  $original.find(".step-status").attr("id", `status-badge-${id}`).text("Em Edição");

  $original.find(".pedido-view")
    .attr("id", `pedido-view${id}`)
    .removeClass("spin")
    .data("isSpun", false);

  $original.find("*").each(function () {
    const $el = $(this);
    const attributes = ["id", "for", "name", "onclick", "onchange"];

    attributes.forEach(attr => {
      const val = $el.attr(attr);
      if (val) {
        $el.attr(attr, val
          .replace(/pedido-view\d*/g, `pedido-view${id}`)
          .replace(/status-badge-\d*/g, `status-badge-${id}`)
          .replace(/(lamina|padrao)(\d+)-(\d+)/g, (_, tipo, x, y) => `${tipo}${id}-${y}`)
          .replace(/([^\d])-1\b/g, `$1-${id}`)
          .replace(/\b(spin|confirm|changePedidoView)\(\d+\)/g, `$1(${id})`)
        );
      }
    });
  });

  $original.find("select").val("").prop("disabled", true);
  $original.find(`#block-color-${id}`).prop("disabled", false);

  $original.find('form button[type="button"]')
    .attr("id", `send-${id}`)
    .prop("disabled", true)
    .text("Confirmar Bloco");

  $original.find("img").each(function () {
    const $img = $(this);
    if ($img.attr("id")?.startsWith("bloco-")) {
      $img.attr("src", "assets/blocos/rBlocoCor0.png");
    } else {
      $img.attr("src", "#").prop("hidden", false);
    }
  });

  if (id > 1) {
    addDeleteButton($original);
  }

  return $original;
}

function moveTampaToEnd() {
  const $tampaSection = $('#section-tampa');
  if ($tampaSection.length) {
    $tampaSection.appendTo($mainContainer);
  }
}

function changeTampaView() {
  const $tampa = $("#tampa-view");
  const value = $("#tampa-color").val();
  const $img = $("#tampa");
  const $confirm = $("#send-tampa");

  if ($tampa && value) {
    $img.attr("src", `assets/tampas/rTampa${value}.png`);
  }

  if (value >= 1 && value <= 3) {
    $confirm.prop("disabled", false);
  }
}

function confirmTampa() {
  const $section = $("#section-tampa");
  const $button = $("#send-tampa");
  const $badge = $("#status-badge-tampa");
  const isDisabled = $section.hasClass("disabled");

  $section.toggleClass("disabled", !isDisabled);
  $button.text(isDisabled ? "Confirmar Tampa" : "Editar Tampa");
  if ($badge.length) {
    $badge.text(isDisabled ? "Pendente" : "Confirmada ✓");
  }

  verificarEstadoBlocos();
}

function handleAddBlock() {
  const currentCount = $('section[id^="section-bloco-"]').length;
  if (currentCount >= MAX_BLOCOS) {
    showMessage("info", `Número máximo de andares atingido (${MAX_BLOCOS}).`);
    return;
  }

  const nextId = getNextAvailableBlockId();
  const $newBlock = cloneAndModifyBlock(nextId);
  const $plusSection = $(this).closest('.plus');
  const $hiddenSection = $plusSection.siblings('.hidden');

  $plusSection.remove();
  $hiddenSection.remove();

  $mainContainer.append($newBlock);
  checkAndInsertHidden();
  refreshStepBadges();

  if (currentCount + 1 < MAX_BLOCOS) {
    $newBlock.after('<section class="plus"><span class="material-symbols-rounded" title="Adicionar novo andar">add</span><p class="plus-caption">Adicionar Andar</p></section>');
  }

  moveTampaToEnd();
}

function handleDeleteBlock() {
  const $sec = $(this).closest('section[id^="section-bloco-"]');
  $sec.remove();

  if ($('.plus').length === 0) {
    $('section[id^="section-bloco-"]').last().after(
      '<section class="plus"><span class="material-symbols-rounded" title="Adicionar novo andar">add</span><p class="plus-caption">Adicionar Andar</p></section>'
    );
  }

  checkAndInsertHidden();
  refreshStepBadges();
  verificarEstadoBlocos();
  moveTampaToEnd();
}

$document.ready(function () {
  initNavigationAndCredits();

  $document.on('click', '.plus', handleAddBlock);
  $document.on('click', '.delete-btn', handleDeleteBlock);

  moveTampaToEnd();

  const urlParams = new URLSearchParams(window.location.search);
  if (urlParams.has("error")) {
    showMessage("error", urlParams.get("error") || "Erro ao processar o pedido.");
  } else if (urlParams.has("success")) {
    showMessage("success", "Pedido enviado com sucesso!");
  }
});

function verBlocosMontados() {
  const modalBloco1 = document.getElementById("modal-bloco1");
  const container = document.querySelector(".modal-pedido-view .modal-bloco-container");
  const containerWidth = container?.clientWidth || 350;
  const alturaBloco = modalBloco1?.offsetHeight || (containerWidth * (145 / 320));

  const fatorMultiplicador = 0.445;
  const dif = 0.255 * alturaBloco;

  const tampa = document.getElementById("tampa-color").value || "1";
  $(`#modal-tampa`).attr("src", `assets/tampas/rTampa${tampa}.png`).css("top", "0px");

  const confirmados = $('section[id^="section-bloco-"].disabled');
  confirmados.each(function (index) {
    const modalIndex = index + 1;
    const altura = (((index + 1) * fatorMultiplicador * alturaBloco) - dif) + "px";

    $(`#modal-bloco${modalIndex}`).css("top", altura);
    $(`[id^="modal-lamina${modalIndex}-"]`).css("top", altura);
    $(`[id^="modal-padrao${modalIndex}-"]`).css("top", altura);
  });
}

function openModal() {
  $('.modal-pedido-view').removeClass('spin');
  updateModalImages();
  document.getElementById('pedidoModal').style.display = 'block';

  const modalBloco1 = document.getElementById("modal-bloco1");
  if (modalBloco1) {
    modalBloco1.onload = () => verBlocosMontados();
  }
  verBlocosMontados();
}

function closeModal() {
  document.getElementById('pedidoModal').style.display = 'none';
}

document.getElementById("modal-confirm").addEventListener("click", async () => {
  await submitOrder();
});

export async function submitOrder() {
  if (isSubmittingOrder) {
    return;
  }
  const btnConfirm = document.getElementById("modal-confirm");
  let clpStatus = getClpStatusFromLocalStorage();

  if (clpStatus?.ativo !== true) {
    showMessage("error", "CLP não está conectado!");
    return;
  }

  isSubmittingOrder = true;
  if (btnConfirm) {
    btnConfirm.disabled = true;
  }

  try {
    closeModal();

    const sucessoTampa = await enviarTampa();
    if (!sucessoTampa) {
      showMessage("error", "Falha ao mover a tampa");
      return;
    }

    saveDataToLocalStorage();
    await enviarPedido();
  } finally {
    isSubmittingOrder = false;
    if (btnConfirm) {
      btnConfirm.disabled = false;
    }
  }
}

function updateModalImages() {
  const isSpun = $('.modal-pedido-view').hasClass('spin');

  // Limpa camadas anteriores para evitar resquícios de blocos/lâminas/padrões removidos
  for (let i = 1; i <= MAX_BLOCOS; i++) {
    $(`#modal-bloco${i}`).attr("src", "#").hide();
    for (let f = 1; f <= 3; f++) {
      $(`#modal-lamina${i}-${f}`).attr("src", "#");
      $(`#modal-padrao${i}-${f}`).attr("src", "#").prop("hidden", false);
    }
  }

  $('section[id^="section-bloco-"].disabled').each(function (index) {
    const sectionId = this.id.split('-')[2];
    const modalIndex = index + 1;
    const $blocoModal = $(`#modal-bloco${modalIndex}`);
    const blockColor = $(`#block-color-${sectionId}`).val();

    const l1Color = $(`#l1-color-${sectionId}`).val();
    const l2Color = $(`#l2-color-${sectionId}`).val();
    const l3Color = $(`#l3-color-${sectionId}`).val();

    const l1Pattern = $(`#l1-pattern-${sectionId}`).val();
    const l2Pattern = $(`#l2-pattern-${sectionId}`).val();
    const l3Pattern = $(`#l3-pattern-${sectionId}`).val();

    $blocoModal.attr("src", `assets/blocos/rBlocoCor${blockColor}.png`).show();

    const $lamina1 = $(`#modal-lamina${modalIndex}-1`);
    const $lamina2 = $(`#modal-lamina${modalIndex}-2`);
    const $lamina3 = $(`#modal-lamina${modalIndex}-3`);
    const $padrao1 = $(`#modal-padrao${modalIndex}-1`);
    const $padrao2 = $(`#modal-padrao${modalIndex}-2`);
    const $padrao3 = $(`#modal-padrao${modalIndex}-3`);

    if (isSpun) {
      $lamina1.attr("src", l3Color ? `assets/laminas/lamina1-${l3Color}.png` : "#");
      $lamina3.attr("src", l1Color ? `assets/laminas/lamina3-${l1Color}.png` : "#");

      $padrao1.attr("src", l1Pattern ? `assets/padroes/padrao${l1Pattern}-1.png` : "#").prop("hidden", true);
      $padrao3.attr("src", l3Pattern ? `assets/padroes/padrao${l3Pattern}-1.png` : "#").prop("hidden", false);
    } else {
      $lamina1.attr("src", l1Color ? `assets/laminas/lamina1-${l1Color}.png` : "#");
      $lamina3.attr("src", l3Color ? `assets/laminas/lamina3-${l3Color}.png` : "#");

      $padrao1.attr("src", l1Pattern ? `assets/padroes/padrao${l1Pattern}-1.png` : "#").prop("hidden", false);
      $padrao3.attr("src", l3Pattern ? `assets/padroes/padrao${l3Pattern}-1.png` : "#").prop("hidden", true);
    }

    $lamina2.attr("src", l2Color ? `assets/laminas/lamina2-${l2Color}.png` : "#");
    $padrao2.attr("src", l2Pattern ? `assets/padroes/padrao${l2Pattern}-2.png` : "#").prop("hidden", false);
  });
}

async function enviarPedido() {
  const formData = new FormData();
  let hasData = false;

  $('section[id^="section-bloco-"].disabled').each(function (index) {
    const sectionId = this.id.split('-')[2];
    const floorNum = index + 1;
    hasData = true;

    const blockColor = $(`#block-color-${sectionId}`).val();
    const l1Color = $(`#l1-color-${sectionId}`).val();
    const l2Color = $(`#l2-color-${sectionId}`).val();
    const l3Color = $(`#l3-color-${sectionId}`).val();
    const l1Pattern = $(`#l1-pattern-${sectionId}`).val();
    const l2Pattern = $(`#l2-pattern-${sectionId}`).val();
    const l3Pattern = $(`#l3-pattern-${sectionId}`).val();

    formData.append(`block-color-${floorNum}`, blockColor);
    formData.append(`l1-color-${floorNum}`, l1Color);
    formData.append(`l2-color-${floorNum}`, l2Color);
    formData.append(`l3-color-${floorNum}`, l3Color);
    if (l1Pattern) formData.append(`l1-pattern-${floorNum}`, l1Pattern);
    if (l2Pattern) formData.append(`l2-pattern-${floorNum}`, l2Pattern);
    if (l3Pattern) formData.append(`l3-pattern-${floorNum}`, l3Pattern);
  });

  if (!hasData) {
    showMessage("error", "Nenhum bloco confirmado para envio!");
    return;
  }

  formData.append('total-blocks', $('section[id^="section-bloco-"].disabled').length);

  try {
    const response = await fetch("/pedidoTeste", {
      method: "POST",
      body: formData,
    });

    if (response.redirected) {
      const targetUrl = new URL(response.url, window.location.origin);
      if (targetUrl.searchParams.has("error")) {
        showMessage("error", targetUrl.searchParams.get("error") || "Erro ao enviar pedido.");
      } else {
        showMessage("success", "Pedido enviado! Redirecionando para Produção Ao Vivo...");
        window.location.href = response.url;
      }
    } else if (!response.ok) {
      showMessage("error", `Erro ao enviar pedido (HTTP ${response.status})`);
    } else {
      showMessage("success", "Pedido enviado com sucesso!");
      window.location.href = "/view?success";
    }
  } catch (error) {
    console.error("Erro ao enviar pedido:", error);
    showMessage("error", "Falha de comunicação ao enviar o pedido.");
  }
}

async function enviarTampa() {
  let value = document.getElementById("tampa-color").value;

  try {
    const response = await fetch('/api/moverTampa', {
      method: 'POST',
      headers: {
        'Content-Type': 'application/x-www-form-urlencoded',
      },
      body: `pos=${value}`
    });

    if (!response.ok) throw new Error(`Erro na resposta: ${response.status}`);

    const data = await response.text();
    return data.includes("Ok") || data.includes("Posição selecionada");
  } catch (error) {
    console.error("Erro na requisição: ", error);
    return false;
  }
}

window.onclick = function (event) {
  const modal = document.getElementById('pedidoModal');
  if (event.target == modal) {
    closeModal();
  }
};

function saveDataToLocalStorage() {
  let arrayModais = Array.from(document.querySelectorAll('section[id^="section-bloco-"].disabled'));
  let arrayIdsNum = [];

  arrayModais.forEach((element) => {
    let id = element.getAttribute("id");
    arrayIdsNum.push(id.split('-')[2]);
  });

  const corBloco = function (index) {
    const id = arrayIdsNum[index] ?? "NULL";
    const el = document.getElementById(`block-color-${id}`);
    return el?.value ?? "";
  };

  const corLamina = function (lamina, index) {
    const id = arrayIdsNum[index] ?? "NULL";
    const el = document.getElementById(`l${lamina}-color-${id}`);
    return el?.value ?? "";
  };

  const padraoLamina = function (padrao, index) {
    const id = arrayIdsNum[index] ?? "NULL";
    const el = document.getElementById(`l${padrao}-pattern-${id}`);
    return el?.value ?? "";
  };

  let parametros = {
    tampa: document.getElementById("tampa-color")?.value ?? "1",
    cor1: corBloco(0),
    cor2: corBloco(1),
    cor3: corBloco(2),

    laminas: {
      "1": [corLamina(1, 0), corLamina(2, 0), corLamina(3, 0)],
      "2": [corLamina(1, 1), corLamina(2, 1), corLamina(3, 1)],
      "3": [corLamina(1, 2), corLamina(2, 2), corLamina(3, 2)],
    },

    padroes: {
      "1": [padraoLamina(1, 0), padraoLamina(2, 0), padraoLamina(3, 0)],
      "2": [padraoLamina(1, 1), padraoLamina(2, 1), padraoLamina(3, 1)],
      "3": [padraoLamina(1, 2), padraoLamina(2, 2), padraoLamina(3, 2)],
    }
  };

  localStorage.setItem("montagem", JSON.stringify(parametros));
}

window.changePedidoView = changePedidoView;
window.changeTampaView = changeTampaView;
window.confirmTampa = confirmTampa;
window.confirm = confirm;
window.spin = spin;
window.spinModal = spinModal;
window.openModal = openModal;
window.closeModal = closeModal;
window.enviarTampa = enviarTampa;