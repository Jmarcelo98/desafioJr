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
		Integer quantidadeDeParcelas = null;

		for (PersonEntity personEntity : personArray) {

			if (personEntity.getNome().equalsIgnoreCase(nome)) {

				apiEntity.setNome(personEntity.getNome());
				apiEntity.setValorPedido(valorPedido);
				apiEntity.setSalario(personEntity.getSalario());

				if (personEntity.getIdade() >= 80) {

					creditoAReceber = 0.20 * personEntity.getSalario();

				} else if (personEntity.getIdade() >= 50) {

					creditoAReceber = 0.70 * personEntity.getSalario();

				} else if (personEntity.getIdade() >= 30) {

					creditoAReceber = 0.90 * personEntity.getSalario();

				} else if (personEntity.getIdade() >= 20) {

					creditoAReceber = 1 * personEntity.getSalario();

				}

				apiEntity.setValorEmprestado(creditoAReceber);

			} 

		}
		
		 if (apiEntity.getNome() == null) {
			return new ResponseEntity<>(HttpStatus.NOT_FOUND);
		}

		return new ResponseEntity<>(apiEntity, HttpStatus.OK);
	}

}
