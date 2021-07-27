package com.example.demo.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.example.demo.entities.Classeur;
import com.example.demo.respository.ClasseurRepository;

@Service
public class ClasseurServiceImpl implements ClasseurService{
	
	@Autowired
	ClasseurRepository classeurRepository;
	
	@Override
	public Classeur addClasseur(Classeur c) {
		return classeurRepository.save(c);
	}

	@Override
	public Classeur getClasseurById(Long id) {
		return classeurRepository.findById(id).get(); 
	}

	@Override
	public List<Classeur> getAllClasseurs() {
		return classeurRepository.findAll();
	}

	@Override
	public void deleteClasseur(Long id) {
		classeurRepository.deleteById(id);
	}

	@Override
	public void updateClasseur(Classeur c) {		
		classeurRepository.save(c);
	}

}
