package br.com.app.conatus.entities;

import java.io.Serializable;

import br.com.app.conatus.enums.TipoPermissaoEnum;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.EqualsAndHashCode.Include;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "TB_PERMISSAO")
@Builder @AllArgsConstructor @NoArgsConstructor
@Setter @Getter @EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class PermissaoEntity implements Serializable{

	private static final long serialVersionUID = 1L;

	@Id @Include
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "IDENT")
	private Long id;
	
	private String permissao;
	
	private String codigoPermissao;
	
	@Column(name = "SG_TIPO", nullable = false)
	@Enumerated(EnumType.STRING)
	private TipoPermissaoEnum tipoPermissao;
}
