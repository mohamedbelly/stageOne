package com.example.demo.services;

import java.util.List;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.stereotype.Service;
import com.example.demo.entities.Cases;
import com.example.demo.respository.CaseRepository;

@Service
public class CaseServiceImpl implements CaseService{
	
	@Autowired
	CaseRepository caseRepository;
	
	@Override
	public Cases addCase(Cases c) {
		return caseRepository.save(c);
	}

	@Override
	public Cases getCaseById(Long id) {
		return caseRepository.findById(id).get(); //getById(id)
	}
	
	@Override
	public List<Cases> getAllCases() {
		return caseRepository.findAll();
	}

	@Override
	public void deleteCase(Long id) {
		caseRepository.deleteById(id);
	}

	@Override
	public void updateCase(Cases c) {		
		caseRepository.save(c);
	}

}
