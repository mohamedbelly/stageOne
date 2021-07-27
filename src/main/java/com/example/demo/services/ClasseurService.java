package com.example.demo.services;

import java.util.List;

import com.example.demo.entities.Classeur;

public interface ClasseurService {
	public Classeur addClasseur(Classeur c);
	public Classeur getClasseurById(Long id);
	public List<Classeur> getAllClasseurs();
	public void deleteClasseur(Long id);
	public void updateClasseur(Classeur c);
}
