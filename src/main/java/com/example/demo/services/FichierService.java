package com.example.demo.services;

import java.util.List;

import com.example.demo.entities.Fichier;

public interface FichierService {
	public Fichier addFichier(Fichier f);
	public Fichier getFichierById(Long id);
	public List<Fichier> getAllFichiers();
	public void deleteFichier(Long id);
	public void updateFichier(Fichier f);
	public String searchFichier(String nom);
}
