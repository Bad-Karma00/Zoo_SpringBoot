package it.uniroma3.siw.spring.model;

import javax.persistence.*;


@Entity
@Table
public class Animale implements Comparable<Animale> {
	
	@Id
	@GeneratedValue (strategy = GenerationType.IDENTITY)
	private Long id;
		
	@Column (nullable = false)
	private String nome;
	
	@Column (nullable = false)
	private String classe;
	
	@Column (nullable = false)
	private String ordine;
	
	@ManyToOne(cascade= {CascadeType.PERSIST})
	private Area area;
	
	@Column(nullable = true, length = 64)
    private String immagine;
	

	public Animale() {
		
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

	public String getClasse() {
		return classe;
	}

	public void setClasse(String classe) {
		this.classe = classe;
	}

	public String getOrdine() {
		return ordine;
	}

	public void setOrdine(String ordine) {
		this.ordine = ordine;
	}

	public Area getArea() {
		return area;
	}

	public void setAreaAnimale(Area areaAnimale) {
		this.area = areaAnimale;
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
	         
	        return "/photos/" + id + nome + "/" + immagine;
	    }
	
	public int compareTo(Animale animale){
		int result;
		result = this.getNome().compareTo(animale.getNome());
		if (result == 0)
			result = this.getClasse().compareTo(animale.getClasse());
		return result;
	}

}