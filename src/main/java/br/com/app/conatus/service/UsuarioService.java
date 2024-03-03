package br.com.app.conatus.service;

import java.util.List;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.security.core.userdetails.UserDetailsService;
import org.springframework.security.core.userdetails.UsernameNotFoundException;
import org.springframework.stereotype.Service;

import br.com.app.conatus.entities.UsuarioEntity;
import br.com.app.conatus.enums.CodigoDominio;
import br.com.app.conatus.exceptions.NaoEncontradoException;
import br.com.app.conatus.model.UsuarioAutenticadoVO;
import br.com.app.conatus.repositories.UsuarioRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UsuarioService implements UserDetailsService {
	
	private final UsuarioRepository usuarioRepository;

	@Override
	public UserDetails loadUserByUsername(String username) throws UsernameNotFoundException {

		UsuarioEntity usuario = usuarioRepository.findByUsername(username, CodigoDominio.STATUS_ATIVO.name())
				.orElseThrow(() -> new NaoEncontradoException(String.format("Usuário %s não encontrado.", username)));
		
		return UsuarioAutenticadoVO.builder()
				.idUsuario(usuario.getId())
				.username(username)
				.password(usuario.getSenha())
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

}
