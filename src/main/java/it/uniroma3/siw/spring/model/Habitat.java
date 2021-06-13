package it.uniroma3.siw.spring.model;



import java.util.List;

import javax.persistence.*;


@Entity
@Table(uniqueConstraints=@UniqueConstraint(columnNames={"nome"}))
 public class Habitat {
	
	@Id
	@GeneratedValue (strategy = GenerationType.IDENTITY)
	private Long Id;
	
	@Column (nullable = false)
	private Long dimensione;
	
	@Column (nullable = false)
	private String descrizione;
	
	@Column (nullable = false)
	private String nome;
	
	@Column(nullable = true)
	private String immagine;
	
	@ManyToOne(cascade= {CascadeType.PERSIST})
	private Responsabile responsabile;
	
	@OneToMany (mappedBy = "areaHabitat", cascade= {CascadeType.PERSIST})
	private List<Area> aree;
	
	public Habitat() {
		
	}

	public Long getId() {
		return Id;
	}

	public void setId(Long id) {
		Id = id;
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

	
}