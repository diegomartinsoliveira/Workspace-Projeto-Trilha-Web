package br.com.coldigogeladeiras.rest;

import java.sql.Connection;
import java.util.ArrayList;
import java.util.List;

import javax.ws.rs.Consumes;
import javax.ws.rs.DELETE;
import javax.ws.rs.GET;
import javax.ws.rs.POST;
import javax.ws.rs.PUT;
import javax.ws.rs.Path;
import javax.ws.rs.PathParam;
import javax.ws.rs.Produces;
import javax.ws.rs.QueryParam;
import javax.ws.rs.core.MediaType;
import javax.ws.rs.core.Response;

import com.google.gson.Gson;
import com.google.gson.JsonObject;

import br.com.coldigogeladeiras.bd.Conexao;
import br.com.coldigogeladeiras.jdbc.JDBCMarcaDAO;
import br.com.coldigogeladeiras.jdbc.JDBCProdutoDAO;
import br.com.coldigogeladeiras.modelo.Marca;
import br.com.coldigogeladeiras.modelo.Produto;

@Path("produto")
public class ProdutoRest extends UtilRest {

	@POST
	@Path("/inserir")
	@Consumes("application/*")
	public Response inserir(String produtoParam) {

		try {

			String msg = "";
			Produto produto = new Gson().fromJson(produtoParam, Produto.class);
			Conexao conec = new Conexao();
			Connection conexao = conec.abrirConexao();
			JDBCProdutoDAO jdbcProduto = new JDBCProdutoDAO(conexao);
			JDBCMarcaDAO jdbcMarca = new JDBCMarcaDAO(conexao);
			boolean retornoProdutoDuplicado = jdbcProduto.verificaProdutoDuplicado(produto);

			if (retornoProdutoDuplicado) {

				Marca marca = jdbcMarca.buscarPorId(produto.getMarcaId());

				if (marca.getId() != produto.getMarcaId()) {

					return this.buildErrorResponse("Essa marca não existe mais, atualize a página");

				} else {

					boolean retorno = jdbcProduto.inserir(produto);
					conec.fecharConexao();

					if (retorno) {

						msg = "Produto cadastrado com sucesso!";

					} else {

						return this.buildErrorResponse("Erro ao cadastrar produto");
					}
				}
			} else {

				return this.buildErrorResponse("Este produto já está cadastrado!");

			}

			return this.buildResponse(msg);

		} catch (Exception e) {
			e.printStackTrace();
			return this.buildErrorResponse(e.getMessage());
		}
	}

	@GET
	@Path("/buscar")
	@Consumes("application/*")
	@Produces(MediaType.APPLICATION_JSON)
	public Response buscarPorNome(@QueryParam("valorBusca") String nome) {

		try {

			List<JsonObject> listaProdutos = new ArrayList<JsonObject>();

			Conexao conec = new Conexao();
			Connection conexao = conec.abrirConexao();
			JDBCProdutoDAO jdbcProduto = new JDBCProdutoDAO(conexao);
			listaProdutos = jdbcProduto.buscarPorNome(nome);
			conec.fecharConexao();

			String json = new Gson().toJson(listaProdutos);
			return this.buildResponse(json);

		} catch (Exception e) {
			e.printStackTrace();
			return this.buildErrorResponse(e.getMessage());
		}

	}

	@DELETE
	@Path("/excluir/{id}")
	@Consumes("application/*")
	public Response excluir(@PathParam("id") int id) {

		try {

			Conexao conec = new Conexao();
			Connection conexao = conec.abrirConexao();
			JDBCProdutoDAO jdbcProduto = new JDBCProdutoDAO(conexao);

			boolean retorno = jdbcProduto.deletar(id);
			conec.fecharConexao();
			String msg = "";

			if (retorno) {

				msg = "Produto excluído com sucesso!";

			} else {

				return this.buildErrorResponse("Erro ao excluir produto.");
			}

			return this.buildResponse(msg);

		} catch (Exception e) {
			e.printStackTrace();
			return this.buildErrorResponse(e.getMessage());
		}

	}

	@GET
	@Path("/buscarPorId")
	@Consumes("application/*")
	@Produces(MediaType.APPLICATION_JSON)
	public Response buscarPorId(@QueryParam("id") int id) {

		try {
			Produto produto = new Produto();
			Conexao conec = new Conexao();
			Connection conexao = conec.abrirConexao();
			JDBCProdutoDAO jdbcProduto = new JDBCProdutoDAO(conexao);

			produto = jdbcProduto.buscarPorId(id);

			conec.fecharConexao();

			return this.buildResponse(produto);

		} catch (Exception e) {
			e.printStackTrace();
			return this.buildErrorResponse(e.getMessage());
		}
	}

	@PUT
	@Path("/alterar")
	@Consumes("application/*")
	public Response alterar(String produtoParam) {

		try {
			Produto produto = new Gson().fromJson(produtoParam, Produto.class);
			Conexao conec = new Conexao();
			Connection conexao = conec.abrirConexao();
			JDBCProdutoDAO jdbcProduto = new JDBCProdutoDAO(conexao);

			boolean retorno = jdbcProduto.alterar(produto);

			String msg = "";
			conec.fecharConexao();
			if (retorno) {

				msg = "Produto alterado com sucesso!";

			} else {

				return this.buildErrorResponse("Erro ao alterar produto.");
			}

			return this.buildResponse(msg);

		} catch (Exception e) {
			e.printStackTrace();
			return this.buildErrorResponse(e.getMessage());
		}

	}

}