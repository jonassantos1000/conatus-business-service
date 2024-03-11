package br.com.app.conatus.entities.factory;

import br.com.app.conatus.entities.DominioEntity;
import br.com.app.conatus.entities.PessoaFisicaEntity;
import br.com.app.conatus.enums.TipoPessoaEnum;
import br.com.app.conatus.model.CadastroUsuarioVO;
import lombok.AccessLevel;
import lombok.NoArgsConstructor;

@NoArgsConstructor(access = AccessLevel.PRIVATE)
public class PessoaFisicaEntityFactory {

	public static PessoaFisicaEntity converterParaEntity(CadastroUsuarioVO dados, DominioEntity situacao) {
		return PessoaFisicaEntity.builder()
				.id(1L)
				.email(dados.getEmail())
				.tipoPessoa(TipoPessoaEnum.PESSOA_FISICA)
				.situacao(situacao)
				.telefone(dados.getTelefone())
				.nome(dados.getNome())
				.build();
	}
}
