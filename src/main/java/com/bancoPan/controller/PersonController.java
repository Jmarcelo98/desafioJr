package com.bancoPan.controller;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RestController;

import com.bancoPan.factory.PersonFactory;

@CrossOrigin
@RestController
public class PersonController {

	@Autowired
	private PersonFactory personFactory;

	@GetMapping
	public Object buscarPessoaCadastrada(String nome, Double valorCreditado) {
		return personFactory.buscarPessoaCadastrada(nome, valorCreditado);
	}

}
