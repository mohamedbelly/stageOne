package com.example.demo.entities;
import java.util.List;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.sun.istack.NotNull;

@Entity
public class Classeur {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long idClasseur;
	@NotNull
	@Size(min = 3,max = 15, message = "Le nombre de caractere doit etre entre 3 et 15")
	private String nomClasseur;
	@ManyToOne
	@JoinColumn(name="code_case")
	private Cases cases;
	@OneToMany(mappedBy = "classeur")
	private List<Fichier> fichiers;
	
	public Long getIdClasseur() {
		return idClasseur;
	}
	public void setIdClasseur(Long idClasseur) {
		this.idClasseur = idClasseur;
	}
	public String getNomClasseur() {
		return nomClasseur;
	}
	public void setNomClasseur(String nomClasseur) {
		this.nomClasseur = nomClasseur;
	}
	@JsonIgnore
	public List<Fichier> getFichiers() {
		return fichiers;
	}
	public void setFichiers(List<Fichier> fichiers) {
		this.fichiers = fichiers;
	}
	public Cases getCases() {
		return cases;
	}
	public void setCases(Cases cases) {
		this.cases = cases;
	}
	public Classeur(String nomClasseur) {
		super();
		this.nomClasseur = nomClasseur;
	}
	public Classeur() {
		super();
		// TODO Auto-generated constructor stub
	}
	
}
