package br.com.app.conatus.entities.factory;

import br.com.app.conatus.entities.DominioEntity;
import br.com.app.conatus.entities.PessoaFisicaEntity;
import br.com.app.conatus.entities.UsuarioEntity;
import br.com.app.conatus.model.CadastroUsuarioVO;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class UsuarioEntityFactory {

	public static UsuarioEntity converterParaEntity(CadastroUsuarioVO dados, PessoaFisicaEntity pessoa, DominioEntity situacao) {
		
		return UsuarioEntity.builder()
				.pessoa(pessoa)
				.senha(dados.getSenha())
				.situacao(situacao)
				.idGoogle(dados.getIdGoogle())
				.build();
	}
}
