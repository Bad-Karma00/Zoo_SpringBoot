package it.uniroma3.siw.spring.controller;

import java.io.IOException;
import java.io.InputStream;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Collections;
import java.util.Comparator;
import java.util.List;

import javax.validation.Valid;

import org.apache.tomcat.util.http.fileupload.FileUtils;
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
import it.uniroma3.siw.spring.model.Habitat;
import it.uniroma3.siw.spring.service.AnimaleService;
import it.uniroma3.siw.spring.service.AreaService;
import it.uniroma3.siw.spring.service.HabitatService;
import it.uniroma3.siw.spring.validator.AnimaleValidator;
import it.uniroma3.siw.spring.validator.AreaValidator;

@Controller
public class AreaController {

	@Autowired
	private AreaService areaService;
	
	@Autowired
	private HabitatService habitatService;
	
	@Autowired
	private AreaValidator areaValidator;
	
	@Autowired
	private AnimaleService animaleService;
	
    
    @RequestMapping(value="/addArea", method = RequestMethod.GET)
    public String addArea(Model model) {
    		model.addAttribute("area", new Area());
    		model.addAttribute("habitats", this.habitatService.tutti());
    		return "InserisciArea.html";
    }
    
    
    @RequestMapping(value="/rimArea", method = RequestMethod.GET)
    public String rimArea(Model model) {
    		model.addAttribute("aree", this.areaService.tutte());
        	return "RimuoviArea.html";
    }
    
    @RequestMapping(value = "/rimozioneArea", method = RequestMethod.POST)
    public String rimozioneArea(@ModelAttribute("area") Area area,
    							 Model model, BindingResult bindingResult,
								 @RequestParam(value = "areaSelezionata") Long areaID) throws IOException {
    		List<Area> aree = (List<Area>) areaService.tutte();
    		Collections.sort(aree);
    		Area areaDaRim = areaService.areaPerId(areaID);
    		if(!(areaDaRim.getImmagine()==null)) {
    	   	String uploadDir ="photos/"+ areaDaRim.getId()+areaDaRim.getNome();
    		Path uploadPath = Paths.get(uploadDir);
    		FileUtils.deleteDirectory(uploadPath.toFile());;
    		}
    		this.areaService.delete(areaDaRim);
    		model.addAttribute("aree", this.areaService.tutte());
    		return "aree.html";
    }
    
    @RequestMapping(value="/editArea", method = RequestMethod.GET)
    public String selezionaArea(Model model) {
    	model.addAttribute("area", new Area());
    	model.addAttribute("aree", this.areaService.tutte());
    	model.addAttribute("habitats", this.habitatService.tutti());
        return "editArea.html";
    }
    
    @RequestMapping(value = "/modificaArea", method = RequestMethod.POST)
    public String modificaArea(@ModelAttribute("areaSelezionata") Long areaID,
    								 @ModelAttribute("nome") String nomeNuovo,
    								 @ModelAttribute("descrizione") String descrizioneNuova,
    								 @ModelAttribute("orario") LocalTime orarioNuovo,
    								 @ModelAttribute("habitatSelezionato") Long habitatID,
    								 @RequestParam(value="img") MultipartFile immagine,
    								 Model model, BindingResult bindingResult) throws IOException{
    	    
    		Area areaDaRim = areaService.areaPerId(areaID);
	     	String uploadDir1 ="photos/"+ areaDaRim.getId()+areaDaRim.getNome();
		    Path uploadPath1 = Paths.get(uploadDir1);
		    FileUtils.deleteDirectory(uploadPath1.toFile());;
        	List<Habitat> habitats = (List<Habitat>) habitatService.tutti();
        	Collections.sort(habitats);
            Habitat habitatNuovo = habitatService.habitatPerId(habitatID);
        	String fileName = StringUtils.cleanPath(immagine.getOriginalFilename());
        	Area areaNuova = new Area();
        	areaNuova.setId(areaID);
        	areaNuova.setNome(nomeNuovo);
        	areaNuova.setDescrizione(descrizioneNuova);
        	areaNuova.setOrario(orarioNuovo);
        	areaNuova.setHabitat(habitatNuovo);
        	if(!(immagine.getSize()==0)) {
        	areaNuova.setImmagine(fileName);
        	areaNuova.setImmagine(immagine.getOriginalFilename());
        	}
        	else { areaNuova.setImmagine(null);}
        	areaService.inserisci(areaNuova);
            model.addAttribute("aree", this.areaService.tutte());
            if(!(immagine.getSize()==0)) {
            String uploadDir ="photos/"+ areaNuova.getId()+areaNuova.getNome();
            
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
            }
            return "aree.html";
    }
    
