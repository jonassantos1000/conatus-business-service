package br.com.app.conatus.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.com.app.conatus.entities.UsuarioGrupoUsuarioEntity;

@Repository
public interface UsuarioGrupoUsuarioRepository extends JpaRepository<UsuarioGrupoUsuarioEntity, Long>{

}
