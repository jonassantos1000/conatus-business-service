package br.com.app.conatus.service;

import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.oauth2.core.oidc.user.DefaultOidcUser;
import org.springframework.stereotype.Service;

import br.com.app.conatus.model.AuthenticationRequest;
import br.com.app.conatus.model.AuthenticationResponse;
import br.com.app.conatus.model.CadastroUsuarioVO;
import br.com.app.conatus.model.UsuarioAutenticadoVO;
import br.com.app.conatus.model.factory.CadastroUsuarioVOFactory;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class AutenticacaoService {
	
	private final PasswordEncoder passwordEncoder;
	
	private final UsuarioService usuarioService;
	
	private final AuthenticationManager authenticationManager;
	
	private final TokenService tokenService;

	
	public void salvarUsuario(CadastroUsuarioVO dadosUsuario) {
		
		dadosUsuario.setSenha(passwordEncoder.encode(dadosUsuario.getSenha()));
		
		usuarioService.salvarUsuario(dadosUsuario);		
	}


	public AuthenticationResponse autenticarUsuario(AuthenticationRequest authRequest) {
		
		UsernamePasswordAuthenticationToken usernamePassword = new UsernamePasswordAuthenticationToken(
				authRequest.email(), authRequest.senha());
		
		var auth = this.authenticationManager.authenticate(usernamePassword);
		
		var token = tokenService.generateToken(((UsuarioAutenticadoVO) auth.getPrincipal()).getEmail());
		
		return new AuthenticationResponse(token);
	}


	public String autenticarUsuario(DefaultOidcUser usuario) {
		
		String token = tokenService.generateToken(usuario.getEmail());
		
		if (!usuarioService.isUsuarioGoogleExistente(usuario.getSubject())) {
			
			CadastroUsuarioVO dadosUsuario = CadastroUsuarioVOFactory.converterParaVO(usuario);
			
			usuarioService.salvarUsuario(dadosUsuario);
		}
		
		return token;
	}
	

}
