package br.com.app.conatus.exceptions;

import org.springframework.validation.FieldError;

public record DetalheErroResponse(String mensagem, String detalhe) {
	public DetalheErroResponse(FieldError erro) { 
		this(erro.getField(), erro.getDefaultMessage());
	}
}
