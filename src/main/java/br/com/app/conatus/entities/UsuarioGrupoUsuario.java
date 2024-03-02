package br.com.app.conatus.entities;

import java.io.Serializable;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.Id;
import jakarta.persistence.IdClass;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
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
@IdClass(UsuarioGrupoUsuarioId.class)
public class UsuarioGrupoUsuario implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	@EqualsAndHashCode.Include
	@Id
	@Column(name =  "ID_GRUPO_USUARIO")
	private Long idGrupoUsuario;
	
	@EqualsAndHashCode.Include
	@Id
	@Column(name =  "ID_USUARIO")
	private Long idUsuario;

	@ManyToOne(optional = false)
	@JoinColumn(name = "ID_GRUPO_USUARIO", insertable = false, updatable = false)
	private GrupoUsuarioEntity grupoUsuario;
	
	@ManyToOne(optional = false)
	@JoinColumn(name = "ID_USUARIO", insertable = false, updatable = false)
	private UsuarioEntity usuario;

}
