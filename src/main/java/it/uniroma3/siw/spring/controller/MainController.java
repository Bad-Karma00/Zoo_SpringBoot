package it.uniroma3.siw.spring.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;

@Controller
public class MainController {

	@RequestMapping("/informazioni")
	public String mostraInfo(Model model){
		return "informazioni.html";
	}
	
	@RequestMapping("/prenota")
	public String mostraPrenota(Model model){
		return "prenota.html";
	}
	
	
}
