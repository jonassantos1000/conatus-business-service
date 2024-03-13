package br.com.app.conatus.controller;

import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.RequestHeader;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RestController;

import br.com.app.conatus.service.TokenService;
import br.com.app.conatus.service.UsuarioService;
import lombok.RequiredArgsConstructor;

@CrossOrigin
@RestController
@RequiredArgsConstructor
@RequestMapping("/usuarios")
public class UsuarioController {
	
	
	private final UsuarioService usuarioService;
	
	private final TokenService tokenService;
	
	@GetMapping
	public void buscarUsuario(@RequestHeader ("Authorization") String token) {
		
		System.out.println("########## token: " + token);
		
		UserDetails loadUserByUsername = usuarioService.loadUserByUsername(tokenService.validateToken(token));
		
		System.out.println(loadUserByUsername.getUsername());
	}

}
