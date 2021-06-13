package it.uniroma3.siw.spring.model;


import java.time.LocalDateTime;
import java.util.List;

import javax.persistence.*;


@Entity
@Table(uniqueConstraints=@UniqueConstraint(columnNames={"nome"}))
 public class Area {
	
	@Id
	@GeneratedValue (strategy = GenerationType.IDENTITY)
	private Long Id;
	
	@Column (nullable = false)
	private String nome;
	
	@Column (nullable = false)
	private String descrizione;
	
	@Column (nullable = false)
	private LocalDateTime orario;
	
	@ManyToOne
	private Habitat areaHabitat;
	
	@OneToMany (mappedBy = "areaAnimale", cascade= {CascadeType.PERSIST})
	private List<Animale> animali;
	
	public Area() {
		
	}

	public Long getId() {
		return Id;
	}

	public void setId(Long id) {
		Id = id;
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

	public Habitat getAreaHabitat() {
		return areaHabitat;
	}

	public void setAreaHabitat(Habitat areaHabitat) {
		this.areaHabitat = areaHabitat;
	}

	public List<Animale> getAnimali() {
		return animali;
	}

	public void setAnimali(List<Animale> animali) {
		this.animali = animali;
	}
	


}