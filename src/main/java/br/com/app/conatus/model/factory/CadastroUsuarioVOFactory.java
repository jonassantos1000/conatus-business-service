package br.com.app.conatus.model.factory;

import org.springframework.security.oauth2.core.oidc.user.DefaultOidcUser;

import br.com.app.conatus.model.CadastroUsuarioVO;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class CadastroUsuarioVOFactory {
	
	public static CadastroUsuarioVO converterParaVO(DefaultOidcUser usuario) {
		return CadastroUsuarioVO.builder()
				.nome(usuario.getName())
				.email(usuario.getEmail())
				.idGoogle(usuario.getSubject())
				.build();
	}

}
