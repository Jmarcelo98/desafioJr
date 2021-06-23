package com.bancoPan.service;

import java.lang.reflect.Type;
import java.util.ArrayList;

import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.bancoPan.entity.ApiEntity;
import com.bancoPan.entity.PersonEntity;
import com.bancoPan.factory.ApiFactory;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

@Service
public class ApiService implements ApiFactory {

//	PORCENTAGENS DE ACORDO COM AS IDADES
	private final Double IDADE_MAIOR_80 = 0.20;
	private final Double IDADE_MAIOR_50 = 0.70;
	private final Double IDADE_MAIOR_30 = 0.90;
	private final Double IDADE_MAIOR_20 = 1.0;

//	PORCENTAGEM DE ACORCO COM AS FAIXAS SALARIAS;
	private final Double DE_1K_A_2K = 0.05;
	private final Double DE_2K_A_3K = 0.10;
	private final Double DE_3K_A_4K = 0.15;
	private final Double DE_4K_A_5K = 0.20;
	private final Double DE_5K_A_6K = 0.25;
	private final Double DE_6K_A_7K = 0.30;
	private final Double DE_7K_A_8K = 0.35;
	private final Double DE_8K_A_9K = 0.40;
	private final Double MAIOR_QUE_9K = 0.45;

	ArrayList<PersonEntity> personArray;

	@EventListener(ContextRefreshedEvent.class)
	public void buscarPessoasCadastradas() {

		Gson gson = new Gson();
		RestTemplate template = new RestTemplate();
		String urlApi = "http://www.mocky.io/v2/5e2b3b8d32000054001c7109";

		ResponseEntity<String> buscarJson = template.getForEntity(urlApi, String.class);

		String json = buscarJson.getBody();

		Type userListType = new TypeToken<ArrayList<PersonEntity>>() {
		}.getType();

		personArray = gson.fromJson(json, userListType);
	}

	@Override
	public ResponseEntity<ApiEntity> calcularValores(String nome, Double valorPedido) {

		ApiEntity apiEntity = new ApiEntity();

		if (valorPedido <= 0) {
			return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
		}

		Double creditoAReceber = null;
		Double valorParcela = null;
		Integer quantidadeDeParcelas = null;

		for (PersonEntity personEntity : personArray) {

			if (personEntity.getNome().equalsIgnoreCase(nome)) {

				apiEntity.setNome(personEntity.getNome());
				apiEntity.setValorPedido(valorPedido);
				apiEntity.setSalario(personEntity.getSalario());

				creditoAReceber = valorCreditoAReceber(personEntity.getIdade(), personEntity.getSalario());

				valorParcela = valorParcela(apiEntity.getSalario());
				
				quantidadeDeParcelas = quantidadeDeParcelas(creditoAReceber, valorParcela);

				if (valorPedido <= creditoAReceber) {
					apiEntity.setValorEmprestado(valorPedido);
				} else {
					apiEntity.setValorEmprestado(creditoAReceber);
				}
				
				apiEntity.setValorParcela(valorParcela);
				apiEntity.setQuantidadeParcelas(quantidadeDeParcelas);
			}
		}

		if (apiEntity.getNome() == null) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}

		return new ResponseEntity<>(apiEntity, HttpStatus.OK);
	}

	public Double valorCreditoAReceber(Integer idade, Double valor) {

		Double creditoAReceber = null;

		if (idade >= 80) {

			creditoAReceber = valor * IDADE_MAIOR_80;

		} else if (idade >= 50) {

			creditoAReceber = valor * IDADE_MAIOR_50;

		} else if (idade >= 30) {

			creditoAReceber = valor * IDADE_MAIOR_30;

		} else if (idade >= 20) {

			creditoAReceber = valor * IDADE_MAIOR_20;

		}

		return creditoAReceber;

	}

	public Double valorParcela(Double salario) {

		Double valorParcela = null;

		if (salario >= 1000 && salario <= 2000) {
			valorParcela = salario * DE_1K_A_2K;
		} else if (salario >= 2001 && salario <= 3000) {
			valorParcela = salario * DE_2K_A_3K;
		} else if (salario >= 3001 && salario <= 4000) {
			valorParcela = salario * DE_3K_A_4K;
		} else if (salario >= 4001 && salario <= 5000) {
			valorParcela = salario * DE_4K_A_5K;
		} else if (salario >= 5001 && salario <= 6000) {
			valorParcela = salario * DE_5K_A_6K;
		} else if (salario >= 6001 && salario <= 7000) {
			valorParcela = salario * DE_6K_A_7K;
		} else if (salario >= 7001 && salario <= 8000) {
			valorParcela = salario * DE_7K_A_8K;
		} else if (salario >= 8001 && salario <= 9000) {
			valorParcela = salario * DE_8K_A_9K;
		} else if (salario >= 9001) {
			valorParcela = salario * MAIOR_QUE_9K;

		}

		return valorParcela;

	}
	
	public Integer quantidadeDeParcelas(Double valorEmprestado, Double valorDaParcela) {
		Integer quantidadeParcelas = null;
		
		quantidadeParcelas = (int) (valorEmprestado / valorDaParcela ) ;
		
		return quantidadeParcelas;
	}

}
