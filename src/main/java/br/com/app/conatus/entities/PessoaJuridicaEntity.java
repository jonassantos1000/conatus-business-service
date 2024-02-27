package br.com.app.conatus.entities;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.PrimaryKeyJoinColumn;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Entity
@Table(name = "TB_PESSOA_JURIDICA")
@PrimaryKeyJoinColumn(name="ID_PESSOA")
@SuperBuilder @Setter @Getter 
@AllArgsConstructor @NoArgsConstructor
public class PessoaJuridicaEntity extends PessoaEntity {
	
	private static final long serialVersionUID = 1L;

	@Column(name = "CD_CNPJ")
	private String cnpj;
	
	@Column(name = "DS_NOME_FANTASIA")
	private String nomeFantasia;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "ID_DOM_RAMO_ATIV")
	private DominioEntity ramoAtividade;
	
}
