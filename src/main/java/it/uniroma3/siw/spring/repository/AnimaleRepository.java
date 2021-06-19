package it.uniroma3.siw.spring.repository;

import java.util.List;

import org.springframework.data.repository.CrudRepository;

import it.uniroma3.siw.spring.model.Animale;
import it.uniroma3.siw.spring.model.Area;

public interface AnimaleRepository extends CrudRepository<Animale,Long> {
	public List<Animale> findByNome(String nome);

	public List<Animale> findByArea(Area area);
	
	public List<Animale> findByNomeAndArea(String nome,Area area);

	public List<Animale> findByNomeOrArea(String nome,Area area);
}
