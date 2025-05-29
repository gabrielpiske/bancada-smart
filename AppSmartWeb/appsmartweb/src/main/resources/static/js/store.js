var isSpun = false;
var isConfirm = false;

const MAX_BLOCOS = 3;
let blocoCount = 1;

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
    // Atualiza bloco
    $(`#bloco-${id}`).attr("src", `assets/bloco/rBlocoCor${blockColor}.png`);

    // Habilita/desabilita controles
    [$lamina1, $lamina2, $lamina3].forEach(el => el.prop("disabled", false));

    // Obtem valores
    const [l1Color, l2Color, l3Color] = [$lamina1, $lamina2, $lamina3].map(el => el.val());
    const [l1Pattern, l2Pattern, l3Pattern] = ['l1', 'l2', 'l3'].map(p => $(`#${p}-pattern-${id}`).val());

    // Atualiza botão de envio
    $sendBtn.prop("disabled", !(l1Color && l2Color && l3Color));

    // Atualiza imagens das lâminas
    updateLaminaImages(id, isSpun, l1Color, l2Color, l3Color, l1Pattern, l2Pattern, l3Pattern);
  } else {
    // Estado padrão quando não há cor selecionada
    resetPedidoView(id, $lamina1, $lamina2, $lamina3, $sendBtn);
  }

  verificarEstadoBlocos();
}

function updateLaminaImages(id, isSpun, l1Color, l2Color, l3Color, l1Pattern, l2Pattern, l3Pattern) {
  const $lamina1 = $(`#lamina${id}-1`);
  const $lamina3 = $(`#lamina${id}-3`);
  const $padrao1 = $(`#padrao${id}-1`);
  const $padrao3 = $(`#padrao${id}-3`);

  // Atualiza lâminas 1 e 3 baseado no estado de rotação
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

  // Lâmina 2 não muda com rotação
  $(`#lamina${id}-2`).attr("src", l2Color ? `assets/laminas/lamina2-${l2Color}.png` : "#");
  $(`#padrao${id}-2`).attr("src", l2Pattern ? `assets/padroes/padrao${l2Pattern}-2.png` : "#");

  // Habilita/desabilita padrões
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

  $view.toggleClass("spin").data("isSpun", !isSpun);

  // Alterna lâminas e padrões
  const $lamina1 = $(`#lamina${id}-1`);
  const $lamina3 = $(`#lamina${id}-3`);
  const $padrao1 = $(`#padrao${id}-1`);
  const $padrao3 = $(`#padrao${id}-3`);

  // Troca src entre lâminas 1 e 3
  const src1 = $lamina1.attr('src');
  const src3 = $lamina3.attr('src');

  $lamina1.attr('src', src3.replace(/lamina(\d)-(\d)/, 'lamina1-$2'));
  $lamina3.attr('src', src1.replace(/lamina(\d)-(\d)/, 'lamina3-$2'));

  // Alterna visibilidade dos padrões
  $padrao3.prop("hidden", !isSpun);
  $padrao1.prop("hidden", isSpun);
}

function verificarEstadoBlocos() {
  const blocosDesativados = $('section[id^="section-bloco-"].disabled').length;
  $("#enviar-pedido").prop("disabled", blocosDesativados === 0);
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

  // Atualização específica para o pedido-view
  const $pedidoView = $original.find(".pedido-view")
    .attr("id", `pedido-view${id}`)
    .removeClass("spin")
    .data("isSpun", false);

  // Atualização geral dos elementos
  $original.find("*").each(function () {
    const $el = $(this);
    const attributes = ["id", "for", "name", "onclick", "onchange"];

    attributes.forEach(attr => {
      const val = $el.attr(attr);
      if (val) {
        // Substituição mais abrangente
        $el.attr(attr, val
          .replace(/pedido-view\d*/g, `pedido-view${id}`)
          .replace(/(lamina|padrao)(\d+)-(\d+)/g, (_, tipo, x, y) => `${tipo}${id}-${y}`)
          .replace(/([^\d])-1\b/g, `$1-${id}`)
          .replace(/\b(spin|confirm|changePedidoView)\(\d+\)/g, `$1(${id})`)
        );
      }
    });
  });

  // Configuração dos selects
  $original.find("select").val("").prop("disabled", true);
  $original.find(`#block-color-${id}`).prop("disabled", false);

  // Configuração do botão
  $original.find('button[type="button"]')
    .attr("id", `send-${id}`)
    .prop("disabled", true)
    .text("Confirmar");

  // Reset das imagens
  $original.find("img").each(function () {
    const $img = $(this);
    if ($img.attr("id")?.startsWith("bloco-")) {
      $img.attr("src", "assets/bloco/rBlocoCor0.png");
    } else {
      $img.attr("src", "#");
    }
  });

  if (id > 1) {
    addDeleteButton($original);
  }

  return $original;
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
}

