package br.com.app.conatus.repositories;

import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.stereotype.Repository;

import br.com.app.conatus.entities.PessoaEntity;

@Repository
public interface PessoaRepository extends JpaRepository<PessoaEntity, Long>{

}
