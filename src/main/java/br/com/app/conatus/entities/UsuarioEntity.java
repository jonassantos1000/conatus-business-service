package br.com.app.conatus.entities;

import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.List;
import java.util.Set;

import org.hibernate.annotations.UpdateTimestamp;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.OneToMany;
import jakarta.persistence.OneToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.EqualsAndHashCode;
import lombok.EqualsAndHashCode.Include;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Entity
@Table(name = "TB_USUARIO")
@Builder @AllArgsConstructor @NoArgsConstructor
@Setter @Getter @EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class UsuarioEntity implements Serializable {
	
	private static final long serialVersionUID = 1L;

	@Id @Include
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "IDENT")
	private Long id;
	
	@Column(name = "DS_SENHA")
	private String senha;
	
	@Column(name = "DS_TOKEN")
	private String token;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "ID_PESSOA")
	private PessoaFisicaEntity pessoa;
	
	@OneToMany(mappedBy = "usuario", fetch = FetchType.LAZY)
	private Set<UsuarioTenantEntity> tenants;
	
	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "ID_TENANT_SELECIONADO", referencedColumnName = "CD_TENANT")
	private TenantEntity tenantSelecionado;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "ID_DOM_SITUACAO")
	private DominioEntity situacao;
	
	@UpdateTimestamp
	@Column(name = "DT_ATUALIZACAO")
	private ZonedDateTime dataAtualizacao;
	
	@OneToMany(mappedBy = "usuario", fetch = FetchType.LAZY)
	private List<UsuarioGrupoUsuario> grupos;

}
