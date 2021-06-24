package com.bancoPan.service;

import java.lang.reflect.Type;
import java.util.ArrayList;

import org.apache.commons.math3.util.Precision;
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

		Double creditoMaximoPermitido= null;
		
		Double valorParcela = null;
		Integer quantidadeDeParcelas = null;

		for (PersonEntity personEntity : personArray) {

			if (personEntity.getNome().equalsIgnoreCase(nome)) {

				creditoMaximoPermitido = valorCreditoMaximo(personEntity.getIdade(), personEntity.getSalario());
				Double valorPedidoDuasCasasDecimais = valorComDuasCasas(valorPedido);
				
				if (valorPedidoDuasCasasDecimais > creditoMaximoPermitido) {
					return new ResponseEntity<>(HttpStatus.BAD_REQUEST);
				} else {	
					apiEntity.setValorPedido(valorPedidoDuasCasasDecimais);
					apiEntity.setValorEmprestado(valorPedidoDuasCasasDecimais);
				}
				
				apiEntity.setNome(personEntity.getNome());			
			
				Double salarioDuasCasasDecimais = valorComDuasCasas(personEntity.getSalario());
				apiEntity.setSalario(salarioDuasCasasDecimais);			

				valorParcela = valorParcela(apiEntity.getValorEmprestado(), apiEntity.getSalario());

				quantidadeDeParcelas = quantidadeDeParcelas(apiEntity.getValorEmprestado(), valorParcela);

				apiEntity.setValorParcela(valorParcela);
				apiEntity.setQuantidadeParcelas(quantidadeDeParcelas);
			}
		}

		if (apiEntity.getNome() == null) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}

		return new ResponseEntity<>(apiEntity, HttpStatus.OK);
	}

	public Double valorCreditoMaximo(Integer idade, Double valor) {

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

		Double valorArredondado = valorComDuasCasas(creditoAReceber);

		return valorArredondado;

	}

	public Double valorParcela(Double valorCreditoEmprestado, Double salario) {

		Double valorParcela = null;

		if (valorCreditoEmprestado >= 1000 && valorCreditoEmprestado <= 2000) {
			valorParcela = salario * DE_1K_A_2K;
		} else if (valorCreditoEmprestado >= 2001 && valorCreditoEmprestado <= 3000) {
			valorParcela = salario * DE_2K_A_3K;
		} else if (valorCreditoEmprestado >= 3001 && valorCreditoEmprestado <= 4000) {
			valorParcela = salario * DE_3K_A_4K;
		} else if (valorCreditoEmprestado >= 4001 && valorCreditoEmprestado <= 5000) {
			valorParcela = salario * DE_4K_A_5K;
		} else if (valorCreditoEmprestado >= 5001 && valorCreditoEmprestado <= 6000) {
			valorParcela = salario * DE_5K_A_6K;
		} else if (valorCreditoEmprestado >= 6001 && valorCreditoEmprestado <= 7000) {
			valorParcela = salario * DE_6K_A_7K;
		} else if (valorCreditoEmprestado >= 7001 && valorCreditoEmprestado <= 8000) {
			valorParcela = salario * DE_7K_A_8K;
		} else if (valorCreditoEmprestado >= 8001 && valorCreditoEmprestado <= 9000) {
			valorParcela = salario * DE_8K_A_9K;
		} else if (valorCreditoEmprestado >= 9001) {
			valorParcela = salario * MAIOR_QUE_9K;
		}
		Double valorArredondado = valorComDuasCasas(valorParcela);

		return valorArredondado;

	}

	public Integer quantidadeDeParcelas(Double valorCreditoEmprestado, Double valorParcela) {

		Double quantidadeParcelas = (valorCreditoEmprestado / valorParcela);

		double arredondar = arredondarPraCima(quantidadeParcelas);
		
		return (int) arredondar;
	}
	
	public double arredondarPraCima(Double valor) {	
		return Math.ceil(valor);	
	}

	public Double valorComDuasCasas(Double valor) {
		return Precision.round(valor, 2);
	}

}