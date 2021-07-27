package com.example.demo.entities;

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

import java.util.List;

@Entity
public class Cases {
	@Id
	@GeneratedValue(strategy = GenerationType.IDENTITY )
	private Long idCase;
	@NotNull
	@Size(min = 3,max = 15, message = "Le nombre de caractere doit etre entre 3 et 15")
	private String nomCase;
	@OneToMany(mappedBy = "cases")
	private List<Classeur> classeurs;
	@ManyToOne
	@JoinColumn(name="code_placard")
	private Placard placards;
	
	//Methodes GETTERS & SETTERS
		public Long getIdCase() {
			return idCase;
		}
		public void setIdCase(Long idCase) {
			this.idCase = idCase;
		}
		public String getNomCase() {
			return nomCase;
		}
		public void setNom(String nomCase) {
			this.nomCase= nomCase;
		}
		public Cases(String nomCase) {
			super();
			this.nomCase = nomCase;
		}
		public Cases() {
			
		}
		@JsonIgnore
		public List<Classeur> getClasseurs() {
			return classeurs;
		}
		public void setClasseurs(List<Classeur> classeurs) {
			this.classeurs = classeurs;
		}
		public Placard getPlacards() {
			return placards;
		}
		public void setPlacards(Placard placards) {
			this.placards = placards;
		}
			
}
