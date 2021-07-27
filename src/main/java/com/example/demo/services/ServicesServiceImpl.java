package com.example.demo.services;

import java.util.List;
import com.example.demo.entities.Services;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;


import com.example.demo.respository.ServicesRepository;

@Service
public class ServicesServiceImpl implements ServicesService{
	
	@Autowired
	ServicesRepository serviceRepository;
	
	@Override
	public Services addService(Services c) {
		return serviceRepository.save(c);
	}

	@Override
	public Services getServiceById(Long id) {
		return serviceRepository.findById(id).get(); 
	}

	@Override
	public List<Services> getAllServices() {
		return serviceRepository.findAll();
	}

	@Override
	public void deleteService(Long id) {
		serviceRepository.deleteById(id);
	}

	@Override
	public void updateService(Services s) {		
		serviceRepository.save(s);
	}

}
