package br.com.app.conatus.configuration;

import static org.springframework.boot.autoconfigure.security.servlet.PathRequest.toH2Console;

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
	
	@Bean
	@Order(Ordered.LOWEST_PRECEDENCE - 1)
	SecurityFilterChain bearerAuthFilterChain(HttpSecurity http) throws Exception {

	    http.securityMatcher(new BearerRequestedMatcher());
	        
	    http.sessionManagement(session -> session.sessionCreationPolicy(SessionCreationPolicy.STATELESS));
	    
	    http.authorizeHttpRequests(request -> {
	    	request.requestMatchers(HttpMethod.POST, "/auth/login/token").permitAll();
	    	request.requestMatchers(HttpMethod.POST, "/auth/register").permitAll();
	    	request.requestMatchers(toH2Console()).permitAll();
	    	request.anyRequest().authenticated();
	    });
	    
        http.csrf((csrf) -> csrf.disable());

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
    		request.requestMatchers("/login/oauth2/code/google").authenticated(); //url client oauth2
    		request.requestMatchers("/oauth2/authorization/google").authenticated();//url client oauth2
    		request.requestMatchers("/auth/login/google").authenticated(); //url redirect apos usuario autenticar no client
    		request.requestMatchers("/oauth2/login/google").authenticated(); //url utilizada para fazer login via oauth
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
    public SecurityFilterChain withoutAuthorizeFilterChain(HttpSecurity http) throws Exception {
		
	    http.securityMatcher(new WithoutAuthorizeRequestedMatcher());
		
	    http.authorizeHttpRequests(auth -> {
	    	auth.requestMatchers(toH2Console()).permitAll();
	    	auth.anyRequest().denyAll();
	    });
        
	    http.headers(header -> header.frameOptions(frame -> frame.disable()));
	            
        return http.build();
    }
    
	
	private static class BearerRequestedMatcher implements RequestMatcher {
	    @Override
	    public boolean matches(HttpServletRequest request) {
	        String auth = request.getHeader("Authorization");
	        String uri = request.getRequestURI();
	        
	        // Determina se a requisição é feita através do bearer token
	        return isBearerAuth(auth, uri);
	    }

		private static boolean isBearerAuth(String auth, String uri) {
			return (!uri.contains("oauth2")) && ((auth != null) && (auth.startsWith("Bearer")) || uri.contains("/register") || uri.contains("/login/token"));
		}
	}
	
	private static class OauthRequestedMatcher implements RequestMatcher {
		
	    @Override
	    public boolean matches(HttpServletRequest request) {
	        String uri = request.getRequestURI();
	        String auth = request.getHeader("Authorization");
	        
	        // Determina se a requisição é feita através do oauth2
	        return isOauth2Auth(auth, uri);
	    }
	    
		private static boolean isOauth2Auth(String auth, String uri) {
			return (uri != null)
	        		&& (uri.contains("/login/oauth2/code/google") || 
	        				uri.contains("/oauth2/authorization/google") || 
	        				uri.contains("/auth/login/google") || 
	        				uri.contains("/oauth2/login/google") ||
	        				uri.contains("/logout"))
	        		&& StringUtils.isBlank(auth);
		}
	}
	
	private static class WithoutAuthorizeRequestedMatcher implements RequestMatcher {
		
	    @Override
	    public boolean matches(HttpServletRequest request) {

	        // Determina se a requisição esta sendo feita sem nenhum tipo de autenticacao valida
	        return isWithoutAuthorizeRequestedMatcher(request);
	    }
	    
		private boolean isWithoutAuthorizeRequestedMatcher(HttpServletRequest request) {
			
	        String auth = request.getHeader("Authorization");
	        String uri = request.getRequestURI();

			return ! (BearerRequestedMatcher.isBearerAuth(auth, uri) || OauthRequestedMatcher.isOauth2Auth(auth, uri));
		}
	}
	
}
