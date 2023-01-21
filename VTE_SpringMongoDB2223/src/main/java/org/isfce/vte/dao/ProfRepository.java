package org.isfce.vte.dao;

import java.util.List;

import org.isfce.vte.model.Professeur;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.mongodb.repository.Query;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource(collectionResourceRel = "profs", path = "prof")
public interface ProfRepository extends MongoRepository<Professeur, String> {
	/**
	 * Requête REST associée: http://localhost:8080/prof/search/findByNom?nomP=Van
	 * Oudenhove
	 * 
	 * @param nom
	 * @return
	 */
	List<Professeur> findByNom(@Param("nomP") String nom);

	/**
	 * Requête REST associée:
	 * http://localhost:8080/prof/search/findByContactEmailRegex?email=@isfce.org
	 * 
	 * @param email
	 * @return
	 */
	
	
	List<Professeur> findByContactEmailRegex(@Param("email") String email);

	
	/**
	 * Requête REST associée
	 * http://localhost:8080/prof/search/findNomPrenomPour1Tel?tel=02/647.25.69
	 * 
	 * @param tel
	 * @return
	 */
	@Query(value = "{ 'contact.tel' : ?0 }", fields = "{ 'nom' : 1, 'prenom' : 1}")
	List<Professeur> findNomPrenomPour1Tel(@Param("tel") String tel);
}
