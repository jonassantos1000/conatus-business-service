package br.com.app.conatus.configuration;

import java.io.IOException;
import java.time.Instant;

import org.springframework.http.HttpStatus;
import org.springframework.security.authentication.UsernamePasswordAuthenticationToken;
import org.springframework.security.core.context.SecurityContextHolder;
import org.springframework.security.core.userdetails.UserDetails;
import org.springframework.stereotype.Component;
import org.springframework.web.filter.OncePerRequestFilter;

import com.fasterxml.jackson.databind.ObjectMapper;
import com.fasterxml.jackson.databind.json.JsonMapper;

import br.com.app.conatus.exceptions.DetalheErroResponse;
import br.com.app.conatus.exceptions.ErroResponse;
import br.com.app.conatus.exceptions.MsgException;
import br.com.app.conatus.exceptions.NaoEncontradoException;
import br.com.app.conatus.service.TokenService;
import br.com.app.conatus.service.UsuarioService;
import br.com.app.conatus.util.TokenUtil;
import jakarta.servlet.FilterChain;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@Component
@RequiredArgsConstructor
public class SecurityFilter extends OncePerRequestFilter {
	
	private final TokenService tokenService;
	
	private final UsuarioService usuarioService;


	@Override
	protected void doFilterInternal(HttpServletRequest request, HttpServletResponse response, FilterChain filterChain) throws ServletException, IOException {
		
		try {
		
			String token = this.recoverToken(request);
					
			if(token != null) {
				
				String subject = tokenService.validateToken(token);
				
				UserDetails user = usuarioService.loadUserByUsernameWithAuthority(subject);
							
				var authentication = new UsernamePasswordAuthenticationToken(user, null, user.getAuthorities());
				
				SecurityContextHolder.getContext().setAuthentication(authentication);
			}
			
			filterChain.doFilter(request, response);
		} catch (MsgException e) {
            setErrorResponse(HttpStatus.BAD_REQUEST, request, response, e);
		} catch (NaoEncontradoException e) {
            setErrorResponse(HttpStatus.NOT_FOUND, request, response, e);
        } catch (RuntimeException e) {
            setErrorResponse(HttpStatus.INTERNAL_SERVER_ERROR, request, response, e);
        }

	}
	
	private String recoverToken(HttpServletRequest request) {
		
		return TokenUtil.replaceBearer(request.getHeader("Authorization"));
	}
	
    private void setErrorResponse(HttpStatus status, HttpServletRequest request, HttpServletResponse response, Throwable ex){
        
    	response.setStatus(status.value());
        
        response.setContentType("application/json");
        
        ErroResponse apiError = new ErroResponse(Instant.now(), new DetalheErroResponse("Erro na requisição", ex.getMessage()), request.getRequestURI());
        
        ObjectMapper objectMapper= JsonMapper.builder().findAndAddModules().build();
        
        try {
            objectMapper.writeValue(response.getWriter(), apiError);
        } catch (IOException e) {
            e.printStackTrace();
        }
    }
      

}
