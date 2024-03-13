package br.com.app.conatus.service;

import java.util.List;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import br.com.app.conatus.entities.DominioEntity;
import br.com.app.conatus.entities.PessoaFisicaEntity;
import br.com.app.conatus.entities.UsuarioEntity;
import br.com.app.conatus.entities.factory.PessoaFisicaEntityFactory;
import br.com.app.conatus.entities.factory.UsuarioEntityFactory;
import br.com.app.conatus.enums.CodigoDominio;
import br.com.app.conatus.exceptions.MsgException;
import br.com.app.conatus.exceptions.NaoEncontradoException;
import br.com.app.conatus.model.CadastroUsuarioVO;
import br.com.app.conatus.model.UsuarioAutenticadoVO;
import br.com.app.conatus.repositories.UsuarioRepository;
import jakarta.transaction.Transactional;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UsuarioService implements UserDetailsService {
	
	private final UsuarioRepository usuarioRepository;
	
	private final DominioService dominioService;
	
	private final UsuarioGrupoUsuarioService usuarioGrupoUsuarioService;
	
	private final PessoaFisicaService pessoaFisicaService;
	
	
	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

		UsuarioEntity usuario = usuarioRepository.findByUsername(username, CodigoDominio.STATUS_ATIVO.name())
				.orElseThrow(() -> new NaoEncontradoException(String.format("Usuário %s não encontrado.", username)));
		
		return UsuarioAutenticadoVO.builder()
				.idUsuario(usuario.getId())
				.email(username)
				.senha(usuario.getSenha())
				.situacao(usuario.getSituacao().getCodigo())
				.codigoAutorizacoes(List.of())
				.build();
	}
	
	public UserDetails loadUserByUsernameWithAuthority(String username) throws UsernameNotFoundException {

		UsuarioAutenticadoVO usuario = (UsuarioAutenticadoVO) this.loadUserByUsername(username);
		
		List<String> autorizacoes = usuarioRepository.findAutorizacoesByUsuarioId(usuario.getIdUsuario()); 
		
		usuario.setCodigoAutorizacoes(autorizacoes);
		
		return usuario;
	}

	@Transactional
	public void salvarUsuario(CadastroUsuarioVO dadosUsuario) {
		
		if(usuarioRepository.existsByPessoaEmail(dadosUsuario.getEmail())) {
			throw new MsgException("E-mail já está sendo utilizado por outro usuário.");
		}
		
		DominioEntity situacao = dominioService.recuperarPorCodigo(CodigoDominio.STATUS_ATIVO);
		
		PessoaFisicaEntity pessoaFisica = pessoaFisicaService.salvarPessoaFisica(PessoaFisicaEntityFactory.converterParaEntity(dadosUsuario, situacao));
		
		UsuarioEntity usuario = usuarioRepository.save(UsuarioEntityFactory.converterParaEntity(dadosUsuario, pessoaFisica, situacao));
		
		usuarioGrupoUsuarioService.vincularGrupoBasicoNoUsuario(usuario);
		
	}
	
	protected boolean isUsuarioGoogleExistente(String idGoogle) {
		return usuarioRepository.existsByIdGoogle(idGoogle);
	}

}
