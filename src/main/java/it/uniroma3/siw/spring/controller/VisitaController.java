package it.uniroma3.siw.spring.controller;

import java.util.ArrayList;

import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import it.uniroma3.siw.spring.model.Credentials;
import it.uniroma3.siw.spring.model.Visita;
import it.uniroma3.siw.spring.repository.VisitaRepository;
import it.uniroma3.siw.spring.service.CredentialsService;
import it.uniroma3.siw.spring.service.VisitaService;
import it.uniroma3.siw.spring.validator.VisitaValidator;

@Controller
public class VisitaController {

	@Autowired
	private VisitaService visitaService;
	
	@Autowired
	private VisitaRepository visitaRepository;
	
	@Autowired
	private VisitaValidator visitaValidator;
	
	@Autowired
	private CredentialsService credentialsService;
	
	private final Logger logger = LoggerFactory.getLogger(this.getClass());
    
	@RequestMapping(value = "/delete", method = RequestMethod.POST)
    public String rimuoviVisita(@Valid @ModelAttribute("visita") Visita visita,
    							Model model, BindingResult bindingResult,
    							@RequestParam(value = "visitaId") Long visitaID,
    							Authentication auth) {
		
			logger.debug("ID passato al controller:" + visitaID);
			
			
			Visita visitaDaRimuovere = visitaService.visitaPerId(visitaID);
			visitaService.delete(visitaDaRimuovere);
			
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
	
	 @RequestMapping(value = "/visita", method = RequestMethod.GET)
	    public String getVisite(Model model) {
	    		model.addAttribute("visite", this.visitaService.tutte());
	    		return "visite.html";
	    }
    
    
    @RequestMapping(value = "/addVisita", method = RequestMethod.POST)
    public String newVisita(@Valid @ModelAttribute("visita") Visita visita, 
    								Model model, BindingResult bindingResult,
    								Authentication auth) {
    	
    	this.visitaValidator.validate(visita, bindingResult);
        if (!bindingResult.hasErrors()) {
        	
        	
        	String username = auth.getName();
        	logger.debug(username);
        	Credentials credenziali = this.credentialsService.getCredentials(username);
        	visita.setCredentials(credenziali);
        	this.visitaService.inserisci(visita);
            model.addAttribute("visita", this.visitaService.tutte());
            model.addAttribute("visita", visita);
            return "VisitaConfermata.html";
        }
        return "prenota.html";
    }
}
