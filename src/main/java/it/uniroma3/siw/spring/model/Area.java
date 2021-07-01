package it.uniroma3.siw.spring.model;


import java.time.LocalDateTime;
import java.time.LocalTime;
import java.util.Collections;
import java.util.List;

import javax.persistence.*;


@Entity
@Table
 public class Area implements Comparable<Area> {
	
	@Id
	@GeneratedValue (strategy = GenerationType.IDENTITY)
	private Long id;
	
	@Column (nullable = false)
	private String nome;
	
	@Column (nullable = false)
	private String descrizione;
	
	@Column (nullable = false)
	private LocalTime orario;
	
	@Column(nullable = true, length = 64)
    private String immagine;

	@ManyToOne
	private Habitat habitat;
	
	@OneToMany (mappedBy = "area", cascade= {CascadeType.PERSIST})
	private List<Animale> animali;
	
	public Area() {
		
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getDescrizione() {
		return descrizione;
	}

	public void setDescrizione(String descrizione) {
		this.descrizione = descrizione;
	}

	public LocalTime getOrario() {
		return orario;
	}

	public void setOrario(LocalTime orario) {
		this.orario = orario;
	}

	public Habitat getHabitat() {
		return habitat;
	}

	public void setHabitat(Habitat habitat) {
		this.habitat = habitat;
	}

	public List<Animale> getAnimali() {
		return animali;
	}

	public void setAnimali(List<Animale> animali) {
		this.animali = animali;
	}
	
	
	public String getImmagine() {
		return immagine;
	}

	public void setImmagine(String immagine) {
		this.immagine = immagine;
	}
	
	 @Transient
	    public String getPhotosImagePath() {
	        if (immagine == null || id == null) return null;
	         
	        return "/photos/" + id + (nome.replaceAll("\\s+","")) + "/" + immagine;
	    }
	 
	 @Transient
	    public String getPhotosAnimale() {
		 if(animali.isEmpty()) return null;
			Collections.shuffle(animali);
			Animale animale=animali.get(0);
	        if (animale.getId() == null || animale.getImmagine() == null) return null;
	         
	        return "/photos/" + animale.getId() + animale.getNome() + "/" + animale.getImmagine();
	    }

	public int compareTo(Area area){
		int result;
		result = this.getNome().compareTo(area.getNome());
		if (result == 0)
			result = this.getHabitat().compareTo(area.getHabitat());
		return result;
	}


}