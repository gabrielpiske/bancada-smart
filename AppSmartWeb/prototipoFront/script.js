function changePedidoView(){
  $("#bloco1").attr("src",$('#block-color').val() ? "bloco/rBlocoCor"+$('#block-color').val()+".png" : "bloco/rBlocoCor0.png");
  $("#lamina1-1").attr("src", $("#l1-color").val() ? "laminas/lamina1-"+$("#l1-color").val()+".png" : "#");
  $("#lamina1-2").attr("src", $("#l2-color").val() ? "laminas/lamina2-"+$("#l2-color").val()+".png" : "#");
  $("#lamina1-3").attr("src", $("#l3-color").val() ? "laminas/lamina3-"+$("#l3-color").val()+".png" : "#");
  $(".padrao1-1").attr("src", $("#l1-pattern").val() ? "padroes/padrao"+$("#l1-pattern").val()+"-1.png" : "#");
  $(".padrao1-2").attr("src", $("#l2-pattern").val() ? "padroes/padrao"+$("#l2-pattern").val()+"-2.png" : "#");

  $("#l1-pattern").attr("disabled", !$("#l1-color").val());
  $("#l2-pattern").attr("disabled", !$("#l2-color").val());
  $("#l3-pattern").attr("disabled", !$("#l3-color").val());
}

function spin1(){
  $('#pedido-view1').toggleClass("spin");
  var lamina1 = $('#lamina1-1').attr('src');
  var lamina3 = $('#lamina1-3').attr('src');
  $('.lamina1-1').attr('src', lamina3);
  $('.lamina1-3').attr('src', lamina1);
}