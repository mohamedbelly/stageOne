package com.example.demo.entities;
import java.util.List;

import javax.persistence.Entity;
import javax.persistence.GeneratedValue;
import javax.persistence.GenerationType;
import javax.persistence.Id;
import javax.persistence.OneToMany;
import javax.validation.constraints.DecimalMin;
import javax.validation.constraints.Size;

import com.fasterxml.jackson.annotation.JsonIgnore;
import com.sun.istack.NotNull;


@Entity
public class Services {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY)
	private Long idService;
	@NotNull
	@Size(min = 3,max = 15, message = "Le nombre de caractere doit etre entre 3 et 15")
	private String nomService;
	@DecimalMin("1")
	private Long nbrPlacards;
	@DecimalMin("1")
	private Long nbrCases;	
	@OneToMany(mappedBy = "services")
	private List<Placard> placards;
	
	public Long getIdService() {
		return idService;
	}
	public void setIdService(Long idService) {
		this.idService = idService;
	}
	public String getNomService() {
		return nomService;
	}
	public void setNomService(String nomService) {
		this.nomService = nomService;
	}
	public Long getNbrPlacards() {
		return nbrPlacards;
	}
	public void setNbrPlacards(Long nbrPlacards) {
		this.nbrPlacards = nbrPlacards;
	}
	public Long getNbrCases() {
		return nbrCases;
	}
	public void setNbrCases(Long nbrCases) {
		this.nbrCases = nbrCases;
	}
	public Services(String nomService) {
		super();
		this.nomService = nomService;
	}
	@JsonIgnore
	public List<Placard> getPlacards() {
		return placards;
	}
	public void setPlacards(List<Placard> placards) {
		this.placards = placards;
	}
	public Services() {
		super();
		// TODO Auto-generated constructor stub
	}
}
