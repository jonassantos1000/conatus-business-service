package br.com.app.conatus.service;

import org.springframework.cache.annotation.Cacheable;
import org.springframework.stereotype.Service;

import br.com.app.conatus.entities.DominioEntity;
import br.com.app.conatus.enums.CodigoDominio;
import br.com.app.conatus.exceptions.NaoEncontradoException;
import br.com.app.conatus.repositories.DominioRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class DominioService {
	
	private final DominioRepository dominioRepository;
	
	@Cacheable("dominio-id")
	public DominioEntity recuperarPorId(Long id) {
		return dominioRepository.findById(id).orElseThrow(
				() -> new NaoEncontradoException("Não foi encontrado um dominio com id: %d".formatted(id)));
	}
	
	@Cacheable("dominio-codigo")
	public DominioEntity recuperarPorCodigo(String codigo) {
		return dominioRepository.findByCodigo(codigo).orElseThrow(
				() -> new NaoEncontradoException("Não foi encontrado um dominio com codigo: %s".formatted(codigo)));
	}
	
	@Cacheable("dominio-codigo")
	public DominioEntity recuperarPorCodigo(CodigoDominio codigo) {
		return recuperarPorCodigo(codigo.name());
	}

//	@Cacheable("controller-dominio-id")
//	public DominioResponse buscarDominioPorId(Long id) {
//		return DominioResponseFactory.converterParaResponse(recuperarPorId(id));
//	}
//	
//	@Cacheable("controller-dominio-codigo")
//	public DominioResponse buscarDominioPorCodigo(String codigo) {
//		return DominioResponseFactory.converterParaResponse(recuperarPorCodigo(codigo));
//	}
//
//	@Cacheable("controller-dominio-tipo-codigo")
//	public List<DominioResponse> buscarDominioPorCodigoTipo(String codTipo) {
//		return dominioRepository.findByTipoCodigo(codTipo).stream().map(DominioResponseFactory::converterParaResponse)
//				.toList();
//	}
//
//	@Cacheable("controller-dominio-tipo-id")
//	public List<DominioResponse> buscarDominioPorIdTipo(Long idTipo) {
//		return dominioRepository.findByTipoId(idTipo).stream().map(DominioResponseFactory::converterParaResponse)
//				.toList();
//	}

}
