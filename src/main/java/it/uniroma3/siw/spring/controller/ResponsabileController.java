package it.uniroma3.siw.spring.controller;

import java.util.Collections;
import java.util.Comparator;
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
import it.uniroma3.siw.spring.model.Responsabile;
import it.uniroma3.siw.spring.service.ResponsabileService;
import it.uniroma3.siw.spring.validator.ResponsabileValidator;

@Controller
public class ResponsabileController {

	@Autowired
	private ResponsabileService responsabileService;
	
	@Autowired
	private ResponsabileValidator responsabileValidator;
	
	@RequestMapping(value="/addResponsabile", method = RequestMethod.GET)
    public String addResponsabile(Model model) {
    		model.addAttribute("responsabile", new Responsabile());
    		return "InserisciResponsabile.html";
    }
    
    
    @RequestMapping(value="/rimResponsabile", method = RequestMethod.GET)
    public String rimResponsabile(Model model) {
    		model.addAttribute("responsabili", this.responsabileService.tutti());
        	return "RimuoviResponsabile.html";
    }
    
    @RequestMapping(value = "/rimozioneResponsabile", method = RequestMethod.POST)
    public String rimozioneResponsabile(@ModelAttribute("responsabile") Responsabile responsabile,
    							 Model model, BindingResult bindingResult,
								 @RequestParam(value = "responsabileSelezionato") Long responsabileID){
    		List<Responsabile> responsabili = (List<Responsabile>) responsabileService.tutti();
    		Collections.sort(responsabili);
    		Responsabile responsabileDaRim = responsabileService.responsabilePerId(responsabileID);
    		this.responsabileService.delete(responsabileDaRim);
    		model.addAttribute("responsabili", this.responsabileService.tutti());
    		return "responsabili.html";
    }
    
  
    
    @RequestMapping(value = "/responsabile/{id}", method = RequestMethod.GET)
    public String getResponsabile(@ModelAttribute("id") Long id, Model model) {
    	Responsabile responsabile=this.responsabileService.responsabilePerId(id);
    	model.addAttribute("responsabile", responsabile);
    	return "responsabile.html";
    }

    @RequestMapping(value = "/responsabile", method = RequestMethod.GET)
    public String getResponsabili(Model model) {
    		model.addAttribute("responsabili", this.responsabileService.tutti());
    		return "responsabili.html";
    }
    
    @RequestMapping(value = "/responsabile", method = RequestMethod.POST)
    public String newResponsabile(@Valid @ModelAttribute("responsabile") Responsabile responsabile, 
    									Model model, BindingResult bindingResult) {
    	
    	this.responsabileValidator.validate(responsabile, bindingResult);
        if (!bindingResult.hasErrors()) {
        	this.responsabileService.inserisci(responsabile);
            model.addAttribute("responsabili", this.responsabileService.tutti());
            return "responsabili.html";
        }
        return "InserisciResponsabile.html";
    }
    
    @RequestMapping(value="/ordineAlfabeticoResponsabile", method = RequestMethod.GET)
    public String ordineAlfabeticoResponsabile(Model model) {
    		List<Responsabile> responsabiliAlfabetico = this.responsabileService.tutti();
    		
    		if (responsabiliAlfabetico.size() > 0) {
  			  Collections.sort(responsabiliAlfabetico, new Comparator<Responsabile>() {
  			      @Override
  			      public int compare(final Responsabile responsabile1, final Responsabile responsabile2 ) {
  			    	  int differenza = 0;
  			          differenza = responsabile1.getNome().compareTo(responsabile2.getNome());
  			          if(differenza == 0) {
  			        	  return responsabile1.getCognome().compareTo(responsabile2.getCognome());
  			          }
  			          else {
  			        	  return differenza;
  			          }
  			      }
  			  });
  			}
  		model.addAttribute("responsabili", responsabiliAlfabetico);
  		
      	return "responsabili.html";
  }
    
    @RequestMapping(value="/ordineMatricola", method = RequestMethod.GET)
    public String ordineMatricola(Model model) {
    		List<Responsabile> responsabiliMatricola = this.responsabileService.tutti();
    		
    		if (responsabiliMatricola.size() > 0) {
  			  Collections.sort(responsabiliMatricola, new Comparator<Responsabile>() {
  			      @Override
  			      public int compare(final Responsabile responsabile1, final Responsabile responsabile2 ) {
  			    	return responsabile1.getMatricola().compareTo(responsabile2.getMatricola());
  			      }
  			  });
  			}
  		model.addAttribute("responsabili", responsabiliMatricola);
  		
      	return "responsabili.html";
  }
}
