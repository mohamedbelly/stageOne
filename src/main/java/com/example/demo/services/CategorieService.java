package com.example.demo.services;

import java.util.List;

import com.example.demo.entities.Categorie;

public interface CategorieService {
	public Categorie addCategorie(Categorie c);
	public Categorie getCategorieById(Long id);
	public List<Categorie> getAllCategories();
	public void deleteCategorie(Long id);
	public void updateCategorie(Categorie c);
}
