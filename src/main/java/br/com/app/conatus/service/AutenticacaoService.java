package br.com.app.conatus.service;

import java.time.ZonedDateTime;

import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.stereotype.Service;

import br.com.app.conatus.entities.DominioEntity;
import br.com.app.conatus.entities.PessoaFisicaEntity;
import br.com.app.conatus.entities.UsuarioEntity;
import br.com.app.conatus.entities.UsuarioGrupoUsuario;
import br.com.app.conatus.enums.TipoPessoaEnum;
import br.com.app.conatus.exceptions.MsgException;
import br.com.app.conatus.model.RegisterRequest;
import br.com.app.conatus.repositories.PessoaRepository;
import br.com.app.conatus.repositories.UsuarioGrupoUsuarioRepository;
import br.com.app.conatus.repositories.UsuarioRepository;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AutenticacaoService {
	
	private final UsuarioRepository usuarioRepository;
	
	private final PessoaRepository pessoaRepository;
	
	private final UsuarioGrupoUsuarioRepository usuarioGrupoUsuarioRepository;

	public void salvarUsuario(@Valid RegisterRequest data) {
		if(usuarioRepository.existsByPessoaEmail(data.username())) {
			throw new MsgException("Username já esta em uso.");
		}
		
		String encryptedPassword = new BCryptPasswordEncoder().encode(data.password());
		
		UsuarioEntity newUser = UsuarioEntity.builder()
				.pessoa(pessoaRepository.save(PessoaFisicaEntity.builder()
						.id(1L)
						.email(data.email())
						.cpf("12345645")
						.tipoPessoa(TipoPessoaEnum.PESSOA_FISICA)
						.situacao(DominioEntity.builder().id(1L).build())
						.telefone("123456")
						.dataAtualizacao(ZonedDateTime.now())
						.nome(data.name())
						.build()))
				.senha(encryptedPassword)
				.situacao(DominioEntity.builder().id(1L).build())
				.build();

		usuarioRepository.save(newUser);
				
		usuarioGrupoUsuarioRepository.save(UsuarioGrupoUsuario.builder().idGrupoUsuario(1L).idUsuario(1L).build());
		
	}

}
