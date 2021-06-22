package com.bancoPan.factory;

import javax.validation.Valid;

public interface PersonFactory {

	Object buscarPessoaCadastrada(@Valid String nome, @Valid Double valorCreditado);

}
