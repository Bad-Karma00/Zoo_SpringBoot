package it.uniroma3.siw.spring.controller;

import java.util.ArrayList;

import java.util.List;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import it.uniroma3.siw.spring.model.Credentials;
import it.uniroma3.siw.spring.model.Visita;
import it.uniroma3.siw.spring.repository.AnimaleRepository;
import it.uniroma3.siw.spring.repository.VisitaRepository;
import it.uniroma3.siw.spring.service.AnimaleService;
import it.uniroma3.siw.spring.service.CredentialsService;
import it.uniroma3.siw.spring.service.VisitaService;

@Controller
public class MainController {
	
	@Autowired
	private CredentialsService credentialsService;
	
	@Autowired
	private VisitaRepository visitaRepository;
	
	@Autowired
	private VisitaService visitaService;
	
	@Autowired
	private AnimaleRepository animaleRepository;
	
	@Autowired
	private AnimaleService animaleService;
	
	
	private final Logger logger = LoggerFactory.getLogger(this.getClass());
	
	@RequestMapping(value = {"/", "index"}, method = RequestMethod.GET)
	public String index(Model model) {
		int nAnimali = animaleRepository.contaAnimali();
		logger.debug("Animali contati: " + nAnimali);
		
		
		Long randID1 = 1 + (long) (Math.random() * (nAnimali - 0));
		logger.debug("Primo ID random: " + randID1);
		Long randID2 = 1 + (long) (Math.random() * (nAnimali - 0));
		logger.debug("Secondo ID random: " + randID2);
		Long randID3 = 1 + (long) (Math.random() * (nAnimali - 0));
		logger.debug("Terzo ID random: " + randID3);
		
		model.addAttribute("animale1", animaleService.animalePerId(randID1));
		
		model.addAttribute("animale2", animaleService.animalePerId(randID2));
		
		model.addAttribute("animale3", animaleService.animalePerId(randID3));
		
		return "index";
	}

	@RequestMapping("/informazioni")
	public String mostraInfo(Model model){
		return "informazioni.html";
	}
	
	@RequestMapping(value = "/prenota", method = RequestMethod.GET)
	public String mostraPrenota(Model model){
		model.addAttribute("visita", new Visita());
		return "prenota.html";
	}
	
	@RequestMapping(value = "/areaP", method = RequestMethod.GET)
	public String mostraAreaP(Model model, Authentication auth){
		String username = auth.getName();
		logger.debug("Username utente:" + username); 
		
		
		Credentials credenziali = this.credentialsService.getCredentials(username);
		Long credentialID = credenziali.getId();
		logger.debug("ID credenziali:" + credentialID);
		
		
		List<Long> visiteID = visitaRepository.findByCredentialsID(credentialID);
		
		List<Visita> visite = new ArrayList<>();
		
		for(Long id : visiteID) {
			visite.add(visitaService.visitaPerId(id));
			logger.debug("Visita trovata:" + visitaService.visitaPerId(id).toString());
		}
		model.addAttribute("visite", visite);
		return "areaPersonale.html";
	}
	
	
}
