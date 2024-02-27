package br.com.app.conatus.exceptions;

import java.time.Instant;
import java.util.Arrays;
import java.util.List;

import com.fasterxml.jackson.annotation.JsonFormat;

public record ErroResponse(@JsonFormat(shape = JsonFormat.Shape.STRING, pattern="yyyy-MM-dd'T'HH:mm:ss'Z'", timezone="GMT")
		Instant timestamp, List<DetalheErroResponse> erros, String path) {
	
	public ErroResponse(Instant timestamp, DetalheErroResponse erros, String path) {
		this(timestamp, Arrays.asList(erros), path);
	}
}
