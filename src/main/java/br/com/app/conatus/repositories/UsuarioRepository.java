package br.com.app.conatus.repositories;

import java.util.List;
import java.util.Optional;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.stereotype.Repository;

import br.com.app.conatus.entities.UsuarioEntity;

@Repository
public interface UsuarioRepository extends JpaRepository<UsuarioEntity, Long>{

	@Query(" SELECT usuario FROM UsuarioEntity usuario "
			+ " JOIN FETCH usuario.pessoa pessoaFisica "
			+ " JOIN FETCH usuario.situacao situacao "
			+ " WHERE pessoaFisica.email = :email "
			+ " AND usuario.situacao.codigo = :codSituacao ")
	Optional<UsuarioEntity> findByUsername(String email, String codSituacao);
	
	@Query(value = " SELECT DISTINCT p.CODIGO_PERMISSAO FROM TB_USUARIO u "
			+ " INNER JOIN TB_USUARIO_GRUPO_USUARIO ugu on u.IDENT = ugu.ID_USUARIO "
			+ " INNER JOIN TB_AUTORIZACAO_GRUPO_USUARIO agu on agu.ID_GRUPO_USUARIO = ugu.ID_GRUPO_USUARIO "
			+ " INNER JOIN TB_PERMISSAO p on p.IDENT = agu.ID_PERMISSAO "
			+ " where u.IDENT = :idUsuario ", nativeQuery = true)
	List<String> findAutorizacoesByUsuarioId(@Param("idUsuario") Long idUsuario);
		
	boolean existsByPessoaEmail(String email); 
	
	boolean existsByIdGoogle(String idGoogle);

}
