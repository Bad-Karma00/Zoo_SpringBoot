package it.uniroma3.siw.spring.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import it.uniroma3.siw.spring.model.Responsabile;

public interface ResponsabileRepository extends CrudRepository<Responsabile,Long> {
	
	public List<Responsabile> findByNome(String nome);

	public List<Responsabile> findByNomeAndCognome(String nome, String cognome);

	public List<Responsabile> findByNomeOrCognome(String nome, String cognome);
	

}
