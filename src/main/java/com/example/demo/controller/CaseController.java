package com.example.demo.controller;

import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;

import javax.validation.Valid;

import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import com.example.demo.entities.Cases;
import com.example.demo.entities.Classeur;
import com.example.demo.entities.Fichier;
import com.example.demo.entities.Placard;
import com.example.demo.entities.Services;
import com.example.demo.services.CaseService;
import com.example.demo.services.ClasseurService;
import com.example.demo.services.FichierService;
import com.example.demo.services.PlacardService;
import com.example.demo.services.ServicesService;

@RestController //le resultat est par defaut de format json

public class CaseController {
	@Autowired
	FichierService fichierService;
	@Autowired
	ClasseurService classeurService;
	@Autowired
	CaseService caseService;
	@Autowired
	PlacardService placardService;
	@Autowired
	ServicesService serviceService;
	private Path fileStoragePath;
	private String caseStorageLocation;
	@RequestMapping(value = "/cases", method = RequestMethod.GET)
	public List<Cases> getAllCases() {
		return caseService.getAllCases();
	}
	// add case
//  	{
//  	    "nomCase": "casenew",
//  	    "placards": {
//  	      "idPlacard": 1
//  	    }
//  	}
	@RequestMapping(value = "/case", method = RequestMethod.POST)
	public Cases addCase(@Valid @RequestBody Cases c, BindingResult bindingResult){
		if(bindingResult.hasErrors()) {
			//return FormService;
		}
		Placard p=placardService.getPlacardById(c.getPlacards().getIdPlacard());
		List<Cases> liste= p.getCases();
		//verifier si le nom de la case existe deja dans la base de donnée
		if(liste!=null) {
			for(Cases l : liste) {
				if(l.getNomCase().equals((c.getNomCase()))) {
					c.setNom(c.getNomCase()+"_"+liste.size());
					break;
				}
			}
		}
		Services s= p.getServices(); 
		String caseStorageLocation=s.getNomService()+"/"+p.getNomPlacard()+"/"+c.getNomCase();
		//creation du repertoire placard
		fileStoragePath = Paths.get(caseStorageLocation).toAbsolutePath().normalize();
        try {
            Files.createDirectories(fileStoragePath);
            //ajouter la case dans la base de donnees
            caseService.addCase(c);
          //modifier le nombre des cases dans la base de données
            p.setNombreCases(p.getNombreCases()+1);
            placardService.updatePlacard(p);
            s.setNbrCases(s.getNbrCases()+1);
            serviceService.updateService(s);
        } catch (IOException e) {
            throw new RuntimeException("Issue in creating the directory");
        }
		return c;
	}
	// edit case
//	{
//	    "nomCase": "casenewnew",
//	    "idCase":2
//	}
	@RequestMapping(value = "/case", method = RequestMethod.PUT) // la modification de la case : modifier le nom ou l'emplacement de la case
	public Cases editCase(@RequestBody Cases c) {
		Services s;
		Placard p;

		File newdir = null;
		Cases c1=caseService.getCaseById(c.getIdCase()); //obtenir les anciennes infos sur le placard de la db
		if(c.getPlacards()==null) { //on reste dans le meme placard
			c.setPlacards(c1.getPlacards());
			p=placardService.getPlacardById(c1.getPlacards().getIdPlacard()); //obtenir les infors sur le service du placard
		} else {
				p=placardService.getPlacardById(c.getPlacards().getIdPlacard()); //obtenir les infors sur le service du placard
				//la case change le placard
				c.setPlacards(c.getPlacards());
		 }
		s=p.getServices();
		if(c.getNomCase()==null) 
			c.setNom(c1.getNomCase());
		else 
			c.setNom(c.getNomCase());
		List<Cases> liste= p.getCases();
		//verifier si le nom de la case existe deja dans le placard donné
		if(liste!=null){ 
			for(Cases l : liste) {
				if(l.getNomCase().equals((c.getNomCase())) && l.getIdCase()!=c.getIdCase()) { // "possibilié de rester dans le mm repertoire et donc on compare la case avec d'autres cases"
				  throw new RuntimeException("Le nom de case donné, existe déjà");
				}
		    }
		}
		File source = new File(c1.getPlacards().getServices().getNomService(),c1.getPlacards().getNomPlacard());
		source= new File(source,c1.getNomCase());

		//renommer la case
		newdir = new File(c1.getPlacards().getServices().getNomService(),c1.getPlacards().getNomPlacard());
		newdir = new File(newdir,c.getNomCase());
		
		if (source.renameTo(newdir)) {
		    System.out.println("Directory renamed successfully");
		 } else {
			 System.out.println("Failed to rename directory");
		 }
		//le repertoire de a case
		File sourceFolder = newdir;
		File destinationFolder = new File(s.getNomService(),p.getNomPlacard());
		destinationFolder = new File(destinationFolder,c.getNomCase());
		try {
			Files.move(sourceFolder.toPath(),destinationFolder.toPath(),StandardCopyOption.REPLACE_EXISTING);
			System.out.println("Directory moved successfully");
		}
		catch (IOException e) {
			 throw new RuntimeException("Issue in moving the directory");
        }
		if(c.getPlacards().getIdPlacard()!=c1.getPlacards().getIdPlacard()) {
			p=placardService.getPlacardById(c1.getPlacards().getIdPlacard());
			p.setNombreCases(p.getNombreCases()-1);
			s=p.getServices();
			s.setNbrCases(s.getNbrCases()-1);
			placardService.updatePlacard(p);
			serviceService.updateService(s);
			
			//et donc le nombre des cases dans le nouveau placard augmente
			p=placardService.getPlacardById(c.getPlacards().getIdPlacard());
			p.setNombreCases(p.getNombreCases()+1);
			s=p.getServices();
			s.setNbrCases(s.getNbrCases()+1);
			placardService.updatePlacard(p);
			serviceService.updateService(s);
		}
		caseService.updateCase(c);
		return c;
	}
	@RequestMapping(value = "/case/{id}", method = RequestMethod.DELETE)
	public void deleteCase(@PathVariable Long id) {
		Cases cases=caseService.getCaseById(id);
		Placard p=cases.getPlacards();
		Services s= p.getServices();
		caseStorageLocation=s.getNomService()+"/"+p.getNomPlacard()+"/"+cases.getNomCase();
		//le repertoire de la case
		fileStoragePath = Paths.get(caseStorageLocation).toAbsolutePath().normalize();
			try {
		   //supprimer les fichier et les classeurs qui appartiennent à la case
			List<Classeur> classeurs = cases.getClasseurs();
			if(classeurs != null) { //si la case contienne des classeurs
				for(Classeur classeur: classeurs){ 
					List<Fichier> fichiers=classeur.getFichiers(); //pour chaque classeur on recupere la liste des fichiers
					if(fichiers != null) { // si le classeur contient des fichiers
		            	for(Fichier fichier: fichiers){
		            		String fichierStorageLocation=s.getNomService()+"/"+p.getNomPlacard()+"/"+cases.getNomCase()+"/"+classeur.getNomClasseur()+"/"+fichier.getNomFichier();
		            		Path fichierStoragePath = Paths.get(fichierStorageLocation).toAbsolutePath().normalize();
		            		try {
		            			//supprimer le fichier depuis son emplacement dans le dossier de stockage
		                        Files.delete(fichierStoragePath);
		                      //supprimer le fichier de la base de donné
		                        fichierService.deleteFichier(fichier.getIdFichier());
		                    	}
		            		catch (IOException e) {
		                        throw new RuntimeException("Issue in deleting the directory");
		                    	}
			            }
		            }
					String classeurStorageLocation=s.getNomService()+"/"+p.getNomPlacard()+"/"+cases.getNomCase()+"/"+classeur.getNomClasseur();
            		Path classeurStoragePath = Paths.get(classeurStorageLocation).toAbsolutePath().normalize();
            		try {
            			//supprimer le classeur depuis son emplacement dans le dossier de stockage
                        Files.delete(classeurStoragePath);
                      //supprimer le fichier de la base de donné
                        classeurService.deleteClasseur(classeur.getIdClasseur());
                    	}
            		catch (IOException e) {
                        throw new RuntimeException("Issue in deleting the directory");
                    	}
			    }
			}
            //supprimer la case depuis son emplacement dans le dossier de stockage
            Files.delete(fileStoragePath);
            //supprimer le case depuis la base de données
            caseService.deleteCase(id);
          //modifier le nombre des cases dans la base de données
            p.setNombreCases(p.getNombreCases()-1);
            placardService.updatePlacard(p);
            s.setNbrCases(s.getNbrCases()-1);
            serviceService.updateService(s);
        } catch (IOException e) {
            throw new RuntimeException("Issue in deleting the directory");
        }
	}
}
