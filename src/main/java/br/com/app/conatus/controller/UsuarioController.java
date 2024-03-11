package br.com.app.conatus.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.annotation.Secured;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.oidc.user.DefaultOidcUser;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.app.conatus.constante.Autorizacao;
import br.com.app.conatus.model.AuthenticationRequest;
import br.com.app.conatus.model.AuthenticationResponse;
import br.com.app.conatus.model.CadastroUsuarioVO;
import br.com.app.conatus.model.UsuarioAutenticadoVO;
import br.com.app.conatus.service.AutenticacaoService;
import br.com.app.conatus.service.TokenService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

@CrossOrigin
@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
public class UsuarioController {

	@Value("${conatus.security.oauth2.callback.url}")
	private String URL_BASE;
	
	private final AuthenticationManager authenticationManager;
	
	private final AutenticacaoService autenticacaoService;
	
	private final TokenService tokenService;
		

	@GetMapping("/login/google")
	public void integrarLoginGoogle(OAuth2AuthenticationToken oAuth2AuthenticationToken, HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		String idToken = ((DefaultOidcUser) oAuth2AuthenticationToken.getPrincipal()).getIdToken().getTokenValue();

		response.sendRedirect(URL_BASE.concat("/login").concat("?token=").concat(idToken));
	}
	
	@PostMapping("/login/token")
	public ResponseEntity<AuthenticationResponse> login(@RequestBody @Valid AuthenticationRequest authRequest) {
		
		UsernamePasswordAuthenticationToken usernamePassword = new UsernamePasswordAuthenticationToken(
				authRequest.email(), authRequest.senha());
		
		var auth = this.authenticationManager.authenticate(usernamePassword);
		
		var token = tokenService.generateToken(((UsuarioAutenticadoVO) auth.getPrincipal()).getEmail());
		
		return ResponseEntity.ok(new AuthenticationResponse(token));
	}
	
	@PostMapping("/register")
	public void registrarUsuario(@RequestBody @Valid CadastroUsuarioVO data){
		
		autenticacaoService.salvarUsuario(data);
	}
	
	@GetMapping("/teste")
	@Secured({ Autorizacao.B1, Autorizacao.B2 })
	public ResponseEntity<Void> register(){

		return ResponseEntity.ok().build();
	}
	
}
