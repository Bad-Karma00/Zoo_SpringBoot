package it.uniroma3.siw.spring.service;

import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import it.uniroma3.siw.spring.model.Habitat;
import it.uniroma3.siw.spring.model.Responsabile;
import it.uniroma3.siw.spring.repository.ResponsabileRepository;


@Service
public class ResponsabileService {

	@Autowired
	private ResponsabileRepository responsabileRepository; 
	
	@Autowired
	private HabitatService habitatService;
	
	@Transactional
	public Responsabile inserisci(Responsabile responsabile) {
		return responsabileRepository.save(responsabile);
	}
	
	@Transactional
	public List<Responsabile> responsabiliPerNomeAndCognome(String nome, String cognome) {
		return responsabileRepository.findByNomeAndCognome(nome, cognome);
	}

	@Transactional
	public List<Responsabile> tutti() {
		return (List<Responsabile>) responsabileRepository.findAll();
	}

	@Transactional
	public Responsabile responsabilePerId(Long id) {
		Optional<Responsabile> optional = responsabileRepository.findById(id);
		if (optional.isPresent())
			return optional.get();
		else 
			return null;
	}

	@Transactional
	public boolean alreadyExists(Responsabile responsabile) {
		List<Responsabile> responsabili = this.responsabileRepository.findByNomeAndCognome(responsabile.getNome(), responsabile.getCognome());
		if (responsabili.size() > 0)
			return true;
		else 
			return false;
	}
	
	@Transactional
	public void delete(Responsabile collezione){
		List<Habitat> habitats = habitatService.tutti();
		if(!(habitats.isEmpty())) {
			for(Habitat habitat : habitats) {
								habitat.setResponsabile(null);
				}
		}
		
			this.responsabileRepository.delete(collezione);
		}
}
