package br.com.app.conatus.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.stereotype.Repository;

import br.com.app.conatus.entities.UsuarioEntity;

@Repository
public interface UsuarioRepository extends JpaRepository<UsuarioEntity, Long>{

	@Query(" SELECT usuario FROM UsuarioEntity usuario "
			+ " JOIN FETCH usuario.pessoa pessoaFisica "
			+ " LEFT JOIN FETCH usuario.grupos grupos "
			+ " LEFT JOIN FETCH grupos.grupoUsuario grupoUsuario "
			+ " LEFT JOIN FETCH grupoUsuario.autorizacoes autorizacao "
			+ " LEFT JOIN FETCH autorizacao.permissao permissao "
			+ " WHERE pessoaFisica.email = :email "
			+ " AND usuario.situacao.codigo = :codSituacao ")
	UsuarioEntity findByUsername(String email, String codSituacao);
	
	boolean existsByPessoaEmail(String email); 

}
