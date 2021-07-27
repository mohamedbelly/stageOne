package com.example.demo.services;

import java.util.Date;
import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;

import com.example.demo.entities.Fichier;
import com.example.demo.respository.FichierRepository;

@Service
public class FichierServiceImpl implements FichierService{
	
	@Autowired
	FichierRepository fichierRepository;
	
	@Override
	public Fichier addFichier(Fichier f) {
		f.setDateCreation(new Date());
		return fichierRepository.save(f);
	}

	@Override
	public Fichier getFichierById(Long id) {
		return fichierRepository.findById(id).get(); 
	}

	@Override
	public List<Fichier> getAllFichiers() {
		return fichierRepository.findAll();
	}

	@Override
	public void deleteFichier(Long id) {
		fichierRepository.deleteById(id);
	}

	@Override
	public void updateFichier(Fichier f) {		
		fichierRepository.save(f);
	}
	public String searchFichier(String nom) {
		List<Fichier> files = fichierRepository.findAll();
		if(files != null) {
			for(Fichier file :  files){
				if(file.getNomFichier().contentEquals(nom)) {
					return file.getPath();
				}
			}
		}
		return "Fichier introuvable ! ";
	}
}
