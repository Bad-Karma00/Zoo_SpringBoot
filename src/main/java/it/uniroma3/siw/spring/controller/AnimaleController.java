package it.uniroma3.siw.spring.controller;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.Collections;
import java.util.Comparator;
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

import it.uniroma3.siw.spring.model.Animale;
import it.uniroma3.siw.spring.model.Area;
import it.uniroma3.siw.spring.service.AnimaleService;
import it.uniroma3.siw.spring.service.AreaService;
import it.uniroma3.siw.spring.validator.AnimaleValidator;


@Controller
public class AnimaleController {

	@Autowired
	private AnimaleService animaleService;
	
	@Autowired
	private AreaService areaService;
	
	@Autowired
	private AnimaleValidator animaleValidator;
	
    
    @RequestMapping(value="/addAnimale", method = RequestMethod.GET)
    public String addAnimale(Model model) {
    		model.addAttribute("animale", new Animale());
    		model.addAttribute("aree", this.areaService.tutte());
    		return "InserisciAnimale.html";
    }
    
    
    @RequestMapping(value="/rimAnimale", method = RequestMethod.GET)
    public String rimAnimale(Model model) {
    		model.addAttribute("animali", this.animaleService.tutti());
        	return "RimuoviAnimale.html";
    }
    
    @RequestMapping(value = "/rimozioneAnimale", method = RequestMethod.POST)
    public String rimozioneAnimale(@ModelAttribute("animale") Animale animale,
    							 Model model, BindingResult bindingResult,
								 @RequestParam(value = "animaleSelezionato") Long animaleID) throws IOException {
    		List<Animale> animali = (List<Animale>) animaleService.tutti();
    		Collections.sort(animali);
    		Animale animaleDaRim = animaleService.animalePerId(animaleID);
    	   	String fileName = StringUtils.cleanPath(animaleDaRim.getImmagine());
    	   	String uploadDir ="photos/"+ animaleDaRim.getId()+animaleDaRim.getNome();
    		Path uploadPath = Paths.get(uploadDir);
    		 Path filePath = uploadPath.resolve(fileName);
    		 Files.delete(filePath);
    		 Files.delete(uploadPath);
    		this.animaleService.delete(animaleDaRim);
    		model.addAttribute("animali", this.animaleService.tutti());
    		return "animali.html";
    }
    
    @RequestMapping(value="/editAnimale", method = RequestMethod.GET)
    public String selezionaAnimale(Model model) {
    	model.addAttribute("animale", new Animale());
    	model.addAttribute("animali", this.animaleService.tutti());
    	model.addAttribute("aree", this.areaService.tutte());
        return "editAnimale.html";
    }
    
    @RequestMapping(value = "/modificaAnimale", method = RequestMethod.POST)
    public String modificaAnimale(@ModelAttribute("animaleSelezionato") Long animaleID,
    								 @ModelAttribute("nome") String nomeNuovo,
    								 @ModelAttribute("classe") String classeNuova,
    								 @ModelAttribute("ordine") String ordineNuovo,
    								 @ModelAttribute("areaSelezionata") Long areaID,
    								 @RequestParam(value="img") MultipartFile immagine,
    								 Model model, BindingResult bindingResult) throws IOException{
    	    
    		Animale animaleDaRim = animaleService.animalePerId(animaleID);
	   	    String fileName1 = StringUtils.cleanPath(animaleDaRim.getImmagine());
	     	String uploadDir1 ="photos/"+ animaleDaRim.getId()+animaleDaRim.getNome();
		    Path uploadPath1 = Paths.get(uploadDir1);
		    Path filePath1 = uploadPath1.resolve(fileName1);
		    Files.delete(filePath1);
        	List<Animale> animali = (List<Animale>) animaleService.tutti();
        	Collections.sort(animali);
            Area areaNuova = areaService.areaPerId(areaID);
        	String fileName = StringUtils.cleanPath(immagine.getOriginalFilename());
        	Animale animaleNuovo = new Animale();
        	animaleNuovo.setId(animaleID);
        	animaleNuovo.setNome(nomeNuovo);
        	animaleNuovo.setClasse(classeNuova);
        	animaleNuovo.setOrdine(ordineNuovo);
        	animaleNuovo.setAreaAnimale(areaNuova);
        	animaleNuovo.setImmagine(fileName);
        	animaleNuovo.setImmagine(immagine.getOriginalFilename());
        	
        	animaleService.inserisci(animaleNuovo);
            model.addAttribute("animali", this.animaleService.tutti());
            String uploadDir ="photos/"+ animaleNuovo.getId()+animaleNuovo.getNome();
            
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
            return "animali.html";
    }
    
    @RequestMapping(value = "/animale/{id}", method = RequestMethod.GET)
    public String getAnimale(@ModelAttribute("id") Long id, Model model) {
    	Animale animale=this.animaleService.animalePerId(id);
    	model.addAttribute("animale", animale);
    	return "animale.html";
    }

    @RequestMapping(value = "/animale", method = RequestMethod.GET)
    public String getAnimali(Model model) {
    		model.addAttribute("animali", this.animaleService.tutti());
    		return "animali.html";
    }
    
    @RequestMapping(value = "/animale", method = RequestMethod.POST)
    public String newOpera(@Valid @ModelAttribute("animale") Animale animale, 
    									Model model, BindingResult bindingResult,
    									@RequestParam(value = "areaSelezionata") Long areaID,
    									@RequestParam(value="img", required=false)  MultipartFile immagine) throws IOException {
    	
    	this.animaleValidator.validate(animale, bindingResult);
        if (!bindingResult.hasErrors()) {

        	List<Area> aree = (List<Area>) areaService.tutte();
        	Collections.sort(aree);
        	Area area = areaService.areaPerId(areaID);
			String fileName = StringUtils.cleanPath(immagine.getOriginalFilename());
			animale.setImmagine(fileName);
            animale.setImmagine(immagine.getOriginalFilename());
        	animale.setAreaAnimale(area);
        	this.animaleService.inserisci(animale);
            model.addAttribute("animali", this.animaleService.tutti());
           String uploadDir ="photos/"+ animale.getId()+animale.getNome();
            
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
            return "animali.html";
        }
        return "InserisciAnimale.html";
    }
    
    @RequestMapping(value="/ordineAlfabetico", method = RequestMethod.GET)
    public String ordineAlfabetico(Model model) {
    		List<Animale> animaleAlfabetico = this.animaleService.tutti();
    		
    		if (animaleAlfabetico.size() > 0) {
    			  Collections.sort(animaleAlfabetico, new Comparator<Animale>() {
    			      @Override
    			      public int compare(final Animale animale1, final Animale animale2) {
    			          return animale1.getNome().compareTo(animale2.getNome());
    			      }
    			  });
    			}
    		model.addAttribute("animali", animaleAlfabetico);
    		
        	return "animali.html";
    }
    
    @RequestMapping(value="/ordinePerArea", method = RequestMethod.GET)
    public String ordinePerArea(Model model) {
    		List<Animale> animaleArea = this.animaleService.tutti();
    		
    		if (animaleArea.size() > 0) {
    			  Collections.sort(animaleArea, new Comparator<Animale>() {
    			      @Override
    			      public int compare(final Animale animale1, final Animale animale2) {
    			          return animale1.getArea().getNome().compareTo(animale2.getArea().getNome());
    			      }
    			  });
    			}
    		model.addAttribute("animali", animaleArea);
    		
        	return "animali.html";
    }
}
