package br.com.app.conatus.entities;

import java.io.Serializable;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.EqualsAndHashCode.Include;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "TB_DOMINIO")
@Builder @Setter @Getter 
@AllArgsConstructor @NoArgsConstructor @EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class DominioEntity implements Serializable {

	private static final long serialVersionUID = 1L;
	
	@Id @Include
	@Column(name = "IDENT")
	private Long id;
	
	@Column(name = "DOMINIO")
	private String descricao;
	
	@Column(name = "COD_DOMINIO")
	private String codigo;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "ID_TIPO")
	private DominioTipoEntity tipo;
	
}
