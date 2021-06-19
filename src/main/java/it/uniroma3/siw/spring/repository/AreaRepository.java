package it.uniroma3.siw.spring.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import it.uniroma3.siw.spring.model.Area;
import it.uniroma3.siw.spring.model.Habitat;

public interface AreaRepository extends CrudRepository<Area,Long> {
	public List<Area> findByNome(String nome);

	public List<Area> findByNomeAndHabitat(String nome, Habitat habitat);

	public List<Area> findByNomeOrHabitat(String nome,Habitat habitat);
	
	public List<Area> findByHabitat(Habitat habitat);
}
