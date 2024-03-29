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
import br.com.coldigogeladeiras.bd.Conexao;
import br.com.coldigogeladeiras.jdbc.JDBCMarcaDAO;
import br.com.coldigogeladeiras.jdbc.JDBCProdutoDAO;
import br.com.coldigogeladeiras.modelo.Marca;
import br.com.coldigogeladeiras.modelo.Produto;

@Path("marca")
public class MarcaRest extends UtilRest {

	@POST
	@Path("/inserir")
	@Consumes("application/*")
	public Response inserir(String marcaParam) {

		try {

			Marca marca = new Gson().fromJson(marcaParam, Marca.class);

			Conexao conec = new Conexao();
			Connection conexao = conec.abrirConexao();
			JDBCMarcaDAO jdbcMarca = new JDBCMarcaDAO(conexao);
			boolean retornoMarcaDuplicada = jdbcMarca.verificaMarcaDuplicada(marca);
			String msg = "";
			if (retornoMarcaDuplicada) {

				boolean retorno = jdbcMarca.inserir(marca);
				conec.fecharConexao();

				if (retorno) {

					msg = "Marca cadastrada com sucesso!";

				} else {

					return this.buildErrorResponse("Erro ao cadastrar marca");

				}

			} else {

				return this.buildErrorResponse("Esta marca já está cadastrada!");
			}

			return this.buildResponse(msg);

		} catch (Exception e) {
			e.printStackTrace();
			return this.buildErrorResponse(e.getMessage());
		}

	}

	@GET
	@Path("/buscar")
	@Produces(MediaType.APPLICATION_JSON)
	public Response buscar() {
		try {
			List<Marca> listaMarcas = new ArrayList<Marca>();

			Conexao conec = new Conexao();
			Connection conexao = conec.abrirConexao();
			JDBCMarcaDAO jdbcMarca = new JDBCMarcaDAO(conexao);
			listaMarcas = jdbcMarca.buscar();
			conec.fecharConexao();
			return this.buildResponse(listaMarcas);
		} catch (Exception e) {
			e.printStackTrace();
			return this.buildErrorResponse(e.getMessage());
		}
	}

	@GET
	@Path("/buscarPorNome")
	@Consumes("application/*")
	@Produces(MediaType.APPLICATION_JSON)
	public Response buscarPorNome(@QueryParam("valorBusca") String nome) {

		try {

			List<Marca> listaMarcas = new ArrayList<Marca>();

			Conexao conec = new Conexao();
			Connection conexao = conec.abrirConexao();
			JDBCMarcaDAO jdbcMarca = new JDBCMarcaDAO(conexao);
			listaMarcas = jdbcMarca.buscarPorNome(nome);
			conec.fecharConexao();

			String json = new Gson().toJson(listaMarcas);
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

			String msg = "";
			Conexao conec = new Conexao();
			Connection conexao = conec.abrirConexao();
			JDBCMarcaDAO jdbcMarca = new JDBCMarcaDAO(conexao);
			JDBCProdutoDAO jdbcProduto = new JDBCProdutoDAO(conexao);

			Marca marca = jdbcMarca.buscarPorId(id);

			if (marca.getId() != id) {

				return this.buildErrorResponse("Essa marca já foi excluida");

			} else {
				List<Produto> listaProdutos = jdbcProduto.produtosVinculados(id);

				if (!listaProdutos.isEmpty()) {

					return this.buildErrorResponse("Existe produto vinculado a essa marca, não será possível excluir");

				} else {

					boolean retorno = jdbcMarca.deletar(id);
					conec.fecharConexao();

					if (retorno) {

						msg = "Marca excluída com sucesso!";

					} else {

						return this.buildErrorResponse("Erro ao excluir marca!");

					}

				}
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
			Marca marca = new Marca();
			Conexao conec = new Conexao();
			Connection conexao = conec.abrirConexao();
			JDBCMarcaDAO jdbcMarca = new JDBCMarcaDAO(conexao);

			marca = jdbcMarca.buscarPorId(id);

			conec.fecharConexao();

			return this.buildResponse(marca);

		} catch (Exception e) {
			e.printStackTrace();
			return this.buildErrorResponse(e.getMessage());
		}
	}

	@PUT
	@Path("/alterar")
	@Consumes("application/*")
	public Response alterar(String marcaParam) {

		try {
			Marca marca = new Gson().fromJson(marcaParam, Marca.class);
			Conexao conec = new Conexao();
			Connection conexao = conec.abrirConexao();
			JDBCMarcaDAO jdbcMarca = new JDBCMarcaDAO(conexao);

			boolean retorno = jdbcMarca.alterar(marca);
			conec.fecharConexao();
			String msg = "";
			if (retorno) {

				msg = "Marca alterada com sucesso!";

			} else {

				return this.buildErrorResponse("Erro ao alterar marca");

			}

			return this.buildResponse(msg);

		} catch (Exception e) {
			e.printStackTrace();
			return this.buildErrorResponse(e.getMessage());
		}

	}

	@PUT
	@Path("/updateStatus/{id}")
	@Consumes("application/*")
	public Response updateStatus(@PathParam("id") int id) {

		try {

			String msg = "";
			Conexao conec = new Conexao();
			Connection conexao = conec.abrirConexao();
			JDBCMarcaDAO jdbcMarca = new JDBCMarcaDAO(conexao);
			Marca marca = jdbcMarca.buscarPorId(id);

			if (marca.getId() != id) {

				return this.buildErrorResponse("Essa marca não existe mais, atualize a página");

			} else {

				boolean ativo = jdbcMarca.verificaStatus(id);

				int statusAtualizado;

				if (ativo) {
					statusAtualizado = 0;
				} else {
					statusAtualizado = 1;
				}

				boolean retorno = jdbcMarca.updateStatus(statusAtualizado, id);

				conec.fecharConexao();

				if (retorno) {

					msg = "Status da marca alterada com sucesso!";

				} else {

					return this.buildErrorResponse("Erro ao alterar o status da marca!");

				}
			}

			return this.buildResponse(msg);

		} catch (Exception e) {
			e.printStackTrace();
			return this.buildErrorResponse(e.getMessage());
		}
	}
}