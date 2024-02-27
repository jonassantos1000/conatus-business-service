package br.com.app.conatus.entities;

import java.io.Serializable;
import java.util.List;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.Table;
import lombok.EqualsAndHashCode;
import lombok.EqualsAndHashCode.Include;
import lombok.Getter;

@Entity
@Table(name = "TB_GRUPO_USUARIO")
@Getter 
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class GrupoUsuarioEntity implements Serializable {

	private static final long serialVersionUID = 1L;
	
	@Id @Include
	@Column(name = "IDENT")
	private Long id;
	
	@Column(name = "DESC_GRUPO")
	private String grupo;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "ID_DOM_SITUACAO")
	private DominioEntity situacao;
	
	@OneToMany(mappedBy = "grupoUsuario", fetch = FetchType.LAZY)
	private List<AutorizacaoGrupoUsuarioEntity> autorizacoes;

}
