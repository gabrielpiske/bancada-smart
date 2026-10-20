import { getClpStatusFromLocalStorage, showMessage } from './utils.js';

export { changePedidoView, changeTampaView, confirmTampa, confirm, spin, spinModal, openModal, closeModal, enviarTampa };

var isSpun = false;
var isConfirm = false;

const MAX_BLOCOS = 3;
let blocoCount = 1;
let isSubmittingOrder = false;

const $document = $(document);
const $mainContainer = $('main');

function changePedidoView(id) {
  const $section = $(`#section-bloco-${id}`);
  const blockColor = $(`#block-color-${id}`).val();
  const $lamina1 = $(`#l1-color-${id}`);
  const $lamina2 = $(`#l2-color-${id}`);
  const $lamina3 = $(`#l3-color-${id}`);
  const $sendBtn = $(`#send-${id}`);
  const $pedidoView = $(`#pedido-view${id}`);
  const isSpun = $pedidoView.data("isSpun");

  if (blockColor) {
    $(`#bloco-${id}`).attr("src", `assets/blocos/rBlocoCor${blockColor}.png`);
    [$lamina1, $lamina2, $lamina3].forEach(el => el.prop("disabled", false));

    const [l1Color, l2Color, l3Color] = [$lamina1, $lamina2, $lamina3].map(el => el.val());
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
  $(`#padrao${id}-2`).attr("src", l2Pattern ? `assets/padroes/padrao${l2Pattern}-2.png` : "#");

  ['l1', 'l2', 'l3'].forEach((p, i) => {
    $(`#${p}-pattern-${id}`).prop("disabled", ![l1Color, l2Color, l3Color][i]);
  });
}

function confirm(id) {
  const $section = $(`#section-bloco-${id}`);
  const $button = $(`#send-${id}`);
  const isDisabled = $section.hasClass("disabled");

  $section.toggleClass("disabled", !isDisabled);
  $button.text(isDisabled ? "Confirmar" : "Cancelar");

  verificarEstadoBlocos();
}

function spin(id) {
  const $section = $(`#section-bloco-${id}`);
  if ($section.hasClass("disabled")) return;

  const $view = $(`#pedido-view${id}`);
  const isSpun = !$view.data("isSpun");
  $view.toggleClass("spin").data("isSpun", isSpun);

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
  const isSpun = $modalView.hasClass('spin');

  $modalView.toggleClass('spin', !isSpun);

  $('section[id^="section-bloco-"].disabled').each(function (index) {
    const modalIndex = index + 1;
    const $lamina1 = $(`#modal-lamina${modalIndex}-1`);
    const $lamina3 = $(`#modal-lamina${modalIndex}-3`);
    const $padrao1 = $(`#modal-padrao${modalIndex}-1`);
    const $padrao3 = $(`#modal-padrao${modalIndex}-3`);

    const src1 = $lamina1.attr('src');
    const src3 = $lamina3.attr('src');

    if (src1 && src3) {
      $lamina1.attr('src', src3.replace(/lamina(\d)-(\d)/, 'lamina1-$2'));
      $lamina3.attr('src', src1.replace(/lamina(\d)-(\d)/, 'lamina3-$2'));
    }

    if (!isSpun) {
      $padrao1.attr('src', $padrao1.attr('src')?.replace(/padrao(\d+)-1\.png/, 'padrao$1-3.png'));
      $padrao3.attr('src', $padrao3.attr('src')?.replace(/padrao(\d+)-3\.png/, 'padrao$1-1.png'));

      $padrao3.prop("hidden", false);
      $padrao1.prop("hidden", true);
    } else {
      $padrao1.attr('src', $padrao1.attr('src')?.replace(/padrao(\d+)-3\.png/, 'padrao$1-1.png'));
      $padrao3.attr('src', $padrao3.attr('src')?.replace(/padrao(\d+)-1\.png/, 'padrao$1-3.png'));

      $padrao1.prop("hidden", false);
      $padrao3.prop("hidden", true);
    }
  });
}

function verificarEstadoBlocos() {
  const blocosDesativados = $('section[id^="section-bloco-"].disabled').length;
  const tampaDesativada = $('section.section-tampa.disabled').length;
  $("#enviar-pedido").prop("disabled", blocosDesativados === 0 || tampaDesativada === 0);
}

function addDeleteButton($section) {
  $section.prepend('<span class="delete-btn material-symbols-rounded">close</span>');
}

function checkAndInsertHidden() {
  const currentBlocoCount = $('section[id^="section-bloco-"]').length;
  const hiddenExists = $('.hidden').length > 0;

  if (currentBlocoCount === 1 && !hiddenExists) {
    $('.plus').after('<section class="hidden"></section>');
  }
}

function cloneAndModifyBlock(id) {
  const $original = $("#section-bloco-1").clone(true).removeClass("disabled");
  $original.attr("id", `section-bloco-${id}`);

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
          .replace(/(lamina|padrao)(\d+)-(\d+)/g, (_, tipo, x, y) => `${tipo}${id}-${y}`)
          .replace(/([^\d])-1\b/g, `$1-${id}`)
          .replace(/\b(spin|confirm|changePedidoView)\(\d+\)/g, `$1(${id})`)
        );
      }
    });
  });

  $original.find("select").val("").prop("disabled", true);
  $original.find(`#block-color-${id}`).prop("disabled", false);

  $original.find('button[type="button"]')
    .attr("id", `send-${id}`)
    .prop("disabled", true)
    .text("Confirmar");

  $original.find("img").each(function () {
    const $img = $(this);
    if ($img.attr("id")?.startsWith("bloco-")) {
      $img.attr("src", "assets/blocos/rBlocoCor0.png");
    } else {
      $img.attr("src", "#");
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
  const isDisabled = $section.hasClass("disabled");

  $section.toggleClass("disabled", !isDisabled);
  $button.text(isDisabled ? "Confirmar" : "Cancelar");

  verificarEstadoBlocos();
}

function handleAddBlock() {
  const currentCount = $('section[id^="section-bloco-"]').length;
  if (currentCount >= MAX_BLOCOS) {
    alert(`Você atingiu o número máximo de blocos (${MAX_BLOCOS})`);
    return;
  }

  blocoCount++;
  const $newBlock = cloneAndModifyBlock(blocoCount);
  const $plusSection = $(this).closest('.plus');
  const $hiddenSection = $plusSection.siblings('.hidden');

  $plusSection.remove();
  $hiddenSection.remove();

  $mainContainer.append($newBlock);
  checkAndInsertHidden();

  if (currentCount + 1 < MAX_BLOCOS) {
    $newBlock.after('<section class="plus"><span class="material-symbols-rounded" title="Adicionar novo bloco">add</span></section>');
  }

  moveTampaToEnd();
}

function handleDeleteBlock() {
  $(this).closest('section[id^="section-bloco-"]').remove();

  if ($('.plus').length === 0) {
    $('section[id^="section-bloco-"]').last().after(
      '<section class="plus"><span class="material-symbols-rounded" title="Adicionar novo bloco">add</span></section>'
    );
  }

  checkAndInsertHidden();
  verificarEstadoBlocos();

  moveTampaToEnd();
}

// Inicialização
$document.ready(function () {
  $document.on('click', '.plus span', handleAddBlock);
  $document.on('click', '.delete-btn', handleDeleteBlock);

  moveTampaToEnd();

  // Exibe feedback caso a página tenha sido redirecionada com ?error=... ou ?success
  const urlParams = new URLSearchParams(window.location.search);
  if (urlParams.has("error")) {
    showMessage("error", urlParams.get("error") || "Erro ao processar o pedido.");
  } else if (urlParams.has("success")) {
    showMessage("success", "Pedido enviado com sucesso!");
  }
});

function verBlocosMontados() {
  const alturaBloco = document.getElementById("modal-bloco1").offsetHeight;
  const fatorMultiplicador = 0.445;
  const dif = 40;

  const tampa = document.getElementById("tampa-color").value;
  $(`#modal-tampa`).attr("src", `assets/tampas/rTampa${tampa}.png`);

  let confirmados = $('section[id^="section-bloco-"].disabled');
  confirmados.each(function (index) {
    const modalIndex = index + 1;
    const altura = ((index + 1) * fatorMultiplicador * alturaBloco) - dif + "px";

    $(`#modal-bloco${modalIndex}`).css("top", altura);
    $(`[id^="modal-lamina${modalIndex}-"]`).css("top", altura);
    $(`[id^="modal-padrao${modalIndex}-"]`).css("top", altura);
  });
}

function openModal() {
  $('.modal-pedido-view').removeClass('spin');
  updateModalImages();

  setTimeout(() => {
    verBlocosMontados();
  }, 40);

  document.getElementById('pedidoModal').style.display = 'block';
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
  $('[id^="modal-bloco"]').hide();

  $('section[id^="section-bloco-"].disabled').each(function (index) {
    const sectionId = this.id.split('-')[2];
    const modalIndex = index + 1;
    const blocoModal = $(`#modal-bloco${modalIndex}`);
    const blockColor = $(`#block-color-${sectionId}`).val();

    blocoModal.show();
    // Corrige BUG-15 (case-sensitivity: rBlocoCor com 'B' maiúsculo)
    blocoModal.attr("src", `assets/blocos/rBlocoCor${blockColor}.png`);

    ['1', '2', '3'].forEach(l => {
      const lColor = $(`#l${l}-color-${sectionId}`).val();
      const lPattern = $(`#l${l}-pattern-${sectionId}`).val();

      let laminaNum = l;
      if (isSpun) {
        if (l === '1') laminaNum = '3';
        else if (l === '3') laminaNum = '1';
      }

      if (lColor) {
        $(`#modal-lamina${modalIndex}-${l}`).attr("src", `assets/laminas/lamina${laminaNum}-${lColor}.png`);
      }
      if (lPattern) {
        $(`#modal-padrao${modalIndex}-${l}`).attr("src", `assets/padroes/padrao${lPattern}-${laminaNum}.png`);
        $(`#modal-padrao${modalIndex}-${l}`).prop("hidden", isSpun ? (l === '1') : (l === '3'));
      }
    });
  });
}

async function enviarPedido() {
  const formData = new FormData();
  let hasData = false;

  $('section[id^="section-bloco-"].disabled').each(function () {
    const sectionId = this.id.split('-')[2];
    hasData = true;

    const blockColor = $(`#block-color-${sectionId}`).val();
    const l1Color = $(`#l1-color-${sectionId}`).val();
    const l2Color = $(`#l2-color-${sectionId}`).val();
    const l3Color = $(`#l3-color-${sectionId}`).val();
    const l1Pattern = $(`#l1-pattern-${sectionId}`).val();
    const l2Pattern = $(`#l2-pattern-${sectionId}`).val();
    const l3Pattern = $(`#l3-pattern-${sectionId}`).val();

    formData.append(`block-color-${sectionId}`, blockColor);
    formData.append(`l1-color-${sectionId}`, l1Color);
    formData.append(`l2-color-${sectionId}`, l2Color);
    formData.append(`l3-color-${sectionId}`, l3Color);
    if (l1Pattern) formData.append(`l1-pattern-${sectionId}`, l1Pattern);
    if (l2Pattern) formData.append(`l2-pattern-${sectionId}`, l2Pattern);
    if (l3Pattern) formData.append(`l3-pattern-${sectionId}`, l3Pattern);
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
        showMessage("success", "Pedido enviado com sucesso!");
        window.location.href = response.url;
      }
    } else if (!response.ok) {
      showMessage("error", `Erro ao enviar pedido (HTTP ${response.status})`);
    } else {
      showMessage("success", "Pedido enviado com sucesso!");
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
    console.log("Resposta do ESP32: ", data);

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
  let arrayModais = Array.from(document.getElementsByClassName("section-block"));
  let arrayIdsNum = [];

  arrayModais.forEach((element) => {
    let id = element.getAttribute("id");
    arrayIdsNum.push(id.charAt(id.length - 1));
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

  let montagemJSON = JSON.stringify(parametros);
  localStorage.setItem("montagem", montagemJSON);
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