    @RequestMapping(value = "/area/{id}", method = RequestMethod.GET)
    public String getArea(@ModelAttribute("id") Long id, Model model) {
    	Area area=this.areaService.areaPerId(id);
    	model.addAttribute("area", area);
    	model.addAttribute("immagine",area.getPhotosImagePath());
    	model.addAttribute("animali",animaleService.animalePerArea(area));
    	return "area.html";
    }

    @RequestMapping(value = "/area", method = RequestMethod.GET)
    public String getAree(Model model) {
    		model.addAttribute("aree", this.areaService.tutte());
    		return "aree.html";
    }
    
    @RequestMapping(value = "/area", method = RequestMethod.POST)
    public String newArea(@Valid @ModelAttribute("area") Area area, 
    									Model model, BindingResult bindingResult,
    									@RequestParam(value = "habitatSelezionato") Long habitatID,
    									@RequestParam(value="img", required=false)  MultipartFile immagine) throws IOException {
    	
    	this.areaValidator.validate(area, bindingResult);
        if (!bindingResult.hasErrors()) {

        	List<Habitat> habitats = (List<Habitat>) habitatService.tutti();
        	Collections.sort(habitats);
             Habitat habitat = habitatService.habitatPerId(habitatID);
             if(!(immagine.getSize()==0)) {
			String fileName = StringUtils.cleanPath(immagine.getOriginalFilename());
			area.setImmagine(fileName);
            area.setImmagine(immagine.getOriginalFilename());
             }
        	area.setHabitat(habitat);
        	this.areaService.inserisci(area);
            model.addAttribute("aree", this.areaService.tutte());
            if(!(immagine.getSize()==0)) {
           String uploadDir ="photos/"+ area.getId()+area.getNome();
           String fileName = StringUtils.cleanPath(immagine.getOriginalFilename());
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
            }
            return "aree.html";
        }
        model.addAttribute("habitats",this.habitatService.tutti());      
        return "InserisciArea.html";
    }
    
    
    @RequestMapping(value="/ordineAlfabeticoArea", method = RequestMethod.GET)
    public String ordineAlfabetico(Model model) {
    		List<Area> areaAlfabetico = this.areaService.tutte();
    		
    		if (areaAlfabetico.size() > 0) {
    			  Collections.sort(areaAlfabetico, new Comparator<Area>() {
    			      @Override
    			      public int compare(final Area area1, final Area area2) {
    			          return area1.getNome().compareTo(area2.getNome());
    			      }
    			  });
    			}
    		model.addAttribute("aree", areaAlfabetico);
    		
        	return "aree.html";
    }
    
    @RequestMapping(value="/ordinePerHabitat", method = RequestMethod.GET)
    public String ordinePerHabitat(Model model) {
    		List<Area> areaHabitat = this.areaService.tutte();
    		
    		if (areaHabitat.size() > 0) {
    			  Collections.sort(areaHabitat, new Comparator<Area>() {
    			      @Override
    			      public int compare(final Area area1, final Area area2) {
    			          return area1.getHabitat().getNome().compareTo(area2.getHabitat().getNome());
    			      }
    			  });
    			}
    		model.addAttribute("aree", areaHabitat);
    		
        	return "aree.html";
    }
}
