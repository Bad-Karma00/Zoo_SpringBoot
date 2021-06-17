package it.uniroma3.siw.spring.controller;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Collections;
import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.util.StringUtils;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RequestParam;
import org.springframework.web.multipart.MultipartFile;

import it.uniroma3.siw.spring.model.Habitat;
import it.uniroma3.siw.spring.model.Responsabile;
import it.uniroma3.siw.spring.service.HabitatService;
import it.uniroma3.siw.spring.service.ResponsabileService;
import it.uniroma3.siw.spring.validator.HabitatValidator;

@Controller
public class HabitatController {
	
	@Autowired
	private HabitatService habitatService;
	
	@Autowired
	private HabitatValidator habitatValidator; 
	
	@Autowired
	private ResponsabileService responsabileService;
	
    
    @RequestMapping(value="/addHabitat", method = RequestMethod.GET)
    public String addHabitat(Model model) {
    		model.addAttribute("habitat", new Habitat());
    		return "InserisciHabitat.html";
    }
    
    
    @RequestMapping(value="/rimHabitat", method = RequestMethod.GET)
    public String rimHabitat(Model model) {
    		model.addAttribute("habitat", this.habitatService.tutti());
        	return "RimuoviHabitat.html";
    }
    
    @RequestMapping(value = "/rimozioneHabitat", method = RequestMethod.POST)
    public String rimozioneHabitat(@ModelAttribute("habitat") Habitat habitat,
    							 Model model, BindingResult bindingResult,
								 @RequestParam(value = "habitatSelezionato") Long habitatID) throws IOException {
    		List<Habitat> habitats = (List<Habitat>) habitatService.tutti();
    		Collections.sort(habitats);
    		Habitat habitatDaRim = habitatService.habitatPerId(habitatID);
    	   	String fileName = StringUtils.cleanPath(habitatDaRim.getImmagine());
    	   	String uploadDir ="photos/"+ habitatDaRim.getId()+habitatDaRim.getNome();
    		Path uploadPath = Paths.get(uploadDir);
    		 Path filePath = uploadPath.resolve(fileName);
    		 Files.delete(filePath);
    		   	String fileName2 = StringUtils.cleanPath(habitatDaRim.getImmagineTop());
        		 Path filePath2 = uploadPath.resolve(fileName2);
        		 Files.delete(filePath2);
    		 Files.delete(uploadPath);
    		this.habitatService.delete(habitatDaRim);
    		model.addAttribute("habitat", this.habitatService.tutti());
    		return "habitats.html";
    }
    
    @RequestMapping(value="/editHabitat", method = RequestMethod.GET)
    public String selezionaHabitat(Model model) {
    	model.addAttribute("habitat", new Habitat());
    	model.addAttribute("habitats", this.habitatService.tutti());
    	model.addAttribute("responsabili", this.responsabileService.tutti());
        return "editHabitat.html";
    }
    
    @RequestMapping(value = "/modificaHabitat", method = RequestMethod.POST)
    public String modificaAnimale(@ModelAttribute("habitatSelezionato") Long habitatID,
    								 @ModelAttribute("nome") String nomeNuovo,
    								 @ModelAttribute("dimensione") Long dimensioneNuova,
    								 @ModelAttribute("descrizione") String descrizioneNuova,
    								 @ModelAttribute("responsabileSelezionato") Long responsabileID,
    								 @RequestParam(value="img") MultipartFile immagine,
    								 @RequestParam(value="imgTop") MultipartFile immagineTop,
    								 Model model, BindingResult bindingResult) throws IOException{
    	    
    		Habitat habitatDaRim = habitatService.habitatPerId(habitatID);
	   	    String fileName1 = StringUtils.cleanPath(habitatDaRim.getImmagine());
	     	String uploadDir1 ="photos/"+ habitatDaRim.getId()+habitatDaRim.getNome();
		    Path uploadPath1 = Paths.get(uploadDir1);
		    Path filePath1 = uploadPath1.resolve(fileName1);
		    Files.delete(filePath1);
			String fileName2 = StringUtils.cleanPath(habitatDaRim.getImmagineTop());
   		   Path filePath2 = uploadPath1.resolve(fileName2);
   		    Files.delete(filePath2);
        	List<Responsabile> responsabili = (List<Responsabile>) responsabileService.tutti();
        	Collections.sort(responsabili);
         Responsabile responsabileNuovo =responsabileService.responsabilePerId(responsabileID);
        	String fileName = StringUtils.cleanPath(immagine.getOriginalFilename());
        	String fileNameTop = StringUtils.cleanPath(immagineTop.getOriginalFilename());
        	Habitat habitatNuovo = new Habitat();
        	habitatNuovo.setId(habitatID);
        	habitatNuovo.setNome(nomeNuovo);
        	habitatNuovo.setDimensione(dimensioneNuova);
        	habitatNuovo.setDescrizione(descrizioneNuova);
        	habitatNuovo.setResponsabile(responsabileNuovo);
        	habitatNuovo.setImmagine(fileName);
        	habitatNuovo.setImmagine(immagine.getOriginalFilename());
        	habitatNuovo.setImmagineTop(fileNameTop);
        	habitatNuovo.setImmagineTop(immagineTop.getOriginalFilename());
        	
        	habitatService.inserisci(habitatNuovo);
            model.addAttribute("habitat", this.habitatService.tutti());
            String uploadDir ="photos/"+ habitatNuovo.getId()+habitatNuovo.getNome();
            
            Path uploadPath = Paths.get(uploadDir);
            
            if (!Files.exists(uploadPath)) {
                Files.createDirectories(uploadPath);
            }
             
            try (InputStream inputStream = immagine.getInputStream()) {
                Path filePath = uploadPath.resolve(fileName);
                Files.copy(inputStream, filePath, StandardCopyOption.REPLACE_EXISTING);
            } catch (IOException ioe) {        
                throw new IOException("Salvataggio non riuscito: " + fileName, ioe);
            }    
            
            try (InputStream inputStream = immagineTop.getInputStream()) {
                Path filePath = uploadPath.resolve(fileNameTop);
                Files.copy(inputStream, filePath, StandardCopyOption.REPLACE_EXISTING);
            } catch (IOException ioe) {        
                throw new IOException("Salvataggio non riuscito: " + fileNameTop, ioe);
            } 
            return "habitats.html";
    }
    
