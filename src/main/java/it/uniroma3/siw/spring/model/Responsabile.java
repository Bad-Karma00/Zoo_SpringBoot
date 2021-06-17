package it.uniroma3.siw.spring.model;


import java.time.LocalDate;
import java.util.List;

import javax.persistence.*;

import org.springframework.format.annotation.DateTimeFormat;


@Entity
@Table(uniqueConstraints=@UniqueConstraint(columnNames={"nome", "cognome"}))
 public class Responsabile implements Comparable<Responsabile> {
	
	@Id
	private Long matricola;
	
	@Column (nullable = false)
	private String nome;
	
	@Column (nullable = false)
	private String cognome;
	
	@Column (nullable = false)
	private String email;
	
	@Column (nullable = false)
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private LocalDate dataNascita;
	
	@OneToMany (mappedBy = "responsabile", cascade= {CascadeType.PERSIST})
	private List<Habitat> habitat;

	
	public Responsabile() {
		
	}


	public Long getMatricola() {
		return matricola;
	}


	public void setMatricola(Long matricola) {
		this.matricola = matricola;
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


	public String getEmail() {
		return email;
	}


	public void setEmail(String email) {
		this.email = email;
	}


	public LocalDate getDataNascita() {
		return dataNascita;
	}


	public void setDataNascita(LocalDate dataNascita) {
		this.dataNascita = dataNascita;
	}


	public List<Habitat> getHabitat() {
		return habitat;
	}


	public void setHabitat(List<Habitat> habitat) {
		this.habitat = habitat;
	}
	
	public int compareTo(Responsabile responsabile){
		int result;
		result = this.getNome().compareTo(responsabile.getNome());
		if (result == 0)
			result = this.getCognome().compareTo(responsabile.getCognome());
		return result;
	}

}