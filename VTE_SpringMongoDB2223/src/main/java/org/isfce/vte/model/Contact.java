package org.isfce.vte.model;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;
import lombok.Data;
import lombok.NoArgsConstructor;
//Sous-Document ==> pas d'ID
@Data
@NoArgsConstructor(access=AccessLevel.PROTECTED)
@AllArgsConstructor
public class Contact {
	private String email;
	private String tel;
}
