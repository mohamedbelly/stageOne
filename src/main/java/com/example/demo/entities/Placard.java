package com.example.demo.entities;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.JoinColumn;
import javax.persistence.ManyToOne;
import javax.persistence.OneToMany;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.sun.istack.NotNull;

import java.util.List;

@Entity
public class Placard {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY )
	private Long idPlacard;
	@NotNull
	@Size(min = 3,max = 15, message = "Le nombre de caractere doit etre entre 3 et 15")
	private String nomPlacard;
	@DecimalMin("1")
	private Long nombreCases;
	@OneToMany(mappedBy = "placards")
	private List<Cases> cases;
	@ManyToOne
	@JoinColumn(name="code_service")
	private Services services;
	
	//Methodes GETTERS & SETTERS
	public Long getIdPlacard() {
		return idPlacard;
	}
	public void setIdPlacard(Long idPlacard) {
		this.idPlacard = idPlacard;
	}
	public String getNomPlacard() {
		return nomPlacard;
	}
	public void setNomPlacard(String nomPlacard) {
		this.nomPlacard = nomPlacard;
	}
	public Long getNombreCases() {
		return nombreCases;
	}
	public void setNombreCases(Long nombreCases) {
		this.nombreCases = nombreCases;
	}
	@JsonIgnore
	public List<Cases> getCases() {
		return cases;
	}
	public void setCases(List<Cases> cases) {
		this.cases = cases;
	}
	public Services getServices() {
		return services;
	}
	public void setServices(Services services) {
		this.services = services;
	}
	public Placard(String nomPlacard, Long nombreCases) {
		super();
		this.nomPlacard = nomPlacard;
		this.nombreCases = nombreCases;
	}
	public Placard() {
		super();
		// TODO Auto-generated constructor stub
	}		
		
}
