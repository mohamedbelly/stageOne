package com.example.demo.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.entities.Categorie;
import com.example.demo.respository.CategorieRepository;

@Service
public class CategorieServiceImpl implements CategorieService{
	
	@Autowired
	CategorieRepository categorieRepository;
	
	@Override
	public Categorie addCategorie(Categorie c) {
		return categorieRepository.save(c);
	}

	@Override
	public Categorie getCategorieById(Long id) {
		return categorieRepository.findById(id).get(); 
	}

	@Override
	public List<Categorie> getAllCategories() {
		return categorieRepository.findAll();
	}

	@Override
	public void deleteCategorie(Long id) {
		categorieRepository.deleteById(id);
	}

	@Override
	public void updateCategorie(Categorie c) {		
		categorieRepository.save(c);
	}

}
