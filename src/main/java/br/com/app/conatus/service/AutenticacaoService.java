package br.com.app.conatus.service;

import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.stereotype.Service;

import br.com.app.conatus.entities.DominioEntity;
import br.com.app.conatus.entities.PessoaFisicaEntity;
import br.com.app.conatus.entities.UsuarioEntity;
import br.com.app.conatus.entities.factory.PessoaFisicaEntityFactory;
import br.com.app.conatus.entities.factory.UsuarioEntityFactory;
import br.com.app.conatus.enums.CodigoDominio;
import br.com.app.conatus.exceptions.MsgException;
import br.com.app.conatus.model.CadastroUsuarioVO;
import br.com.app.conatus.repositories.UsuarioRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AutenticacaoService {
	
	private final UsuarioRepository usuarioRepository;
	
	private final DominioService dominioService;
	
	private final UsuarioGrupoUsuarioService usuarioGrupoUsuarioService;
	
	private final PessoaFisicaService pessoaFisicaService;
	
	private final PasswordEncoder passwordEncoder;
	
	public void salvarUsuario(CadastroUsuarioVO dadosUsuario) {
		
		if(usuarioRepository.existsByPessoaEmail(dadosUsuario.getEmail())) {
			throw new MsgException("E-mail já está sendo utilizado por outro usuário.");
		}
		
		String senhaCriptografada = passwordEncoder.encode(dadosUsuario.getSenha());
		
		DominioEntity situacao = dominioService.recuperarPorCodigo(CodigoDominio.STATUS_ATIVO);
		
		PessoaFisicaEntity pessoaFisica = pessoaFisicaService.salvarPessoaFisica(PessoaFisicaEntityFactory.converterParaEntity(dadosUsuario, situacao));
		
		UsuarioEntity usuario = usuarioRepository.save(UsuarioEntityFactory.converterParaEntity(dadosUsuario, pessoaFisica, situacao, senhaCriptografada));
		
		usuarioGrupoUsuarioService.vincularGrupoBasicoNoUsuario(usuario);
		
	}

}
