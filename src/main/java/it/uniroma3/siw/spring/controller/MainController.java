package it.uniroma3.siw.spring.controller;

import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;

import it.uniroma3.siw.spring.model.Visita;

@Controller
public class MainController {

    @RequestMapping(value = {"/", "index"}, method = RequestMethod.GET)
    public String index(Model model) {
        return "index.html";
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


}