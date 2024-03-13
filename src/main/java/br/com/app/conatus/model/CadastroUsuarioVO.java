package br.com.app.conatus.model;

import java.io.Serializable;

import jakarta.validation.constraints.NotBlank;
import lombok.Builder;
import lombok.Data;

@Builder
@Data
public class CadastroUsuarioVO implements Serializable{
	
	private static final long serialVersionUID = 1L;
	
	@NotBlank(message = "O nome é obrigatório.")
	private String nome;
	
	@NotBlank(message = "O email é obrigatório.")
	private String email;
	
	@NotBlank(message = "A senha é obrigatória.")
	private String senha;
	
	@NotBlank(message = "O telefone é obrigatório.")
	private String telefone;
	
	private String idGoogle;

}
