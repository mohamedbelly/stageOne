package com.example.demo.entities;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import com.fasterxml.jackson.annotation.JsonIgnore;
import java.util.List;

@Entity
public class Categorie {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY )
	private Long idCategorie;
	private String nomCategorie;
	@OneToMany(mappedBy = "categorie")
	private List<Fichier> fichiers;
	//Methodes GETTERS & SETTERS
		public Long getIdCategorie() {
			return idCategorie;
		}
		public void setIdCategorie(Long idCategorie) {
			this.idCategorie= idCategorie;
		}
		public String getNomCategorie() {
			return nomCategorie;
		}
		public void setNomCategorie(String nomCategorie) {
			this.nomCategorie = nomCategorie;
		}
		public Categorie(String nomCategorie) {
			super();
			this.nomCategorie = nomCategorie;
		}
		public Categorie() {

		}
		@JsonIgnore
		public List<Fichier> getFichiers() {
			return fichiers;
		}
		public void setProduits(List<Fichier> fichiers) {
			this.fichiers = fichiers;
		}		
}
