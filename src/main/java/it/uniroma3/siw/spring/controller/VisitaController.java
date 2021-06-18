package it.uniroma3.siw.spring.controller;

import java.util.Collections;
import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;

import it.uniroma3.siw.spring.model.Visita;
import it.uniroma3.siw.spring.service.VisitaService;
import it.uniroma3.siw.spring.validator.VisitaValidator;

@Controller
public class VisitaController {

	@Autowired
	private VisitaService visitaService;
	
	@Autowired
	private VisitaValidator visitaValidator;
    
    
    @RequestMapping(value="/rimVisita", method = RequestMethod.GET)
    public String rimVisita(Model model) {
    		model.addAttribute("visite", this.visitaService.tutte());
        	return "RimuoviVisita.html";//pagina per utente dove inserisce nome,cpgnome ed id della prenotazione e conferma di volerla eliminare//
    }
    
    @RequestMapping(value = "/rimozioneVisita", method = RequestMethod.POST)
    public String rimozioneVisita(@ModelAttribute("visita") Visita visita,
    							 Model model, BindingResult bindingResult,
								 @RequestParam(value = "visitaSelezionato") Long visitaID){
    		List<Visita> visite = (List<Visita>) visitaService.tutte();
    		Collections.sort(visite);
    		Visita visitaDaRim = visitaService.visitaPerId(visitaID);
    		this.visitaService.delete(visitaDaRim);
    		model.addAttribute("visite", this.visitaService.tutte());
    		return "VisitaEliminata.html";    //pagina per l'utente generico che dice la prenotazione � stata elimnata con successo//
    		}
    
 

    @RequestMapping(value = "/visita", method = RequestMethod.GET)
    public String getVisite(Model model) {
    		model.addAttribute("visite", this.visitaService.tutte());
    		return "visite.html";//pagina per admin con tutte le visite magari per data //
    }
    
    @RequestMapping(value = "/addVisita", method = RequestMethod.POST)
    public String newVisita(@Valid @ModelAttribute("visita") Visita visita, 
    								Model model, BindingResult bindingResult) {
    	
    	this.visitaValidator.validate(visita, bindingResult);
        if (!bindingResult.hasErrors()) {
        	this.visitaService.inserisci(visita);
            model.addAttribute("visita", this.visitaService.tutte());
            model.addAttribute("visita", visita);
            return "VisitaConfermata.html";//pagina di avvenuta conferma della visita che stampa tutti gli attributi incluso id//
        }
        return "prenota.html";
    }
}
