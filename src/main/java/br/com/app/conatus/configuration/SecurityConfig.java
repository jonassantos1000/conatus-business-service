package br.com.app.conatus.configuration;

import java.io.IOException;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.context.annotation.Bean;
import org.springframework.context.annotation.Configuration;
import org.springframework.core.Ordered;
import org.springframework.core.annotation.Order;
import org.springframework.http.HttpMethod;
import org.springframework.security.authentication.AuthenticationManager;
import org.springframework.security.config.annotation.authentication.configuration.AuthenticationConfiguration;
import org.springframework.security.config.annotation.method.configuration.EnableMethodSecurity;
import org.springframework.security.config.annotation.web.builders.HttpSecurity;
import org.springframework.security.config.annotation.web.configuration.EnableWebSecurity;
import org.springframework.security.config.annotation.web.configurers.AbstractHttpConfigurer;
import org.springframework.security.config.http.SessionCreationPolicy;
import org.springframework.security.core.AuthenticationException;
import org.springframework.security.crypto.bcrypt.BCryptPasswordEncoder;
import org.springframework.security.crypto.password.PasswordEncoder;
import org.springframework.security.web.SecurityFilterChain;
import org.springframework.security.web.authentication.AuthenticationFailureHandler;
import org.springframework.security.web.authentication.UsernamePasswordAuthenticationFilter;
import org.springframework.security.web.util.matcher.RequestMatcher;

import io.micrometer.common.util.StringUtils;
import jakarta.servlet.ServletException;
import jakarta.servlet.http.HttpServletRequest;
import jakarta.servlet.http.HttpServletResponse;
import lombok.RequiredArgsConstructor;

@Configuration
@EnableWebSecurity
@EnableMethodSecurity
@RequiredArgsConstructor
public class SecurityConfig implements AuthenticationFailureHandler {
	
	@Value(value = "${conatus.security.oauth2.callback.url}")
	private String URL_HOME;
	
	private final SecurityFilter securityFilter;
		
	@Bean
	@Order(Ordered.LOWEST_PRECEDENCE - 1)
	SecurityFilterChain bearerAuthFilterChain(HttpSecurity http) throws Exception {

	    http.securityMatcher(new BearerRequestedMatcher());

	    http.csrf(AbstractHttpConfigurer::disable);
	    
	    http.httpBasic(AbstractHttpConfigurer::disable);
	    
	    http.sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));
	    
	    http.authorizeHttpRequests(request -> {
	    	request.requestMatchers(HttpMethod.POST, "/auth/login/token").permitAll();
	    	request.requestMatchers(HttpMethod.POST, "/auth/register").permitAll();
	    	request.anyRequest().authenticated();
	    });
    	
    	http.addFilterBefore(securityFilter, UsernamePasswordAuthenticationFilter.class);
	    
	    return http.build();
	}
		
	@Bean
	@Order(Ordered.LOWEST_PRECEDENCE)
    public SecurityFilterChain oauth2FilterChain(HttpSecurity http) throws Exception {
		
	    http.securityMatcher(new OauthRequestedMatcher());
		
	    http.csrf(AbstractHttpConfigurer::disable);
	    
	    http.httpBasic(AbstractHttpConfigurer::disable); 
		
    	http.authorizeHttpRequests(request -> {
    		request.requestMatchers("/auth/login/google").authenticated(); //url redirect apos usuario autenticar
    		request.requestMatchers("/login/oauth2/code/google").authenticated(); //url oauth2
    		request.requestMatchers("/oauth2/authorization/google").authenticated();//url oauth2
    		request.requestMatchers("/oauth2/login/google").authenticated(); //url login
    		request.anyRequest().denyAll();
    	});
    	
    	http.oauth2Login(auth -> {
    		auth.defaultSuccessUrl("/auth/login/google?auth=true", true);
    		auth.failureHandler(authenticationFailureHandler());
    	});
	
	    http.logout((logout) -> {
	    	logout.invalidateHttpSession(true);
	    	logout.clearAuthentication(true);
	    	logout.logoutSuccessUrl(URL_HOME);
	    	logout.deleteCookies("JSESSIONID");
	    });
	    
	    http.formLogin(form -> form.disable());
        
        return http.build();
    }

    @Bean
    public AuthenticationFailureHandler authenticationFailureHandler() {
        return this;
    }
    
	@Bean
	public AuthenticationManager authenticationManager(AuthenticationConfiguration authenticationConfiguration) throws Exception {
		return authenticationConfiguration.getAuthenticationManager();
	}
	
	@Bean
	public PasswordEncoder passwordEncoder() {
		return new BCryptPasswordEncoder();
	}
    
	@Override
	public void onAuthenticationFailure(HttpServletRequest request, HttpServletResponse response, AuthenticationException exception) throws IOException, ServletException {
	
		response.sendRedirect(URL_HOME.concat("?auth=false"));
	}
	
	private static class BearerRequestedMatcher implements RequestMatcher {
	    @Override
	    public boolean matches(HttpServletRequest request) {
	        String auth = request.getHeader("Authorization");
	        String uri = request.getRequestURI();
	        
	        // Determina se a requisição é feita através do bearer token
	        return (!uri.contains("oauth2")) && ((auth != null) && (auth.startsWith("Bearer")) || uri.contains("/register") || uri.contains("/login"));
	    }
	}
	
	private static class OauthRequestedMatcher implements RequestMatcher {
		
	    @Override
	    public boolean matches(HttpServletRequest request) {
	        String uri = request.getRequestURI();
	        String authToken = request.getHeader("Authorization");
	        
	        // Determina se a requisição é feita através do oauth2
	        return (uri != null)
	        		&& (uri.contains("oauth2") || uri.contains("google") || uri.contains("logout"))
	        		&& StringUtils.isBlank(authToken);
	    }
	}
	

}
