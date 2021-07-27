package com.example.demo.services;

import java.util.List;

import com.example.demo.entities.Cases;

public interface CaseService {
	public Cases addCase(Cases c);
	public Cases getCaseById(Long id);
	public List<Cases> getAllCases();
	public void deleteCase(Long id);
	public void updateCase(Cases c);
}
