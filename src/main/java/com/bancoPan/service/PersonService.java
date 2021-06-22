package com.bancoPan.service;

import javax.validation.Valid;

import org.springframework.stereotype.Service;

import com.bancoPan.factory.PersonFactory;

@Service
public class PersonService implements PersonFactory {

	@Override
	public Object buscarPessoaCadastrada(@Valid String nome, @Valid Double valorCreditado) {

		if (valorCreditado <= 0) {
			return null;
		}

		return (nome + " " + valorCreditado);
	}

}
