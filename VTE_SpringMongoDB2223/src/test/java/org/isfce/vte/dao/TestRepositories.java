package org.isfce.vte.dao;



import static org.junit.jupiter.api.Assertions.assertEquals;
import static org.junit.jupiter.api.Assertions.assertTrue;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.isfce.vte.model.Contact;
import org.isfce.vte.model.Cours;
import org.isfce.vte.model.Professeur;
import org.junit.jupiter.api.Test;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.data.mongodb.core.query.Criteria;

import lombok.Data;

/**
 * 
 * Test unitaire pour contrôler le bon fonctionnement de 
 * -Les daos prof et cours
 * -Le miniDSL 
 * -mongoTemplate et le module d'aggrégation
 * 
 * @author Didier
 */
@SpringBootTest
public class TestRepositories {
	/**
	 * 
	 * @author Didier
	 * 
	 *         Classe pour recevoir le résultat d'un étage d'aggrégation avec un
	 *         group by Affiche le nombre de cours pour un nombre de périodes Hyp:
	 *         Bd avec 18 cours et 7 profs. Cf les données pour initialiser ces 2
	 *         collections de MongoDB
	 */
	@Data
	private class ResultAggr2 {
		@Field("_id")
		int nbPeriodes;// _id sera encodé dans ce champ
		int nbCours;
	}

	// Injection des dao et mongoTemplate
	@Autowired
	private ProfRepository daoProfs;
	@Autowired
	private CoursRepository daoCours;
	@Autowired
	private MongoTemplate mongoTemplate;

	@Test
	/**
	 * Test le chargement d'un cours et le nbr de cours
	 */
	public void testCours() {
		assertEquals(18L, daoCours.count());
		Optional<Cours> vte = daoCours.findById("IVTE");
		vte.ifPresent((c) -> System.out.println(c));
		assertTrue(vte.isPresent());
	}

	/**
	 * test le chargement de VO 
	 * vérifie qu'il a 5 cours 
	 * vérifie le nbr de profs : 7
	 */
	@Test
	public void testProfVO() {
		Optional<Professeur> vo = daoProfs.findById("VO");
		assertTrue(vo.isPresent());
		assertEquals("02/647.25.69", vo.get().getContact().getTel());
		List<Cours> cours = vo.get().getCours();
		assertEquals(5, cours.size());
		assertEquals(7, daoProfs.count());
	}

	@Test
	public void testAjoutCoursProf() {
		// Récupération de 2 cours existants
		Optional<Cours> vte = daoCours.findById("IVTE");
		Optional<Cours> ibd = daoCours.findById("IIBD");
		List<Cours> cours = new ArrayList<>();
		vte.ifPresent(c -> cours.add(c));
		ibd.ifPresent(c -> cours.add(c));

		Professeur prof = new Professeur("TRUC", "Truc", "truc", new Contact("truc@isfce.org", "02/647.25.69"), false,
				new Date());
		prof.setCours(cours);
		// sauvegarde
		daoProfs.save(prof);

		// Récupère le prof

		Optional<Professeur> prof2 = daoProfs.findById("TRUC");
		assertTrue(prof2.isPresent());
		assertEquals(prof, prof2.get());

		daoProfs.delete(prof2.get());

	}

	@Test
	public void testAjoutProfNewCours() {
		try {// Création d'une liste avec un nouveau cours
			Cours c1 = new Cours("III", "Test", 10, 1, false);
			List<Cours> cours = new ArrayList<>();
			cours.add(c1);
			// Création d'un nouveau prof avec ce cours
			Professeur prof = new Professeur("TRUC", "Truc", "truc", new Contact("truc@isfce.org", "02/647.25.69"),
					false, new Date());
			prof.setCours(cours);

			// sauvegarde du cours et ensuite du prof
			daoCours.save(c1);
			daoProfs.save(prof);

			// Récupère le prof et regarde si OK
			Optional<Professeur> prof2 = daoProfs.findById("TRUC");
			assertTrue(prof2.isPresent());
			assertEquals(prof, prof2.get());

			// Suppression d'un cours au professeur et sauvegarde
			prof2.get().getCours().remove(c1);
			daoProfs.save(prof2.get());
			Optional<Professeur> prof3 = daoProfs.findById("TRUC");
			assertTrue(prof3.isPresent());
			assertEquals(prof2.get(), prof3.get());

			// vérifie que le cours existe tjs
			Optional<Cours> c2 = daoCours.findById("III");
			assertTrue(c2.isPresent());
			assertEquals(c1, c2.get());

		} finally {
			// Supprimer les cours et professeur de tests
			daoProfs.deleteById("TRUC");
			daoCours.deleteById("III");
		}
	}

	@Test
	public void testMiniDSL() {
		//findByNbPeriodes
		List<Cours> cours = daoCours.findByNbPeriodes(40);
		assertEquals(3, cours.size());
		//findByNom
		List<Professeur> profs = daoProfs.findByNom("Van Oudenhove");
		assertEquals(1, profs.size());
		profs.clear();
		//findBy une expression régulière
		profs = daoProfs.findByContactEmailRegex("isfce.org$");
		assertEquals(3, profs.size());
		System.out.println(profs);
		//Chargement que des champs nom et prenom
		profs = daoProfs.findNomPrenomPour1Tel("02/647.25.69");
		System.out.println(profs);
		assertEquals(3, profs.size());
		//Teste la pagination
		Page<Cours> pCours = daoCours.findAll(PageRequest.of(1, 3));
		assertEquals(3, pCours.getNumberOfElements());

		
		// Exemple d'utilisation du module aggregate
		Aggregation agr;
		agr = Aggregation.newAggregation(Professeur.class,
				Aggregation.match(Criteria.where("contact.tel").in("02/647.25.69")),
				Aggregation.project("nom", "prenom"));
		// Exécution du module
		AggregationResults<Professeur> results = mongoTemplate.aggregate(agr, "prof", Professeur.class);

		System.out.println(results.getMappedResults());
		assertEquals(3, results.getMappedResults().size());
		
		// Exemple2 avec un group
		 
		agr = Aggregation.newAggregation(Cours.class, Aggregation.group("nbPeriodes").count().as("nbCours"));
		// Exécution du module
		AggregationResults<ResultAggr2> results2 = mongoTemplate.aggregate(agr, "cours", ResultAggr2.class);
		System.out.println("--------Aggre2-------");
		System.out.println(results2.getMappedResults());
		assertEquals(6, results2.getMappedResults().size());

	}

}
