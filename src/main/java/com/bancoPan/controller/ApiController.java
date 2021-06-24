package com.bancoPan.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.bancoPan.entity.ApiEntity;
import com.bancoPan.factory.ApiFactory;

import io.swagger.annotations.ApiResponse;
import io.swagger.annotations.ApiResponses;

@CrossOrigin
@RestController
public class ApiController {

	@Autowired
	private ApiFactory apiFactory;

	@ApiResponses(value = { @ApiResponse(code = 200, message = "Pesquisa realizada com sucesso!"),
			@ApiResponse(code = 400, message = "Número do valor pedido igual ou menor a zero OU número do pedido superior ao permitido"),
			@ApiResponse(code = 404, message = "Usuário não encontrado"),
			@ApiResponse(code = 500, message = "Foi gerada uma exceção"), })
	@GetMapping(path = "api/credito/{nome}/{valorPedido}")
	public @ResponseBody ResponseEntity<ApiEntity> calcularValores(@PathVariable(value = "nome") String nome,
			@PathVariable(value = "valorPedido") Double valorPedido) {
		return apiFactory.calcularValores(nome, valorPedido);
	}

}
