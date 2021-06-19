package it.uniroma3.siw.spring.repository;


import org.springframework.data.repository.CrudRepository;
import it.uniroma3.siw.spring.model.User;

public interface UserRepository extends CrudRepository<User,Long>{
	
	public User findByNome(String nome);

	public User findByNomeAndCognome(String nome, String cognome);

	public User findByNomeOrCognome(String nome, String cognome);
}
