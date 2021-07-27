package com.example.demo.services;

import java.util.List;

import com.example.demo.entities.Placard;

public interface PlacardService {
	public Placard addPlacard(Placard p);
	public Placard getPlacardById(Long id);
	public List<Placard> getAllPlacards();
	public void deletePlacard(Long id);
	public void updatePlacard(Placard p);
}