// Inicialização
$document.ready(function () {
  // Event delegation para elementos dinâmicos
  $document.on('click', '.plus span', handleAddBlock);
  $document.on('click', '.delete-btn', handleDeleteBlock);
});

function verBlocosMontados() {
  const alturaBloco = document.getElementById("modal-bloco1").offsetHeight;
  const fatorMultiplicador = 0.445;
  const dif = 40;

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
  updateModalImages();

  setTimeout(() => {
    verBlocosMontados();
  }, 10);

  document.getElementById('pedidoModal').style.display = 'block';
}

function closeModal() {
  document.getElementById('pedidoModal').style.display = 'none';
}

function submitOrder() {
  closeModal();
  enviarPedido();
  window.location.href = '/view';
}

function updateModalImages() {
  $('[id^="modal-bloco"]').hide();
  $('[id*="modal"]:not([id*="tampa"])').each(function () {
    $(this).removeAttr("src");
  });

  $('section[id^="section-bloco-"].disabled').each(function (index) {
    const sectionId = this.id.split('-')[2]; // ID real do bloco (pode ser 1, 4, 5, etc.)
    const modalIndex = index + 1; // posição sequencial para o modal (1, 2, 3)

    const blocoModal = $(`#modal-bloco${modalIndex}`);
    const blockColor = $(`#block-color-${sectionId}`).val();

    blocoModal.show();
    blocoModal.attr("src", `assets/blocks/rblocoCor${blockColor}.png`);

    ['1', '2', '3'].forEach(l => {
      const lColor = $(`#l${l}-color-${sectionId}`).val();
      const lPattern = $(`#l${l}-pattern-${sectionId}`).val();

      if (lColor) {
        $(`#modal-lamina${modalIndex}-${l}`).attr("src", `assets/laminas/lamina${l}-${lColor}.png`);
      }
      if (lPattern) {
        $(`#modal-padrao${modalIndex}-${l}`).attr("src", `assets/padroes/padrao${lPattern}-${l}.png`);
      }
    });
  });

}

function enviarPedido() {
  const formData = new FormData();
  let hasData = false;

  // Iterar sobre todas as seções de bloco que estão disabled (confirmadas)
  $('section[id^="section-bloco-"].disabled').each(function () {
    const sectionId = this.id.split('-')[2]; // Extrai o número do bloco (1, 2, 3...)
    hasData = true;

    // Adicionar todos os campos do formulário mantendo a numeração original
    const blockColor = $(`#block-color-${sectionId}`).val();
    const l1Color = $(`#l1-color-${sectionId}`).val();
    const l2Color = $(`#l2-color-${sectionId}`).val();
    const l3Color = $(`#l3-color-${sectionId}`).val();
    const l1Pattern = $(`#l1-pattern-${sectionId}`).val();
    const l2Pattern = $(`#l2-pattern-${sectionId}`).val();
    const l3Pattern = $(`#l3-pattern-${sectionId}`).val();

    // Adiciona os dados ao FormData com prefixo para cada bloco
    formData.append(`block-color-${sectionId}`, blockColor);
    formData.append(`l1-color-${sectionId}`, l1Color);
    formData.append(`l2-color-${sectionId}`, l2Color);
    formData.append(`l3-color-${sectionId}`, l3Color);
    if (l1Pattern) formData.append(`l1-pattern-${sectionId}`, l1Pattern);
    if (l2Pattern) formData.append(`l2-pattern-${sectionId}`, l2Pattern);
    if (l3Pattern) formData.append(`l3-pattern-${sectionId}`, l3Pattern);
  });

  if (!hasData) {
    alert("Nenhum bloco confirmado para envio!");
    return;
  }

  // Adiciona o número total de blocos confirmados
  formData.append('total-blocks', $('section[id^="section-bloco-"].disabled').length);

  // Enviar para o servidor
  fetch("/pedidoTeste", {
    method: "POST",
    body: formData,
  }).then((response) => {
    if (response.redirected) {
      window.location.href = response.url;
    }
  });
}

window.onclick = function (event) {
  const modal = document.getElementById('pedidoModal');
  if (event.target == modal) {
    closeModal();
  }
}