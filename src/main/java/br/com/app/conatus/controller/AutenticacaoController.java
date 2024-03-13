package br.com.app.conatus.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.oidc.user.DefaultOidcUser;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.app.conatus.model.AuthenticationRequest;
import br.com.app.conatus.model.AuthenticationResponse;
import br.com.app.conatus.model.CadastroUsuarioVO;
import br.com.app.conatus.service.AutenticacaoService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@CrossOrigin
@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
public class AutenticacaoController {

	@Value("${conatus.security.oauth2.callback.url}")
	private String URL_BASE;
	
	private final AutenticacaoService autenticacaoService;
	

	@GetMapping("/login/google")
	public void integrarLoginGoogle(OAuth2AuthenticationToken oAuth2AuthenticationToken, HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		DefaultOidcUser usuario = (DefaultOidcUser) oAuth2AuthenticationToken.getPrincipal();

		String token = autenticacaoService.autenticarUsuario(usuario);

		System.out.println(token);
		
		response.sendRedirect(URL_BASE.concat("/login").concat("?token=").concat(token));
	}
	
	@PostMapping("/login/token")
	public AuthenticationResponse login(@RequestBody @Valid AuthenticationRequest authRequest) {
		
		return autenticacaoService.autenticarUsuario(authRequest);
	}
	
	@PostMapping("/register")
	public void registrarUsuario(@RequestBody @Valid CadastroUsuarioVO data){
		
		autenticacaoService.salvarUsuario(data);
	}
	
}
