package com.example.demo.controller;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import com.example.demo.entities.Categorie;
import com.example.demo.services.CategorieService;

@RestController //le resultat est par defaut de format json

public class CategorieController {
	@Autowired
	CategorieService categorieService;
	
	@RequestMapping(value = "/categories", method = RequestMethod.GET)
	public List<Categorie> getAllCategories() {
		return categorieService.getAllCategories();
	}
	
	@RequestMapping(value = "/categorie" ,method = RequestMethod.POST)
	public Categorie addCategorie(@RequestBody Categorie c){
			categorieService.addCategorie(c);
			return c;
	}
	@RequestMapping(value = "/categorie" ,method = RequestMethod.PUT)
	public Categorie editeCategorie(@RequestBody Categorie c){
		if(c.getNomCategorie()!=null){
		c.setNomCategorie(c.getNomCategorie());
	    categorieService.updateCategorie(c);
		}
	    return c;
	}

	@RequestMapping(value = "/categorie/{id}", method = RequestMethod.DELETE)
	public void deleteCategorie(@PathVariable Long id) {
		categorieService.deleteCategorie(id);
	}
}
