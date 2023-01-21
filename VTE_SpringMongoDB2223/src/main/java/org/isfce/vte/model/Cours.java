package org.isfce.vte.model;

import org.springframework.data.annotation.Id;
import org.springframework.data.mongodb.core.index.Indexed;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.Getter;
import lombok.NoArgsConstructor;

@Data
@NoArgsConstructor(access = AccessLevel.PROTECTED)
@AllArgsConstructor
public class Cours {
	@Id // pas obligatoire si le champ s'appelle id
	@Getter // pour ne pas avoir de setter
	private String code;
	
	private String intitule;
	@Indexed // Création d'un index en MongoDB sur ce champ
	private int nbPeriodes;
	 
	private int etcs;
	
	private boolean determinant;
}
