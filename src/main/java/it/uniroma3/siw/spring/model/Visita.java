package it.uniroma3.siw.spring.model;

import java.time.LocalDate;

import javax.persistence.*;


@Entity
@Table(uniqueConstraints=@UniqueConstraint(columnNames={"nome"}))
 public class Visita {
	
	@Id
	@GeneratedValue (strategy = GenerationType.IDENTITY)
	private Long Id;
	
	@Column (nullable = false)
	private LocalDate data;
	
	@Column (nullable = false)
	private String nome;
	
	@Column (nullable = false)
	private String cognome;
	

	
	public Visita() {
		
	}



	public Long getId() {
		return Id;
	}



	public void setId(Long id) {
		Id = id;
	}



	public LocalDate getData() {
		return data;
	}



	public void setData(LocalDate data) {
		this.data = data;
	}



	public String getNome() {
		return nome;
	}



	public void setNome(String nome) {
		this.nome = nome;
	}



	public String getCognome() {
		return cognome;
	}



	public void setCognome(String cognome) {
		this.cognome = cognome;
	}

}