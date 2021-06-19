package it.uniroma3.siw.spring.service;

import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import it.uniroma3.siw.spring.model.Animale;
import it.uniroma3.siw.spring.model.Area;
import it.uniroma3.siw.spring.model.Habitat;
import it.uniroma3.siw.spring.repository.AreaRepository;

@Service
public class AreaService {

	@Autowired
	private AreaRepository areaRepository; 
	
	@Autowired 
	private AnimaleService animaleService;	
		
	@Transactional
	public Area inserisci(Area area) {
		return areaRepository.save(area);
	}
	
	@Transactional
	public List<Area> areaPerNomeAndHabitat(String nome, Habitat habitat) {
		return areaRepository.findByNomeAndHabitat(nome, habitat);
	}
	
	@Transactional
	public List<Area> areaHabitat(Habitat habitat) {
		return areaRepository.findByHabitat(habitat);
	}

	@Transactional
	public List<Area> tutte() {
		return (List<Area>) areaRepository.findAll();
	}

	@Transactional
	public Area areaPerId(Long id) {
		Optional<Area> optional = areaRepository.findById(id);
		if (optional.isPresent())
			return optional.get();
		else 
			return null;
	}

	@Transactional
	public boolean alreadyExists(Area area) {
		List<Area> aree = this.areaRepository.findByNomeAndHabitat(area.getNome(), area.getHabitat());
		if (aree.size() > 0)
			return true;
		else 
			return false;
	}
	//
	@Transactional
	public void delete(Area collezione){
	List<Animale> animali = animaleService.tutti();
		if(!(animali.isEmpty())) {
		for(Animale animale : animali) {
				animale.setAreaAnimale(null);
			}
	}
	
		this.areaRepository.delete(collezione);
	}
}
