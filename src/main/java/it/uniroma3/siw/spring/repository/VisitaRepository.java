package it.uniroma3.siw.spring.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import it.uniroma3.siw.spring.model.Visita;

public interface VisitaRepository extends CrudRepository<Visita,Long> {

	public List<Visita> findByNome(String nome);

	public List<Visita> findByNomeAndCognome(String nome, String cognome);

	public List<Visita> findByNomeOrCognome(String nome, String cognome);
}
