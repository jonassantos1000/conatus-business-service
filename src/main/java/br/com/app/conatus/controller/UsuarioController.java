package br.com.app.conatus.controller;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.security.oauth2.client.OAuth2AuthorizedClientService;
import org.springframework.security.oauth2.client.authentication.OAuth2AuthenticationToken;
import org.springframework.security.oauth2.core.oidc.user.DefaultOidcUser;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@RestController
@RequestMapping("/")
@RequiredArgsConstructor
public class UsuarioController {

	private final OAuth2AuthorizedClientService clientService;
	
	@Value("${conatus.oauth2.callback.url}")
	private String URL_BASE;

	@GetMapping()
	public void integrarLoginGoogle(OAuth2AuthenticationToken oAuth2AuthenticationToken, HttpServletResponse response) throws Exception {
		
		String idToken = ((DefaultOidcUser) oAuth2AuthenticationToken.getPrincipal()).getIdToken().getTokenValue();
		
		response.addHeader("Authorization", "Bearer ".concat(idToken));
		response.sendRedirect(URL_BASE.concat("/login"));
	}
	
}