package it.uniroma3.siw.spring.model;


import java.time.LocalDateTime;
import java.util.List;

import javax.persistence.*;


@Entity
@Table(uniqueConstraints=@UniqueConstraint(columnNames={"nome"}))
 public class Area implements Comparable<Area> {
	
	@Id
	@GeneratedValue (strategy = GenerationType.IDENTITY)
	private Long id;
	
	@Column (nullable = false)
	private String nome;
	
	@Column (nullable = false)
	private String descrizione;
	
	@Column (nullable = false)
	private LocalDateTime orario;
	
	@Column(nullable = true, length = 64)
    private String immagine;
	
	@ManyToOne
	private Habitat habitat;
	
	@OneToMany (mappedBy = "areaAnimale", cascade= {CascadeType.PERSIST})
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

	public LocalDateTime getOrario() {
		return orario;
	}

	public void setOrario(LocalDateTime orario) {
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
	
	 @Transient
	    public String getPhotosImagePath() {
	        if (immagine == null || id == null) return null;
	         
	        return "/photos/" + id + nome + "/" + immagine;
	    }

	public int compareTo(Area area){
		int result;
		result = this.getNome().compareTo(area.getNome());
		if (result == 0)
			result = this.getHabitat().compareTo(area.getHabitat());
		return result;
	}


}