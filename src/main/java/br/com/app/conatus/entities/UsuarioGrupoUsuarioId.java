package br.com.app.conatus.entities;

import java.io.Serializable;

import jakarta.persistence.Embeddable;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Data;
import lombok.EqualsAndHashCode;
import lombok.NoArgsConstructor;

@Embeddable
@Data @Builder
@NoArgsConstructor
@AllArgsConstructor
@EqualsAndHashCode
public class UsuarioGrupoUsuarioId implements Serializable {
	
	private static final long serialVersionUID = 1L;
	
	@ManyToOne
	@JoinColumn(name = "ID_GRUPO_USUARIO")
	private GrupoUsuarioEntity grupoUsuario;
	
	@ManyToOne
	@JoinColumn(name = "ID_USUARIO")
	private UsuarioEntity usuario;
	

}
