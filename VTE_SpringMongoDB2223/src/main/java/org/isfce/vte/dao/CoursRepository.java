package org.isfce.vte.dao;

import java.util.List;

import org.isfce.vte.model.Cours;
import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

@RepositoryRestResource(collectionResourceRel = "cours", path = "cours")
public interface CoursRepository extends MongoRepository<Cours, String> {
	/* Requête REST associée
	 * http://localhost:8080/cours/search/findByNbPeriodes?nb=40
	 */
	List<Cours> findByNbPeriodes(@Param("nb") int nbPeriodes);
}
