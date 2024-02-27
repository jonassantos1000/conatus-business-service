package br.com.app.conatus.entities;

import java.time.ZonedDateTime;

import org.hibernate.annotations.CreationTimestamp;
import org.hibernate.annotations.UpdateTimestamp;

import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.JoinColumn;
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
@Table(name = "TB_TENANT")
@Builder @AllArgsConstructor @NoArgsConstructor
@Setter @Getter @EqualsAndHashCode(onlyExplicitlyIncluded = true)
public class TenantEntity {
	
	@Id @Include
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	@Column(name = "IDENT")
	private Long id;
	
	@Column(name = "CD_TENANT", unique = true)
	private String codigoTenant;

	@Column(name = "DS_NOME")
	private String nome;
	
	@Column(name = "DT_INICIO_AMOSTRA_GRATIS")
	private ZonedDateTime dataInicioAmostraGratis;
	
	@Column(name = "DT_FIM_AMOSTRA_GRATIS")
	private ZonedDateTime dataFimAmostraGratis;
	
	@Column(name = "QT_USUARIO")
	private Integer quantidadeUsuarios;
	
	@CreationTimestamp
	@Column(name = "DT_CADASTRO")
	private ZonedDateTime dataCadastro;
	
	@UpdateTimestamp
	@Column(name = "DT_ATUALIZACAO")
	private ZonedDateTime dataAtualizacao;
	
	@OneToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "ID_PESSOA_JURIDICA", unique = true, nullable = false)
	private PessoaJuridicaEntity pessoaJuridica;
}
