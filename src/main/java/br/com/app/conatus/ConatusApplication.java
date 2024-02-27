package br.com.app.conatus;

import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;

@SpringBootApplication
public class ConatusApplication {

	public static void main(String[] args) {
		SpringApplication.run(ConatusApplication.class, args);
	}
//	
//	@Autowired
//	private UsuarioRepository repository;
//
//	@Override
//	public void run(String... args) throws Exception {
//	
//		UsuarioEntity usuario = UsuarioEntity.builder()
//				.token("123")
//				.situacao(DominioEntity.builder()
//						.codigo(CodigoDominio.STATUS_ATIVO.name())
//						.build())
//				.pessoa(PessoaFisicaEntity.builder().email("").build())
//				
//				.build();
//		
//	}

}
