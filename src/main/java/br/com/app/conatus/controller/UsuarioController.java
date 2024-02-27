package br.com.app.conatus.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.oidc.user.DefaultOidcUser;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PostMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@RestController
@RequiredArgsConstructor
@RequestMapping("/auth")
public class UsuarioController {

	
	@Value("${conatus.security.oauth2.callback.url}")
	private String URL_BASE;

	@GetMapping("/login/google")
	public void integrarLoginGoogle(OAuth2AuthenticationToken oAuth2AuthenticationToken, HttpServletRequest request, HttpServletResponse response) throws Exception {
		
		String idToken = ((DefaultOidcUser) oAuth2AuthenticationToken.getPrincipal()).getIdToken().getTokenValue();

		response.sendRedirect(URL_BASE.concat("/login").concat("?token=").concat(idToken));
	}
	
	@PostMapping("/login/google2")
	public void integrarLoginGoogle() {
		
	}
	
}
