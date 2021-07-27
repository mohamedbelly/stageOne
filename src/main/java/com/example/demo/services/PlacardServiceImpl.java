package com.example.demo.services;

import java.util.List;
import com.example.demo.entities.Placard;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import com.example.demo.respository.PlacardRepository;

@Service
public class PlacardServiceImpl implements PlacardService{
	
	@Autowired
	PlacardRepository placardRepository;
	@Autowired
	ServicesService serviceService;
	
	
	@Override
	public Placard addPlacard(Placard p) {
		return placardRepository.save(p);
	}

	@Override
	public Placard getPlacardById(Long id) {
		return placardRepository.findById(id).get(); 
	}

	@Override
	public List<Placard> getAllPlacards() {
		return placardRepository.findAll();
	}

	@Override
	public void deletePlacard(Long id) {
		placardRepository.deleteById(id);
	}

	@Override
	public void updatePlacard(Placard p) {		
		placardRepository.save(p);
	}

}
