package com.bancoPan.service;

import java.lang.reflect.Type;
import java.util.ArrayList;

import org.springframework.context.event.ContextRefreshedEvent;
import org.springframework.context.event.EventListener;
import org.springframework.http.ResponseEntity;
import org.springframework.stereotype.Service;
import org.springframework.web.client.RestTemplate;

import com.bancoPan.entity.PersonEntity;
import com.bancoPan.factory.ApiFactory;
import com.google.gson.Gson;
import com.google.gson.reflect.TypeToken;

@Service
public class ApiService implements ApiFactory {

	ArrayList<PersonEntity> personArray;

	@EventListener(ContextRefreshedEvent.class)
	public void f() {

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
	public String calcularValores(String nome, Double valorPedido) {

		if (valorPedido <= 0) {
			return "Error: Valor pedido igual ou inferior a 0";
		}

		Double creditoAReceber;
		Integer quantidadeDeParcelas;

		for (PersonEntity personEntity : personArray) {
			
			

			if (personEntity.getNome().equals(nome)) {

				System.out.println(personEntity.getIdade());
				System.out.println(personEntity.getSalario());
				
				if (personEntity.getIdade() > 80) {

					creditoAReceber = 0.20 * personEntity.getSalario();
					
				} else if (personEntity.getIdade() > 50) {

					creditoAReceber = 0.70 * personEntity.getSalario();
					
				} else if (personEntity.getIdade() > 30) {

					creditoAReceber = 0.90 * personEntity.getSalario();
					
				} else if (personEntity.getIdade() > 20) {

					creditoAReceber = 1 * personEntity.getSalario();
					
				}

			}

		}

		return null;
	}

}
