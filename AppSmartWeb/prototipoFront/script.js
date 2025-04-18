// Função para atualizar a visualização do pedido
function changePedidoView() {
  // Obtemos o valor do select de cor do bloco
  var blockColor = $("#block-color").val();

  if (blockColor !== "") {
    $("#bloco1").attr("src", "bloco/rBlocoCor" + blockColor + ".png");

    // Ativa os selects das lâminas
    $("#l1-color").prop("disabled", false);
    $("#l2-color").prop("disabled", false);
    $("#l3-color").prop("disabled", false);

    // Atualiza as imagens das lâminas e padrões com base nas escolhas
    $("#lamina1-1").attr("src", $("#l1-color").val() ? "laminas/lamina1-" + $("#l1-color").val() + ".png" : "#");
    $("#lamina1-2").attr("src", $("#l2-color").val() ? "laminas/lamina2-" + $("#l2-color").val() + ".png" : "#");
    $("#lamina1-3").attr("src", $("#l3-color").val() ? "laminas/lamina3-" + $("#l3-color").val() + ".png" : "#");

    $(".padrao1-1").attr("src", $("#l1-pattern").val() ? "padroes/padrao" + $("#l1-pattern").val() + "-1.png" : "#");
    $(".padrao1-2").attr("src", $("#l2-pattern").val() ? "padroes/padrao" + $("#l2-pattern").val() + "-2.png" : "#");

    // Ativa ou desativa os campos de padrão com base na seleção da cor da lâmina
    $("#l1-pattern").attr("disabled", !$("#l1-color").val());
    $("#l2-pattern").attr("disabled", !$("#l2-color").val());
    $("#l3-pattern").attr("disabled", !$("#l3-color").val());
  } else {
    // Caso não haja cor do bloco, desabilita os campos das lâminas
    $("#l1-color").prop("disabled", true);
    $("#l2-color").prop("disabled", true);
    $("#l3-color").prop("disabled", true);
    $("#l1-pattern").prop("disabled", true);
    $("#l2-pattern").prop("disabled", true);
    $("#l3-pattern").prop("disabled", true);

    // Reseta as imagens
    $("#bloco1").attr("src", "bloco/rBlocoCor0.png");
    $("#lamina1-1").attr("src", "#");
    $("#lamina1-2").attr("src", "#");
    $("#lamina1-3").attr("src", "#");
    $(".padrao1-1").attr("src", "#");
    $(".padrao1-2").attr("src", "#");
  }
}

// Função para girar as lâminas
function spin1() {
  $('#pedido-view1').toggleClass("spin");

  // Seleciona as lâminas
  var $lamina1 = $('#lamina1-1');
  var $lamina3 = $('#lamina1-3');

  // Pega os src atuais
  var src1 = $lamina1.attr('src');
  var src3 = $lamina3.attr('src');

  var newSrc3 = src1.replace(/lamina(\d)-(\d)\.png/, function(match, pos, cor) {
    return `lamina3-${cor}.png`;
  });

  var newSrc1 = src3.replace(/lamina(\d)-(\d)\.png/, function(match, pos, cor) {
    return `lamina1-${cor}.png`;
  });

  // Aplica os novos src
  $lamina1.attr('src', newSrc1);
  $lamina3.attr('src', newSrc3);
}