package it.uniroma3.siw.spring.controller;

import org.slf4j.Logger;
import org.slf4j.LoggerFactory;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;


import it.uniroma3.siw.spring.service.HabitatService;

@Controller
public class HabitatController {
	
	@Autowired
	private HabitatService habitatService;
	
    
    private final Logger logger = LoggerFactory.getLogger(this.getClass());

	
	
	
	
	
    @RequestMapping(value = "/habitat", method = RequestMethod.GET)
    public String getCollezione(Model model) {
    		model.addAttribute("habitat", this.habitatService.tutti());    		
    		return "habitatGenerale.html";
    }
}
