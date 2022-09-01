COLDIGO.marca = new Object();

$(document).ready(function() {

	//Cadastra no BD a marca informada
	COLDIGO.marca.cadastrar = function() {

		var marca = new Object();
		marca.nome = document.frmAddMarca.nome.value;

		if (marca.nomeMarca == "") {
			COLDIGO.exibirAviso("Preencha todos os campos!");

		} else {

			$.ajax({
				type: "POST",
				url: COLDIGO.PATH + "marca/inserir",
				data: JSON.stringify(marca),
				success: function(msg) {
					COLDIGO.exibirAviso(msg);
					$("#addMarca").trigger("reset");
					COLDIGO.marca.buscarPorNome();


				},
				error: function(info) {
					COLDIGO.exibirAviso("Erro ao cadastrar uma nova marca: " + info.status + " - " + info.statusText);
				}
			});

		}
	};

	//Busca no BD e exibe na página as marcas que atendam á solicitação do usuário
	COLDIGO.marca.buscarPorNome = function() {

		var valorBusca = $("#campoBuscaMarca").val();

		$.ajax({
			type: "GET",
			url: COLDIGO.PATH + "marca/buscarPorNome",
			data: "valorBusca=" + valorBusca,
			success: function(dados) {

				dados = JSON.parse(dados);

				$("#listaMarcas").html(COLDIGO.marca.exibir(dados));
			},
			error: function(info) {
				COLDIGO.exibirAviso("Erro ao consultar os dados: " + info.status + " - " + info.statusText);
			}
		});

	};

	//Transforma os dados das marcas recebidas do servidor em uma tabela HTML
	COLDIGO.marca.exibir = function(listaMarcas) {

		var tabela = "<table class='listaRegistrosMarca'>" +
			"<tr>" +
			"<th>ID</th>" +
			"<th>Marca</th>" +
			"<th class='acoes'>Ações</th>" +
			"</tr>";

		if (listaMarcas != undefined && listaMarcas.length > 0) {

			for (var i = 0; i < listaMarcas.length; i++) {
				tabela += "<tr>" +
					"<td>" + listaMarcas[i].id + "</td>" +
					"<td>" + listaMarcas[i].nome + "</td>" +
					"<td>" +
					"<a onclick=\"COLDIGO.marca.exibirEdicao('" + listaMarcas[i].id + "')\"><img src='../../imgs/edit.png' alt='Editar registro'></a> " +
					"<a onclick=\"COLDIGO.marca.excluir('" + listaMarcas[i].id + "')\"><img src='../../imgs/delete.png' alt='Excluir registro'></a>" +
					"</td>" +
					"</tr>"
			}

		} else if (listaMarcas == "") {
			tabela += "<tr><td colspan='6'> Nenhum registro encontrado </td></tr>";
		}
		tabela += "</table>";

		return tabela;


	};

	//Executa a função de busca ao carregar a página
	COLDIGO.marca.buscarPorNome();

	//Exclui a marca selecionada
	COLDIGO.marca.excluir = function(id) {
		$.ajax({
			type: "DELETE",
			url: COLDIGO.PATH + "marca/excluir/" + id,
			success: function(msg) {
				COLDIGO.exibirAviso(msg);
				COLDIGO.marca.buscarPorNome();
			},
			error: function(info) {
				COLDIGO.exibirAviso("Erro ao excluir a marca: " + info.status + " - " + info.statusText);
			}
		});
	};

	//Carrega no BD os dados da marca selecionada para alteração e coloca-os no formulário de alteração
	COLDIGO.marca.exibirEdicao = function(id) {
		$.ajax({
			type: "GET",
			url: COLDIGO.PATH + "marca/buscarPorId",
			data: "id=" + id,
			success: function(marca) {

				document.frmEditaMarca.idMarca.value = marca.id;
				document.frmEditaMarca.nome.value = marca.nome;

				var modalEditaMarca = {
					title: "Editar Marca",
					height: 200,
					width: 350,
					modal: true,
					buttons: {
						"Salvar": function() {

							COLDIGO.marca.editar();
						},
						"Cancelar": function() {
							$(this).dialog("close");
						}
					},
					close: function() {
						//caso o usuário simplesmente feche a caixa de edição
						//não deve acontecer nada
					}
				};
				$("#modalEditaMarca").dialog(modalEditaMarca);


			},
			error: function(info) {
				COLDIGO.exibirAviso("Erro ao buscar marca para edição: " + info.status + " - " + info.statusText);
			}
		});
	};

	//Realiza a edição dos dados no BD
	COLDIGO.marca.editar = function() {

		var marca = new Object();
		marca.id = document.frmEditaMarca.idMarca.value;
		marca.nome = document.frmEditaMarca.nome.value;
		

		$.ajax({
			type: "PUT",
			url: COLDIGO.PATH + "marca/alterar",
			data: JSON.stringify(marca),
			success: function(msg) {
				msg = JSON.parse(msg);

				COLDIGO.exibirAviso(msg);
				COLDIGO.marca.buscarPorNome();
				$("#modalEditaMarca").dialog("close");

			},
			error: function(info) {
				COLDIGO.exibirAviso("Erro ao editar marca: " + info.status + " - " + info.statusText);

			}
		});

	};
});