package br.com.coldigogeladeiras.rest;

import javax.ws.rs.core.Response;
import javax.ws.rs.core.Response.ResponseBuilder;
import com.google.gson.Gson;

public class UtilRest {

	public Response buildResponse(Object result) {
		try {
			// Retorna o objeto de resposta com status ok, tendo em seu corpo o objeto
			// valorResposta (que consiste no objeto result convertido para JSON
			String valorResposta = new Gson().toJson(result);
			return Response.ok(valorResposta).build();
		} catch (Exception ex) {
			ex.printStackTrace();
			return this.buildErrorResponse(ex.getMessage());
		}
	}

	public Response buildErrorResponse(String str) {
		// Abaixo o objeto rb recebe o status do erro
		ResponseBuilder rb = Response.status(Response.Status.INTERNAL_SERVER_ERROR);
		// Define a entidade (objeto), que nesse caso � uma mensagem que ser� retornado
		// para o cliente
		rb = rb.entity(str);
		// Define o tipo de retorno do objeto, no caso definido como texto simples.
		rb = rb.type("text/plain");
		// Retorna o objeto de resposta com status erro, junto com a string contendo a
		// mensagem de erro.
		return rb.build();
	}
}