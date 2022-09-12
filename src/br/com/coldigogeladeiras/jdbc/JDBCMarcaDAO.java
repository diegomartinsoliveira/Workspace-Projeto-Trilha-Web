package br.com.coldigogeladeiras.jdbc;

import java.sql.Statement;
import java.sql.Connection;
import java.sql.PreparedStatement;
import java.sql.ResultSet;
import java.sql.SQLException;
import java.util.ArrayList;
import java.util.List;
import br.com.coldigogeladeiras.jdbcinterface.MarcaDAO;
import br.com.coldigogeladeiras.modelo.Marca;

public class JDBCMarcaDAO implements MarcaDAO {

	private Connection conexao;

	public JDBCMarcaDAO(Connection conexao) {
		this.conexao = conexao;
	}

	public boolean inserir(Marca marca) {

		String comando = "INSERT INTO marcas (id, nome) VALUES (?,?)";
		PreparedStatement p;

		try {

			// Prepara o comando para execução no BD em que nos conectamos
			p = this.conexao.prepareStatement(comando);

			// Substitui no comando os "?" pelos valores da marca
			p.setInt(1, marca.getId());
			p.setString(2, marca.getNome());
			// Executa o comando no BD
			p.execute();

		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
		return true;

	}

	public List<Marca> buscar() {

		// Criação da instrução SQL para busca de todas as marcas
		String comando = "SELECT * FROM marcas";

		// Criação de uma lista para armazenar cada marca encontrada
		List<Marca> listMarcas = new ArrayList<Marca>();

		// Criação do objeto marca com valor null (ou seja, sem instanciá-lo)
		Marca marca = null;

		// Abertura do try-catch
		try {

			// Uso da conexão do banco para prepará-lo para uma instrução SQL
			Statement stmt = conexao.createStatement();

			// Execução da instrução criada previamente
			// e armazenamento do resultado no objeto rs
			ResultSet rs = stmt.executeQuery(comando);

			// Enquanto houver uma próxima linha no resultado
			while (rs.next()) {

				// Criação de instância da classe Marca
				marca = new Marca();

				// Recebimento dos 2 dados retornados do BD para cada linha encontrada
				int id = rs.getInt("id");
				String nome = rs.getString("nome");

				// Setando no objeto marca os valores encontrados
				marca.setId(id);
				marca.setNome(nome);

				// Adição da instância contida no objeto Marca na lista de marcas
				listMarcas.add(marca);
			}

			// Caso alguma Exception seja gerada no try, recebe-a no objeto "ex"
		} catch (Exception ex) {
			// Exibe a exceção no console
			ex.printStackTrace();
		}
		// Retorna para quem chamou o metodo a lista criada.
		return listMarcas;

	}

	public List<Marca> buscarPorNome(String nome) {

		// Inicia criação do comando SQL de busca
		String comando = "SELECT * FROM marcas ";
		// Se o nome não estiver vazio...
		if (!nome.equals("")) {
			// concatena no comando o WHERE buscando no nome da marca
			// o texto da variável nome
			comando += "WHERE nome LIKE '%" + nome + "%' ";
		}
		// Finaliza o comando ordenando alfabeticamente por
		// categoria, marca e depois modelo.
		comando += "ORDER BY nome ASC";

		List<Marca> listaMarcas = new ArrayList<Marca>();
		Marca marca = null;

		try {

			Statement stmt = conexao.createStatement();
			ResultSet rs = stmt.executeQuery(comando);

			while (rs.next()) {

				// Criação de instância da classe Marca
				marca = new Marca();

				// Recebimento dos 2 dados retornados do BD para cada linha encontrada
				int id = rs.getInt("id");
				String nomeMarca = rs.getString("nome");

				// Setando no objeto marca os valores encontrados
				marca.setId(id);
				marca.setNome(nomeMarca);

				// Adição da instância contida no objeto Marca na lista de marcas
				listaMarcas.add(marca);
			}

		} catch (Exception e) {
			e.printStackTrace();
		}

		return listaMarcas;
	}
	
	public boolean deletar(int id) {
		String comando = "DELETE FROM marcas WHERE id = ?";
		PreparedStatement p;
		try {
			p = this.conexao.prepareStatement(comando);
			p.setInt(1, id);
			p.execute();
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
		return true;
	}
	
	public Marca buscarPorId(int id) {

		String comando = "SELECT * FROM marcas WHERE marcas.id = ?";
		Marca marca = new Marca();

		try {
			PreparedStatement p = this.conexao.prepareStatement(comando);
			p.setInt(1, id);
			ResultSet rs = p.executeQuery();
			while (rs.next()) {

				String nome = rs.getString("nome");
				
				marca.setId(id);
				marca.setNome(nome);
			}
		} catch (Exception e) {
			e.printStackTrace();
		}
		return marca;
	}

	public boolean alterar(Marca marca) {

		String comando = "UPDATE marcas " + "SET nome=?" + " WHERE id=?";
		PreparedStatement p;

		try {
			p = this.conexao.prepareStatement(comando);
			p.setString(1, marca.getNome());
			p.setInt(2, marca.getId());
			p.executeUpdate();
		} catch (SQLException e) {
			e.printStackTrace();
			return false;
		}
		return true;

	}

}
