package br.com.app.conatus.configuration;

import java.io.IOException;

import org.hibernate.Hibernate;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.nimbusds.oauth2.sdk.util.StringUtils;

import br.com.app.conatus.enums.CodigoDominio;
import br.com.app.conatus.repositories.UsuarioRepository;
import br.com.app.conatus.service.TokenService;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class SecurityFilter extends OncePerRequestFilter {
	
	private final TokenService tokenService;
	
	private final UsuarioRepository usuarioRepository;

	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
		String token = this.recoverToken(request);
				
		if(token != null) {
			
			String subject = tokenService.validateToken(token);
			
			UserDetails user = usuarioRepository.findByUsername(subject, CodigoDominio.STATUS_ATIVO.name());
						
			var authentication = new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());
			
			SecurityContextHolder.getContext().setAuthentication(authentication);
		}
		
		filterChain.doFilter(request, response);

	}
	
	private String recoverToken(HttpServletRequest request) {
		String authHeader = request.getHeader("Authorization");
		
		if(StringUtils.isBlank(authHeader)) {
			return authHeader;
		}
		
		return authHeader.replace("Bearer ", "");
	}

}
