package com.isfce.vte.model;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Value;
import org.springframework.data.annotation.Id;
import org.springframework.data.annotation.PersistenceConstructor;
import org.springframework.data.mongodb.core.index.CompoundIndex;
import org.springframework.data.mongodb.core.index.CompoundIndexes;
import org.springframework.data.mongodb.core.mapping.DBRef;
import org.springframework.data.mongodb.core.mapping.Document;

import lombok.AccessLevel;
import lombok.Data;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@Document(collection = "prof")
//Cr�ation d'un index compos� unique sur les nom et pr�nom
@CompoundIndexes(
{
    @CompoundIndex(name = "nom_prenom", def = "{'nom' : 1, 'prenom': 1}",unique= true)
})
public class Professeur {
	@Id // pas obligatoire si le champ s'appelle id
	private String code;
	private String nom;
	private String prenom;

	// sous document Contact
	private Contact contact;

	// r�f�rence � un vecteur de cours avec des DBRef
	@DBRef
	private List<Cours> cours = new ArrayList<>();

	private Boolean actif;

	private Date dateFonction;
/**
 * Constructeur utilis� pour la persistance
 * @param code (ID) sera forcer en majuscule
 * @param nom
 * @param prenom
 * @param contact (tel et email du prof)
 * @param actif gr�ce � @Value, actif sera initialis� true si le champ est � null en MongoDB
 * @param dateFonction
 */
	@PersistenceConstructor
	public Professeur(String code, String nom, String prenom, Contact contact, @Value("#root.actif ?: true")Boolean actif,
			Date dateFonction) {
		this.code = code.toUpperCase();
		this.nom = nom;
		this.prenom = prenom;
		this.contact = contact;
		this.actif = actif;
		this.dateFonction = dateFonction;
	}
}
