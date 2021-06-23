package com.bancoPan.factory;

import org.springframework.http.ResponseEntity;

import com.bancoPan.entity.ApiEntity;

public interface ApiFactory {
	
//	Object[] listaPessoasCadastradas();
	
	ResponseEntity<ApiEntity> calcularValores(String nome, Double valorPedido);
	
}
