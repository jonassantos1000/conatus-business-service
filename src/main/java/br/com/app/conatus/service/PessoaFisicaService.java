package br.com.app.conatus.service;

import org.springframework.stereotype.Service;

import br.com.app.conatus.entities.PessoaFisicaEntity;
import br.com.app.conatus.repositories.PessoaFisicaRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class PessoaFisicaService {

	private final PessoaFisicaRepository pessoaFisicaRepository;
	
	
	protected PessoaFisicaEntity salvarPessoaFisica(PessoaFisicaEntity pessoa) {
		
		return pessoaFisicaRepository.save(pessoa);
	}
	
}
