package br.com.app.conatus.configuration;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;

import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@Configuration
@EnableWebSecurity
@RequiredArgsConstructor
public class SecurityConfig implements AuthenticationFailureHandler {
	
	@Value(value = "${conatus.oauth2.callback.url}")
	private String URL_HOME;
	
    @Bean
    public SecurityFilterChain filterChain(HttpSecurity http) throws Exception {
    	http
    		.authorizeRequests()
        	.anyRequest()
        	.authenticated()
        .and()
        	.logout()
        	.invalidateHttpSession(true)
        	.clearAuthentication(true)
        	.logoutSuccessUrl(URL_HOME)
        	.deleteCookies("JSESSIONID")
        	.permitAll()
        .and()
        	.oauth2Login()
        	.failureHandler(authenticationFailureHandler())
        .and()
        	.csrf()
        	.disable();
        
        return http.build();
    }

    @Bean
    public AuthenticationFailureHandler authenticationFailureHandler() {
        return this;
    }
    
	@Override
	public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException, ServletException {
	
		response.sendRedirect(URL_HOME);
	}
	

}
