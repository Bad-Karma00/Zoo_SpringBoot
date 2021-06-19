package it.uniroma3.siw.spring.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import it.uniroma3.siw.spring.model.Habitat;
import it.uniroma3.siw.spring.model.Responsabile;

public interface HabitatRepository extends CrudRepository<Habitat, Long>  {
	public List<Habitat> findByNome(String nome);

	public List<Habitat> findByNomeAndResponsabile(String nome, Responsabile responsabile);

	public List<Habitat> findByNomeOrResponsabile(String nome,Responsabile responsabile);
	
	public List<Habitat> findByResponsabile(Responsabile responsabile);
}
