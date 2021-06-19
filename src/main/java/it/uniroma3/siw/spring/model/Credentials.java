package it.uniroma3.siw.spring.model;

import java.util.List;

import javax.persistence.CascadeType;

import javax.persistence.Column;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.persistence.OneToOne;
import javax.persistence.Table;

@Entity
@Table(name = "credentials")
public class Credentials {
	
	
	
	public static final String ADMIN_ROLE = "ADMIN";
	public static final String USER_ROLE = "USER";
	
	
	@Id
	@GeneratedValue (strategy = GenerationType.IDENTITY)
	private Long id;
	
	@Column (nullable = false, unique = true)
	private String username;
	

	@Column (nullable = false)
	private String password;
	

	@Column (nullable = false)
	String ruolo;
	
	@Column (nullable = false)
	Boolean enabled;
	
	@OneToOne(cascade = CascadeType.ALL)
	private User user;
	
	@OneToMany (mappedBy = "credentials", cascade= {CascadeType.PERSIST})
	private List<Visita> visite;
	
	public String getUsername() {
		return username;
	}
	public void setUsername(String username) {
		this.username = username;
	}
	public String getPassword() {
		return password;
	}
	public void setPassword(String password) {
		this.password = password;
	}
	public String getRuolo() {
		return ruolo;
	}
	public void setRuolo(String ruolo) {
		this.ruolo = ruolo;
	}
	public Long getId() {
		return id;
	}
	public void setId(Long id) {
		this.id = id;
	}
	public User getUser() {
		return user;
	}
	public void setUser(User user) {
		this.user = user;
	}
	
	public Boolean getEnabled() {
		return enabled;
	}
	public void setEnabled(Boolean enabled) {
		this.enabled = enabled;
	}
	public List<Visita> getVisite() {
		return visite;
	}
	public void setVisite(List<Visita> visite) {
		this.visite = visite;
	}
	
	

}
