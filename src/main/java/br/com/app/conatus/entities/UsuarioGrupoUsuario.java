package br.com.app.conatus.entities;

import java.io.Serializable;

import jakarta.persistence.EmbeddedId;
import jakarta.persistence.Entity;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "TB_USUARIO_GRUPO_USUARIO")
@Builder @AllArgsConstructor @NoArgsConstructor
@Setter @Getter @EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class UsuarioGrupoUsuario implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	@EmbeddedId
	private UsuarioGrupoUsuarioId idUsuarioGrupoUsuario;

}
