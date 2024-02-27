package br.com.app.conatus.entities;

import java.io.Serializable;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.EqualsAndHashCode;
import lombok.EqualsAndHashCode.Include;
import lombok.Getter;

@Entity
@Table(name = "TB_TIPO_DOMINIO")
@Getter 
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class DominioTipoEntity implements Serializable{

	private static final long serialVersionUID = 1L;

	@Id @Include
	@Column(name = "IDENT")
	private Long id;
	
	@Column(name = "DS_TIPO_DOMINIO")
	private String descricao;
	
	@Column(name = "COD_TIPO_DOMINIO")
	private String codigo;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "ID_DOM_SITUACAO")
	private DominioEntity situacao;
	
}
