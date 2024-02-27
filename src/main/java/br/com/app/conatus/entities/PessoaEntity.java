package br.com.app.conatus.entities;

import java.io.Serializable;
import java.time.ZonedDateTime;

import org.hibernate.annotations.UpdateTimestamp;

import br.com.app.conatus.enums.TipoPessoaEnum;
import jakarta.persistence.Column;
import jakarta.persistence.Entity;
import jakarta.persistence.EnumType;
import jakarta.persistence.Enumerated;
import jakarta.persistence.FetchType;
import jakarta.persistence.GeneratedValue;
import jakarta.persistence.GenerationType;
import jakarta.persistence.Id;
import jakarta.persistence.Inheritance;
import jakarta.persistence.InheritanceType;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import jakarta.persistence.Table;
import lombok.AllArgsConstructor;
import lombok.EqualsAndHashCode;
import lombok.EqualsAndHashCode.Include;
import lombok.Getter;
import lombok.NoArgsConstructor;
import lombok.Setter;
import lombok.experimental.SuperBuilder;

@Entity
@Table(name = "TB_PESSOA")
@AllArgsConstructor @NoArgsConstructor
@SuperBuilder @Getter @Setter
@EqualsAndHashCode(onlyExplicitlyIncluded = true)
@Inheritance(strategy = InheritanceType.JOINED)
public class PessoaEntity implements Serializable{

	private static final long serialVersionUID = 1L;

	@Id @Include 
	@Column(name = "IDENT") 
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long id;
	
	@Column(name = "DS_NOME")
	private String nome;
	
	@Column(name = "DS_EMAIL")
	private String email;
	
	@Column(name = "DT_ATUALIZACAO")
	@UpdateTimestamp
	private ZonedDateTime dataAtualizacao;
	
	@Column(name = "DS_TELEFONE")
	private String telefone;
	
	@Column(name = "SG_TIPO_PESSOA", nullable = false)
	@Enumerated(EnumType.STRING)
	private TipoPessoaEnum tipoPessoa;
	
	@ManyToOne(fetch = FetchType.LAZY)
	@JoinColumn(name = "ID_DOM_SITUACAO")
	private DominioEntity situacao;
}
