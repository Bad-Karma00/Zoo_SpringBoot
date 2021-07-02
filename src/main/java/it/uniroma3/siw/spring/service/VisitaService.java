package it.uniroma3.siw.spring.service;


import java.util.Collections;
import java.util.List;

import java.util.Optional;

import javax.transaction.Transactional;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import it.uniroma3.siw.spring.model.Visita;
import it.uniroma3.siw.spring.repository.VisitaRepository;

@Service
public class VisitaService {

	@Autowired
	private VisitaRepository visitaRepository; 
	
	@Transactional
	public Visita inserisci(Visita visita) {
		return visitaRepository.save(visita);
	}
	
	@Transactional
	public List<Visita> visitaPerNomeAndCognome(String nome, String cognome) {
		return visitaRepository.findByNomeAndCognome(nome, cognome);
	}

	@Transactional
	public List<Visita> tutte() {
		return (List<Visita>) visitaRepository.findAll();
	}
	
	
	@Transactional
	public List<Visita> visitePerId(List<Long> visiteID) {
		List<Visita> optional = (List<Visita>) visitaRepository.findAllById(visiteID);
		if (!(optional.isEmpty()))
			return optional;
		else 
			return Collections.emptyList();
	}
	
	@Transactional
	public Visita visitaPerId(Long id) {
		Optional<Visita> optional = visitaRepository.findById(id);
		if (optional.isPresent())
			return optional.get();
		else 
			return null;
	}

	@Transactional
	public boolean alreadyExists(Visita visita) {
		List<Visita> visite = this.visitaRepository.findByNomeAndCognome(visita.getNome(),visita.getCognome());
		if (visite.size() > 0)
			return true;
		else 
			return false;
	}
	
	@Transactional
	public void delete(Visita visita){
			this.visitaRepository.delete(visita);
		}
}
