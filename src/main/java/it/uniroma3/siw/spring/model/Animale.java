package it.uniroma3.siw.spring.model;

import javax.persistence.*;


@Entity
@Table(uniqueConstraints=@UniqueConstraint(columnNames={"nome"}))
public class Animale {
	
	@Id
	@GeneratedValue (strategy = GenerationType.IDENTITY)
	private Long Id;
		
	@Column (nullable = false)
	private String nome;
	
	@Column (nullable = false)
	private String classe;
	
	@Column (nullable = false)
	private String ordine;
	
	@ManyToOne(cascade= {CascadeType.PERSIST})
	private Area areaAnimale;
	
	public Animale() {
		
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

	public Area getAreaAnimale() {
		return areaAnimale;
	}

	public void setAreaAnimale(Area areaAnimale) {
		this.areaAnimale = areaAnimale;
	}
	

}