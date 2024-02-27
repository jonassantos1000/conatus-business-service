package br.com.app.conatus.entities;

import java.io.Serializable;
import java.time.ZonedDateTime;
import java.util.Collection;
import java.util.List;
import java.util.Set;

import org.hibernate.annotations.UpdateTimestamp;
import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import br.com.app.conatus.enums.CodigoDominio;
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
public class UsuarioEntity implements UserDetails, Serializable {
	
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
	
	@OneToMany(mappedBy = "idUsuarioGrupoUsuario.usuario")
	private List<UsuarioGrupoUsuario> grupos;

	@Override
	public boolean isEnabled() {
		return CodigoDominio.STATUS_ATIVO.name().equals(this.getSituacao().getCodigo());
	}

	@Override
	public String getPassword() {
		return this.getPassword();
	}

	@Override
	public String getUsername() {
		return this.getPessoa().getEmail();
	}

	@Override
	public boolean isAccountNonExpired() {
		return this.isEnabled();
	}

	@Override
	public boolean isAccountNonLocked() {
		return this.isEnabled();
	}

	@Override
	public boolean isCredentialsNonExpired() {
		return this.isEnabled();
	}
	
	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return this.getGrupos().stream()
				.flatMap(grupo -> grupo.getIdUsuarioGrupoUsuario().getGrupoUsuario().getAutorizacoes().stream())
				.map(autorizacao -> {
					return new SimpleGrantedAuthority(autorizacao.getPermissao().getCodigoPermissao());
				}).toList();
	}

}
