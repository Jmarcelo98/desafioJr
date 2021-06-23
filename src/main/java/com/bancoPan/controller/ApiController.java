package com.bancoPan.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.ResponseBody;
import org.springframework.web.bind.annotation.RestController;

import com.bancoPan.factory.ApiFactory;

@CrossOrigin
@RestController
public class ApiController {

	@Autowired
	private ApiFactory apiFactory;

//	@GetMapping(path = "api/credito/buscarPessoasNaApi")
//	public Object[] listaPessoasCadastradas() {
//		return apiFactory.listaPessoasCadastradas();
//	}

	@GetMapping(path = "api/credito/{nome}/{valorPedido}")
	public @ResponseBody String calcularValores(@PathVariable(value = "nome") String nome,
			@PathVariable(value = "valorPedido") Double valorPedido) {
		return apiFactory.calcularValores(nome, valorPedido);
	}

}
