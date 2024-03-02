package br.com.app.conatus.service;

import java.time.Instant;
import java.time.LocalDateTime;
import java.time.ZoneOffset;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.stereotype.Service;

import com.auth0.jwt.JWT;
import com.auth0.jwt.algorithms.Algorithm;
import com.auth0.jwt.exceptions.JWTCreationException;
import com.auth0.jwt.exceptions.JWTVerificationException;

import br.com.app.conatus.entities.UsuarioEntity;
import br.com.app.conatus.exceptions.MsgException;

@Service
public class TokenService {
	
	@Value("${conatus.security.jwt.secret}")
	private String secretJwt;

	public String generateToken(UsuarioEntity usuario) {
		try {
			Algorithm algorithm = Algorithm.HMAC256(secretJwt);
			String token = JWT.create()
					.withIssuer("conatus-bussines-api")
					.withSubject(usuario.getUsername())
					.withExpiresAt(generateExpirationDate())
					.sign(algorithm);
			return token;
		} catch (JWTCreationException e) {
			throw new RuntimeException("Error while generating token", e);
		}
	}
	
	public String validateToken(String token) {
		try {
			Algorithm algorithm = Algorithm.HMAC256(secretJwt);
			return JWT.require(algorithm)
					.withIssuer("conatus-bussines-api")
					.build()
					.verify(token)
					.getSubject();
		} catch (JWTVerificationException e) {
			throw new MsgException("Token invalido.");
		}
	}
	
	private Instant generateExpirationDate() {
		return LocalDateTime.now().plusHours(2).toInstant(ZoneOffset.of("-03:00"));
	}
}
