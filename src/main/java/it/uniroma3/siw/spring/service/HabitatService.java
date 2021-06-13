package it.uniroma3.siw.spring.service;

import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import it.uniroma3.siw.spring.model.Habitat;
import it.uniroma3.siw.spring.model.Responsabile;
import it.uniroma3.siw.spring.repository.HabitatRepository;


@Service
public class HabitatService {

	@Autowired
	private HabitatRepository habitatRepository; 
	
	
	@Transactional
	public Habitat inserisci(Habitat habitat) {
		return habitatRepository.save(habitat);
	}
	
	@Transactional
	public List<Habitat> habitatPerNomeAndResponsabile(String nome, Responsabile responsabile) {
		return habitatRepository.findByNomeAndResponsabile(nome, responsabile);
	}

	@Transactional
	public List<Habitat> tutti() {
		return (List<Habitat>) habitatRepository.findAll();
	}

	@Transactional
	public Habitat habitatPerId(Long id) {
		Optional<Habitat> optional = habitatRepository.findById(id);
		if (optional.isPresent())
			return optional.get();
		else 
			return null;
	}

	@Transactional
	public boolean alreadyExists(Habitat habitat) {
		List<Habitat> habitats = this.habitatRepository.findByNomeAndResponsabile(habitat.getNome(), habitat.getResponsabile());
		if (habitats.size() > 0)
			return true;
		else 
			return false;
	}
	//
	@Transactional
	public void delete(Habitat collezione){
	//	List<Opera> opere = operaService.tutte();
		//if(!(opere.isEmpty())) {
		//for(Opera opera : opere) {
			//if(opera.getCollezione().getId() == collezione.getId()) {
				//opera.setCollezione(null);
			//}
	//	}
		//}
		this.habitatRepository.delete(collezione);
	}
}

