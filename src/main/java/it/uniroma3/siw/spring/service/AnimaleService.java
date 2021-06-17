package it.uniroma3.siw.spring.service;

import java.util.List;
import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import it.uniroma3.siw.spring.model.Animale;
import it.uniroma3.siw.spring.model.Area;
import it.uniroma3.siw.spring.repository.AnimaleRepository;

@Service
public class AnimaleService {

	@Autowired
	private AnimaleRepository animaleRepository; 
	
	
	@Transactional
	public Animale inserisci(Animale animale) {
		return animaleRepository.save(animale);
	}
	
	@Transactional
	public List<Animale> animalePerNomeAndArea(String nome, Area area) {
		return animaleRepository.findByNomeAndArea(nome, area);
	}

	@Transactional
	public List<Animale> tutti() {
		return (List<Animale>) animaleRepository.findAll();
	}

	@Transactional
	public Animale animalePerId(Long id) {
		Optional<Animale> optional = animaleRepository.findById(id);
		if (optional.isPresent())
			return optional.get();
		else 
			return null;
	}

	@Transactional
	public boolean alreadyExists(Animale animale) {
		List<Animale> animali = this.animaleRepository.findByNomeAndArea(animale.getNome(),animale.getArea());
		if (animali.size() > 0)
			return true;
		else 
			return false;
	}
	
	@Transactional
	public void delete(Animale animale){
		this.animaleRepository.delete(animale);
	}
}
