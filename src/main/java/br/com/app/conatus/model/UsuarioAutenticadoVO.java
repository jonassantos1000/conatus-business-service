package br.com.app.conatus.model;

import java.util.Collection;
import java.util.List;

import org.springframework.security.core.GrantedAuthority;
import org.springframework.security.core.authority.SimpleGrantedAuthority;
import org.springframework.security.core.userdetails.UserDetails;

import br.com.app.conatus.enums.CodigoDominio;
import lombok.AllArgsConstructor;
import lombok.Builder;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;

@Builder
@NoArgsConstructor @AllArgsConstructor
@Getter @Setter
public class UsuarioAutenticadoVO implements UserDetails {

	private static final long serialVersionUID = 1L;
	
	private Long idUsuario;
	private String email;
	private String senha;
	private String situacao;
	private List<String> codigoAutorizacoes;

	@Override
	public Collection<? extends GrantedAuthority> getAuthorities() {
		return this.codigoAutorizacoes.stream().map(SimpleGrantedAuthority::new).toList();
	}

	@Override
	public String getPassword() {
		return this.senha;
	}

	@Override
	public String getUsername() {
		return this.email;
	}
	
	@Override
	public boolean isEnabled() {
		return CodigoDominio.STATUS_ATIVO.name().equals(this.situacao);
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

}
