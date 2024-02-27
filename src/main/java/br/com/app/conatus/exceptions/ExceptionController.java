package br.com.app.conatus.exceptions;

import java.time.Instant;
import java.util.List;

import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.MethodArgumentNotValidException;
import org.springframework.web.bind.annotation.ExceptionHandler;
import org.springframework.web.bind.annotation.RestControllerAdvice;
import org.springframework.web.servlet.resource.NoResourceFoundException;

import jakarta.servlet.http.HttpServletRequest;
import lombok.RequiredArgsConstructor;

@RestControllerAdvice
@RequiredArgsConstructor
public class ExceptionController {

	
	@ExceptionHandler(MsgException.class)
	public ResponseEntity<ErroResponse> tratarErro400(MsgException e, HttpServletRequest request){
		ErroResponse erro = new ErroResponse(Instant.now(), new DetalheErroResponse("Falha na requisição", e.getMessage()), request.getRequestURI());
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(erro);
	}
	
	@ExceptionHandler(NaoEncontradoException.class)
	public ResponseEntity<ErroResponse> tratarErro404(NaoEncontradoException e, HttpServletRequest request){
		ErroResponse erro = new ErroResponse(Instant.now(), new DetalheErroResponse("Recurso não encontrado", e.getMessage()), request.getRequestURI());
		return ResponseEntity.status(HttpStatus.NOT_FOUND).body(erro);
	}
	
	@ExceptionHandler(NoResourceFoundException.class)
	public ResponseEntity<ErroResponse> tratarErro404(NoResourceFoundException e, HttpServletRequest request){
		ErroResponse erro = new ErroResponse(Instant.now(), new DetalheErroResponse("Recurso não encontrado", "Recurso solicitado é invalido."), request.getRequestURI());
		return ResponseEntity.status(HttpStatus.NOT_FOUND).body(erro);
	}
	
	@ExceptionHandler(MethodArgumentNotValidException.class)
	public ResponseEntity<ErroResponse> tratarErroBeanValidation(MethodArgumentNotValidException e, HttpServletRequest request){
		List<DetalheErroResponse> errosValidacao = e.getFieldErrors().stream().map(DetalheErroResponse::new).toList();
		ErroResponse erro = new ErroResponse(Instant.now(), errosValidacao, request.getRequestURI());
		return ResponseEntity.status(HttpStatus.BAD_REQUEST).body(erro);
	}
	
}
