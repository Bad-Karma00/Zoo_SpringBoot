package it.uniroma3.siw.spring.model;

import java.time.LocalDate;

import javax.persistence.*;

import org.springframework.format.annotation.DateTimeFormat;


@Entity
@Table(uniqueConstraints=@UniqueConstraint(columnNames={"nome"}))
 public class Visita implements Comparable<Visita>{
	
	@Id
	@GeneratedValue (strategy = GenerationType.IDENTITY)
	private Long Id;
	
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	@Column (nullable = false)
	private LocalDate data;
	
	@Column (nullable = false)
	private String nome;
	
	@Column (nullable = false)
	private String cognome;
   
	@ManyToOne
	private Credentials credentials;
	
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
	

	
	
	public Credentials getCredentials() {
		return credentials;
	}



	public void setCredentials(Credentials credentials) {
		this.credentials = credentials;
	}



	public int compareTo(Visita visita){
		int result;
		result = this.getNome().compareTo(visita.getNome());
		if (result == 0)
			result = this.getCognome().compareTo(visita.getCognome());
		return result;
	}

}