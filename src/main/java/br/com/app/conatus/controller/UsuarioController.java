package br.com.app.conatus.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.http.ResponseEntity;
import org.springframework.security.access.prepost.PreAuthorize;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.oidc.user.DefaultOidcUser;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.app.conatus.entities.UsuarioEntity;
import br.com.app.conatus.model.AuthenticationRequest;
import br.com.app.conatus.model.AuthenticationResponse;
import br.com.app.conatus.model.RegisterRequest;
import br.com.app.conatus.service.AutenticacaoService;
import br.com.app.conatus.service.TokenService;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import jakarta.validation.Valid;
import lombok.RequiredArgsConstructor;

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
		
		System.out.println("chamei");
		
		UsernamePasswordAuthenticationToken usernamePassword = new UsernamePasswordAuthenticationToken(
				authRequest.username(), authRequest.password());
		
		var auth = this.authenticationManager.authenticate(usernamePassword);
		
		var token = tokenService.generateToken((UsuarioEntity) auth.getPrincipal());
		return ResponseEntity.ok(new AuthenticationResponse(token));
	}
	
	@PostMapping("/register")
	public ResponseEntity<Void> register(@RequestBody @Valid RegisterRequest data){
		
		autenticacaoService.salvarUsuario(data);
		
		return ResponseEntity.ok().build();
	}
	
	@GetMapping("/teste")
	@PreAuthorize("hasAuthority('X1232')")
	public ResponseEntity<Void> register(){

		return ResponseEntity.ok().build();
	}
	
}