    @RequestMapping(value = "/habitat/{id}", method = RequestMethod.GET)
    public String getHabitat(@ModelAttribute("id") Long id, Model model) {
Habitat habitat=this.habitatService.habitatPerId(id);
    	model.addAttribute("habitat", habitat);
    	return "habitat.html";
    }

    @RequestMapping(value = "/habitat", method = RequestMethod.GET)
    public String getHabitats(Model model) {
    		model.addAttribute("habitat", this.habitatService.tutti());
    		return "habitats.html";
    }
    
    @RequestMapping(value = "/habitat", method = RequestMethod.POST)
    public String newHabitat(@Valid @ModelAttribute("habitat") Habitat habitat, 
    									Model model, BindingResult bindingResult,
    									@RequestParam(value = "responsabileSelezionato") Long responsabileID,
    									@RequestParam(value="img", required=false)  MultipartFile immagine,
    									@RequestParam(value="imgTop", required=false)  MultipartFile immagineTop
    									) throws IOException {
    	
    	this.habitatValidator.validate(habitat, bindingResult);
        if (!bindingResult.hasErrors()) {

        	List<Responsabile> responsabili = (List<Responsabile>) responsabileService.tutti();
        	Collections.sort(responsabili);
           Responsabile responsabile= responsabileService.responsabilePerId(responsabileID);
			String fileName = StringUtils.cleanPath(immagine.getOriginalFilename());
			String fileName2 = StringUtils.cleanPath(immagineTop.getOriginalFilename());
			habitat.setImmagine(fileName);
            habitat.setImmagine(immagine.getOriginalFilename());
			habitat.setImmagine(fileName2);
            habitat.setImmagine(immagineTop.getOriginalFilename());
        	habitat.setResponsabile(responsabile);
        	this.habitatService.inserisci(habitat);
            model.addAttribute("habitat", this.habitatService.tutti());
           String uploadDir ="photos/"+ habitat.getId()+habitat.getNome();
            
           Path uploadPath = Paths.get(uploadDir);
           
           if (!Files.exists(uploadPath)) {
               Files.createDirectories(uploadPath);
           }
            
           try (InputStream inputStream = immagine.getInputStream()) {
               Path filePath = uploadPath.resolve(fileName);
               Files.copy(inputStream, filePath, StandardCopyOption.REPLACE_EXISTING);
           } catch (IOException ioe) {        
               throw new IOException("Salvataggio non riuscito: " + fileName, ioe);
           }  
           try (InputStream inputStream = immagineTop.getInputStream()) {
               Path filePath = uploadPath.resolve(fileName2);
               Files.copy(inputStream, filePath, StandardCopyOption.REPLACE_EXISTING);
           } catch (IOException ioe) {        
               throw new IOException("Salvataggio non riuscito: " + fileName2, ioe);
           } 
            return "habitats.html";
        }
        return "InserisciHabitat.html";
    }
	
	
	
	
    @RequestMapping(value = "/habitatGenerale", method = RequestMethod.GET)
    public String getHabitat(Model model) {
    		model.addAttribute("habitat", this.habitatService.tutti());    		
    		return "habitatGenerale.html";
    }
}
