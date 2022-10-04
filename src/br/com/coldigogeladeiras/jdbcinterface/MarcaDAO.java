package br.com.coldigogeladeiras.jdbcinterface;
	
import java.util.List;

import br.com.coldigogeladeiras.modelo.Marca;

public interface MarcaDAO {
	
public boolean inserir (Marca marca);	
public List<Marca> buscar();
public List<Marca> buscarPorNome(String nome);
public boolean deletar(int id);
public Marca buscarPorId(int id);
public boolean alterar(Marca marca);
public boolean verificaStatus(int id);



	

}

