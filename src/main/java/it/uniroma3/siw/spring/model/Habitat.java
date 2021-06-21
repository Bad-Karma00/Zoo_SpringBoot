package it.uniroma3.siw.spring.model;



import java.util.List;

import javax.persistence.*;


@Entity
@Table
 public class Habitat implements Comparable<Habitat> {
	
	@Id
	@GeneratedValue (strategy = GenerationType.IDENTITY)
	private Long id;
	
	@Column (nullable = false)
	private Long dimensione;
	
	@Column (nullable = false)
	private String descrizione;
	
	@Column (nullable = false)
	private String nome;
	
	@Column(nullable = true)
	private String immagineTop;
	
	@Column(nullable = true, length = 64)
    private String immagine;
	
	@ManyToOne(cascade= {CascadeType.PERSIST})
	private Responsabile responsabile;
	
	@OneToMany (mappedBy = "habitat", cascade= {CascadeType.PERSIST})
	private List<Area> aree;
	
	public Habitat() {
		
	}

	public Long getId() {
		return id;
	}

	public void setId(Long id) {
		this.id = id;
	}

	public Long getDimensione() {
		return dimensione;
	}

	public void setDimensione(Long dimensione) {
		this.dimensione = dimensione;
	}

	public String getDescrizione() {
		return descrizione;
	}

	public void setDescrizione(String descrizione) {
		this.descrizione = descrizione;
	}

	public String getNome() {
		return nome;
	}

	public void setNome(String nome) {
		this.nome = nome;
	}

	public String getImmagine() {
		return immagine;
	}

	public void setImmagine(String immagine) {
		this.immagine = immagine;
	}

	public Responsabile getResponsabile() {
		return responsabile;
	}

	public void setResponsabile(Responsabile responsabile) {
		this.responsabile = responsabile;
	}

	public List<Area> getAree() {
		return aree;
	}

	public void setAree(List<Area> aree) {
		this.aree = aree;
	}
	
	 @Transient
	    public String getPhotosImagePath() {
	        if (immagine == null || id == null) return null;
	         
	        return "/photos/" + id + nome + "/" + immagine;
	    }
	 
	 @Transient
	    public String getPhotosImagePathTop() {
	        if (immagineTop == null || id == null) return null;
	         
	        return "/photos/" + id + nome + "/" + immagineTop;
	    }
	
	public int compareTo(Habitat habitat){
		int result;
		result = this.getNome().compareTo(habitat.getNome());
		if (result == 0)
			result = this.getResponsabile().compareTo(habitat.getResponsabile());
		return result;
	}
	
	public String getImmagineTop() {
		return immagineTop;
	}

	public void setImmagineTop(String immagineTop) {
		this.immagineTop = immagineTop;
	}

	
}