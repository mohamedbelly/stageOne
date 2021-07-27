package com.example.demo.services;

import java.util.List;

import com.example.demo.entities.Services;

public interface ServicesService {
	public Services addService(Services s);
	public Services getServiceById(Long id);
	public List<Services> getAllServices();
	public void deleteService(Long id);
	public void updateService(Services f);
}
