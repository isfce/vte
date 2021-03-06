package com.isfce.vte.repositories;

import java.util.List;

import org.springframework.data.mongodb.repository.MongoRepository;
import org.springframework.data.repository.query.Param;
import org.springframework.data.rest.core.annotation.RepositoryRestResource;

import com.isfce.vte.model.Cours;

@RepositoryRestResource(collectionResourceRel = "cours", path = "cours")
public interface CoursRepository extends MongoRepository<Cours, String> {
	/* Requ�te REST associ�e
	 * http://localhost:8080/cours/search/findByNbPeriodes?nb=40
	 */
	List<Cours> findByNbPeriodes(@Param("nb") int nbPeriodes);
}
