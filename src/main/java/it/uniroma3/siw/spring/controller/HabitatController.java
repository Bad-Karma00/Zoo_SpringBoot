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

import it.uniroma3.siw.spring.model.Responsabile;
import it.uniroma3.siw.spring.model.Habitat;
import it.uniroma3.siw.spring.model.Responsabile;
import it.uniroma3.siw.spring.service.AreaService;
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
	
	@Autowired
	private AreaService areaService;
	
    
    @RequestMapping(value="/addHabitat", method = RequestMethod.GET)
    public String addHabitat(Model model) {
    		model.addAttribute("habitat", new Habitat());
    		model.addAttribute("responsabili", this.responsabileService.tutti());
    		return "InserisciHabitat.html";
    }
    
    
    @RequestMapping(value="/rimHabitat", method = RequestMethod.GET)
    public String rimHabitat(Model model) {
    		model.addAttribute("habitats", this.habitatService.tutti());
        	return "RimuoviHabitat.html";
    }
    
    @RequestMapping(value = "/rimozioneHabitat", method = RequestMethod.POST)
    public String rimozioneHabitat(@ModelAttribute("habitat") Habitat habitat,
    							 Model model, BindingResult bindingResult,
								 @RequestParam(value = "habitatSelezionato") Long habitatID) throws IOException {
    		List<Habitat> habitats = (List<Habitat>) habitatService.tutti();
    		Collections.sort(habitats);
    		Habitat habitatDaRim = habitatService.habitatPerId(habitatID);
    	   	String uploadDir ="photos/"+ habitatDaRim.getId()+habitatDaRim.getNome();
    		Path uploadPath = Paths.get(uploadDir);
        		 FileUtils.deleteDirectory(uploadPath.toFile());;
    		this.habitatService.delete(habitatDaRim);
    		model.addAttribute("habitats", this.habitatService.tutti());
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
	     	String uploadDir1 ="photos/"+ habitatDaRim.getId()+habitatDaRim.getNome();
		    Path uploadPath1 = Paths.get(uploadDir1);
   		  FileUtils.deleteDirectory(uploadPath1.toFile());;
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
            model.addAttribute("habitats", this.habitatService.tutti());
            String uploadDir ="photos/"+ habitatNuovo.getId()+habitatNuovo.getNome();
            
            Path uploadPath = Paths.get(uploadDir);
            
            if (!Files.exists(uploadPath)) {
                Files.createDirectories(uploadPath);
            }
             
            
            try (InputStream inputStream = immagine.getInputStream()) {
         	   try (InputStream inputStream2 = immagineTop.getInputStream()) {
                    Path filePath2 = uploadPath.resolve(fileNameTop);
                    Files.copy(inputStream2, filePath2, StandardCopyOption.REPLACE_EXISTING);
                } catch (IOException ioe) {        
                    throw new IOException("Salvataggio non riuscito: " + fileNameTop, ioe);
                } 
                Path filePath = uploadPath.resolve(fileName);
                Files.copy(inputStream, filePath, StandardCopyOption.REPLACE_EXISTING);
            } catch (IOException ioe) {        
                throw new IOException("Salvataggio non riuscito: " + fileName, ioe);
            }  
            return "habitats.html";
    }
    
    @RequestMapping(value = "/habitat/{id}", method = RequestMethod.GET)
    public String getHabitat(@ModelAttribute("id") Long id, Model model) {
     Habitat habitat=this.habitatService.habitatPerId(id);
    	model.addAttribute("habitat", habitat);
    	model.addAttribute("immagineT",habitat.getPhotosImagePathTop());
    	model.addAttribute("immagine",habitat.getPhotosImagePath());
    	model.addAttribute("aree",this.areaService.areaHabitat(habitat));
    	return "habitat.html";
    }

    @RequestMapping(value = "/habitat", method = RequestMethod.GET)
    public String getHabitats(Model model) {
    		model.addAttribute("habitats", this.habitatService.tutti());
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
			habitat.setImmagineTop(fileName2);
            habitat.setImmagineTop(immagineTop.getOriginalFilename());
        	habitat.setResponsabile(responsabile);
        	this.habitatService.inserisci(habitat);
            model.addAttribute("habitats", this.habitatService.tutti());
           String uploadDir ="photos/"+ habitat.getId()+habitat.getNome();
            
           Path uploadPath = Paths.get(uploadDir);
           
           if (!Files.exists(uploadPath)) {
               Files.createDirectories(uploadPath);
           }
            
           try (InputStream inputStream = immagine.getInputStream()) {
        	   try (InputStream inputStream2 = immagineTop.getInputStream()) {
                   Path filePath2 = uploadPath.resolve(fileName2);
                   Files.copy(inputStream2, filePath2, StandardCopyOption.REPLACE_EXISTING);
               } catch (IOException ioe) {        
                   throw new IOException("Salvataggio non riuscito: " + fileName2, ioe);
               } 
               Path filePath = uploadPath.resolve(fileName);
               Files.copy(inputStream, filePath, StandardCopyOption.REPLACE_EXISTING);
           } catch (IOException ioe) {        
               throw new IOException("Salvataggio non riuscito: " + fileName, ioe);
           }  
            return "habitats.html";
        }
        return "InserisciHabitat.html";
    }
	
    @RequestMapping(value="/ordineAlfabeticoHabitat", method = RequestMethod.GET)
    public String ordineAlfabeticoHabitat(Model model) {
    		List<Habitat> habitatAlfabetico = this.habitatService.tutti();
    		
    		if (habitatAlfabetico.size() > 0) {
    			  Collections.sort(habitatAlfabetico, new Comparator<Habitat>() {
    			      @Override
    			      public int compare(final Habitat habitat1, final Habitat habitat2) {
    			          return habitat1.getNome().compareTo(habitat2.getNome());
    			      }
    			  });
    			}
    		model.addAttribute("habitats", habitatAlfabetico);
    		
        	return "habitats.html";
    }
    
    @RequestMapping(value="/ordinePerResponsabile", method = RequestMethod.GET)
    public String ordinePerResponsabile(Model model) {
    		List<Habitat> responsabileHabitat = this.habitatService.tutti();
    		
    		if (responsabileHabitat.size() > 0) {
    			  Collections.sort(responsabileHabitat, new Comparator<Habitat>() {
    				  @Override
    			      public int compare(final Habitat habitat1, final Habitat habitat2) {
    					  int result;
    			          result= habitat1.getResponsabile().getNome().compareTo(habitat2.getResponsabile().getNome());
    			          if(result==0) {
    			        	  result= habitat1.getResponsabile().getCognome().compareTo(habitat2.getResponsabile().getCognome());
    			          }
    			          return result;
    			      }
    			  });
    			}
    		model.addAttribute("habitats", responsabileHabitat);
    		
        	return "habitats.html";
    }
}
