package br.com.app.conatus.service;

import org.springframework.stereotype.Service;

import br.com.app.conatus.entities.GrupoUsuarioEntity;
import br.com.app.conatus.entities.UsuarioEntity;
import br.com.app.conatus.entities.UsuarioGrupoUsuarioEntity;
import br.com.app.conatus.repositories.UsuarioGrupoUsuarioRepository;
import lombok.RequiredArgsConstructor;

@Service
@RequiredArgsConstructor
public class UsuarioGrupoUsuarioService {

	private final UsuarioGrupoUsuarioRepository usuarioGrupoUsuarioRepository;

	protected void vincularUsuarioNoGrupoUsuario(UsuarioEntity usuario, GrupoUsuarioEntity grupo) {
		usuarioGrupoUsuarioRepository.save(UsuarioGrupoUsuarioEntity.builder().idGrupoUsuario(grupo.getId()).idUsuario(usuario.getId()).build());
	}

	protected void vincularGrupoBasicoNoUsuario(UsuarioEntity usuario) {
		
		usuarioGrupoUsuarioRepository.save(UsuarioGrupoUsuarioEntity.builder()
				.idGrupoUsuario(1L)
				.idUsuario(usuario.getId())
				.build());
	}
	
}
