package com.example.demo.entities;
import java.util.Date;
import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.Temporal;
import javax.persistence.TemporalType;

import org.springframework.format.annotation.DateTimeFormat;

import com.sun.istack.NotNull;

@Entity
public class Fichier{
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY )
	private Long idFichier;
	@NotNull
	private String nomFichier;
	@Temporal(TemporalType.DATE)
	@DateTimeFormat(pattern = "yyyy-MM-dd")
	private Date dateCreation;
	private String path;
	@ManyToOne
	@JoinColumn(name="code_categorie")
	private Categorie categorie;
	@ManyToOne
	@JoinColumn(name="code_classeur")
	private Classeur classeur;
	
	public Long getIdFichier() {
		return idFichier;
	}
	public void setIdFichier(Long idFichier) {
		this.idFichier = idFichier;
	}
	public String getNomFichier() {
		return nomFichier;
	}
	public void setNomFichier(String nomFichier) {
		this.nomFichier = nomFichier;
	}
	public Date getDateCreation() {
		return dateCreation;
	}
	public void setDateCreation(Date dateCreation) {
		this.dateCreation = dateCreation;
	}
	
	public String getPath() {
		return path;
	}
	public void setPath(String path) {
		this.path = path;
	}
	public Categorie getCategorie() {
		return categorie;
	}
	public Classeur getClasseur() {
		return classeur;
	}
	public void setClasseur(Classeur classeur) {
		this.classeur = classeur;
	}
	public void setCategorie(Categorie categorie) {
		this.categorie = categorie;
	}
	public Fichier(String nomFichier, String typeFichier, String path) {
		super();
		this.nomFichier = nomFichier;
			this.path = path;
	}
	public Fichier() {
		super();
		// TODO Auto-generated constructor stub
	}
	
	
}
