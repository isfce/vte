package com.isfce.vte.repositories;

import static org.junit.Assert.assertEquals;
import static org.junit.Assert.assertTrue;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;
import java.util.Optional;

import org.junit.Test;
import org.junit.runner.RunWith;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.boot.test.context.SpringBootTest;
import org.springframework.data.annotation.Id;
import org.springframework.data.domain.Page;
import org.springframework.data.domain.PageRequest;
import org.springframework.data.mongodb.core.MongoTemplate;
import org.springframework.data.mongodb.core.aggregation.Aggregation;
import org.springframework.data.mongodb.core.aggregation.AggregationResults;
import org.springframework.data.mongodb.core.mapping.Field;
import org.springframework.data.mongodb.core.query.Criteria;
import org.springframework.test.context.junit4.SpringRunner;

import com.isfce.vte.model.Contact;
import com.isfce.vte.model.Cours;
import com.isfce.vte.model.Professeur;

import lombok.Data;

/**
 * 
 * Test unitaire pour contr�ler le bon fonctionnement de 
 * -Les daos prof et cours
 * -Le miniDSL 
 * -mongoTemplate et le module d'aggr�gation
 * 
 * @author Didier
 */
@RunWith(SpringRunner.class)
@SpringBootTest
public class TestRepositories {
	/**
	 * 
	 * @author Didier
	 * 
	 *         Classe pour recevoir le r�sultat d'un �tage d'aggr�gation avec un
	 *         group by Affiche le nombre de cours pour un nombre de p�riodes Hyp:
	 *         Bd avec 18 cours et 5 profs. Cf les donn�es pour initialiser ces 2
	 *         collections de MongoDB
	 */
	@Data
	private class ResultAggr2 {
		@Field("_id")
		int nbPeriodes;// _id sera encod� dans ce champ
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
		assertEquals(18, daoCours.count());
		Optional<Cours> vte = daoCours.findById("IVTE");
		vte.ifPresent((c) -> System.out.println(c));
		assertTrue(vte.isPresent());
	}

	/**
	 * test le chargement de VO 
	 * v�rifie qu'il a 3 cours 
	 * v�rifie le nbr de profs � 5
	 */
	@Test
	public void testProfVO() {
		Optional<Professeur> vo = daoProfs.findById("VO");
		assertTrue(vo.isPresent());
		assertEquals("02/647.25.69", vo.get().getContact().getTel());
		List<Cours> cours = vo.get().getCours();
		assertEquals(3, cours.size());
		assertEquals(5, daoProfs.count());
	}

	@Test
	public void testAjoutCoursProf() {
		// R�cup�ration de 2 cours existants
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

		// R�cup�re le prof

		Optional<Professeur> prof2 = daoProfs.findById("TRUC");
		assertTrue(prof2.isPresent());
		assertEquals(prof, prof2.get());

		daoProfs.delete(prof2.get());

	}

	@Test
	public void testAjoutProfNewCours() {
		try {// Cr�ation d'une liste avec un nouveau cours
			Cours c1 = new Cours("III", "Test", 10, 1, false);
			List<Cours> cours = new ArrayList<>();
			cours.add(c1);
			// Cr�ation d'un nouveau prof avec ce cours
			Professeur prof = new Professeur("TRUC", "Truc", "truc", new Contact("truc@isfce.org", "02/647.25.69"),
					false, new Date());
			prof.setCours(cours);

			// sauvegarde du cours et ensuite du prof
			daoCours.save(c1);
			daoProfs.save(prof);

			// R�cup�re le prof et regarde si OK
			Optional<Professeur> prof2 = daoProfs.findById("TRUC");
			assertTrue(prof2.isPresent());
			assertEquals(prof, prof2.get());

			// Suppression d'un cours au professeur et sauvegarde
			prof2.get().getCours().remove(c1);
			daoProfs.save(prof2.get());
			Optional<Professeur> prof3 = daoProfs.findById("TRUC");
			assertTrue(prof3.isPresent());
			assertEquals(prof2.get(), prof3.get());

			// v�rifie que le cours existe tjs
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
		//findBy une expression r�guli�re
		profs = daoProfs.findByContactEmailRegex("isfce.org$");
		assertEquals(2, profs.size());
		System.out.println(profs);
		//Chargement que des champs nom et prenom
		profs = daoProfs.findNomPrenomPour1Tel("02/647.25.69");
		System.out.println(profs);
		assertEquals(2, profs.size());
		//Teste la pagination
		Page<Cours> pCours = daoCours.findAll(PageRequest.of(1, 3));
		assertEquals(3, pCours.getNumberOfElements());

		
		// Exemple d'utilisation du module aggregate
		Aggregation agr;
		agr = Aggregation.newAggregation(Professeur.class,
				Aggregation.match(Criteria.where("contact.tel").in("02/647.25.69")),
				Aggregation.project("nom", "prenom"));
		// Ex�cution du module
		AggregationResults<Professeur> results = mongoTemplate.aggregate(agr, "prof", Professeur.class);

		System.out.println(results.getMappedResults());
		assertEquals(2, results.getMappedResults().size());
		
		// Exemple2 avec un group
		 
		agr = Aggregation.newAggregation(Cours.class, Aggregation.group("nbPeriodes").count().as("nbCours"));
		// Ex�cution du module
		AggregationResults<ResultAggr2> results2 = mongoTemplate.aggregate(agr, "cours", ResultAggr2.class);
		System.out.println("--------Aggre2-------");
		System.out.println(results2.getMappedResults());
		assertEquals(6, results2.getMappedResults().size());

	}

}
