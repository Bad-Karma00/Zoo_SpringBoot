package it.uniroma3.siw.spring.controller;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.text.ParseException;
import java.text.SimpleDateFormat;
import java.time.LocalDate;
import java.time.LocalDateTime;
import java.time.format.DateTimeFormatter;
import java.util.ArrayList;
import java.util.Collections;
import java.util.Comparator;
import java.util.Date;
import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.security.core.Authentication;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;
import org.apache.tomcat.util.http.fileupload.FileUtils;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import it.uniroma3.siw.spring.model.Animale;
import it.uniroma3.siw.spring.model.Area;
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

		visite.addAll(visitaService.visitePerId(visiteID));
		logger.debug(visite.toString());

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
			Authentication auth) throws ParseException {

		this.visitaValidator.validate(visita, bindingResult);
		if (!bindingResult.hasErrors()) {
			String username = auth.getName();
			logger.debug(username);
			Credentials credenziali = this.credentialsService.getCredentials(username);
			visita.setCredentials(credenziali);
			this.visitaService.inserisci(visita);
			model.addAttribute("visita", this.visitaService.tutte());
			model.addAttribute("visita", visita);
			DateTimeFormatter formatters = DateTimeFormatter.ofPattern("dd/MM/yyyy");
			model.addAttribute("dataV", visita.getData().format(formatters));
			return "VisitaConfermata.html";
		}
		return "prenota.html";
	}

	@RequestMapping(value="/ordineAlfabeticoVisita", method = RequestMethod.GET)
	public String ordineAlfabetico(Model model) {
		List<Visita> visitaAlfabetico = this.visitaService.tutte();

		if (visitaAlfabetico.size() > 0) {
			Collections.sort(visitaAlfabetico, new Comparator<Visita>() {
				@Override
				public int compare(final Visita visita1, final Visita visita2) {
					int result;
					result= visita1.getNome().compareTo(visita2.getNome());
					if(result==0) {
						result= visita1.getCognome().compareTo(visita2.getCognome());
					}
					return result;
				}    			  });
		}
		model.addAttribute("visite", visitaAlfabetico);

		return "visite.html";
	}

	@RequestMapping(value="/ordinePerData", method = RequestMethod.GET)
	public String ordinePerData(Model model) {
		List<Visita> visiteData = this.visitaService.tutte();

		if (visiteData.size() > 0) {
			Collections.sort(visiteData, new Comparator<Visita>() {
				@Override
				public int compare(final Visita visita1, final Visita visita2) {
					return visita1.getData().compareTo(visita2.getData());
				}
			});
		}
		model.addAttribute("visite", visiteData);

		return "visite.html";
	}

	@RequestMapping(value="/editVisita", method = RequestMethod.POST)
	public String selezionaVisita(Model model, Authentication auth) {
		logger.debug("editVisita");
		model.addAttribute("visita", new Visita());
		
		String username = auth.getName();
		logger.debug("Username utente:" + username); 


		Credentials credenziali = this.credentialsService.getCredentials(username);
		Long credentialID = credenziali.getId();
		logger.debug("ID credenziali:" + credentialID);

		List<Long> visiteID = visitaRepository.findByCredentialsID(credentialID);

		List<Visita> visite = new ArrayList<>();

		visite.addAll(visitaService.visitePerId(visiteID));
		logger.debug(visite.toString());
		
		model.addAttribute("visite", visite);
		return "editVisita.html";
	}

	@RequestMapping(value = "/modificaVisita", method = RequestMethod.POST)
	public String modificaVisita(@ModelAttribute("visitaSelezionata") Long visitaID,
			@ModelAttribute("data") String data,
			@ModelAttribute("nome") String nome,
			@ModelAttribute("cognome") String cognome,
			Model model, BindingResult bindingResult,
			Authentication auth){
		
		logger.debug("ID in visita: " + visitaID);
		
		Visita visitaDaMod = new Visita();
		visitaDaMod.setId(visitaID);
		visitaDaMod.setData(LocalDate.parse(data));
		visitaDaMod.setNome(nome);
		visitaDaMod.setCognome(cognome);
		
		String username = auth.getName();
		logger.debug("Username utente:" + username); 


		Credentials credenziali = this.credentialsService.getCredentials(username);
		Long credentialID = credenziali.getId();
		logger.debug("ID credenziali:" + credentialID);
		visitaDaMod.setCredentials(credenziali);

		List<Long> visiteID = visitaRepository.findByCredentialsID(credentialID);

		List<Visita> visite = new ArrayList<>();

		visite.addAll(visitaService.visitePerId(visiteID));
		logger.debug(visite.toString());
		

		visitaService.inserisci(visitaDaMod);
		
		model.addAttribute("visite", visite);
		
		return "areaPersonale.html";
	}
}
