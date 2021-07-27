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

public class ClasseurController {
	@Autowired
	ClasseurService classeurService;
	@Autowired
	FichierService fichierService;
	@Autowired
	PlacardService placardService;
	@Autowired
	ServicesService serviceService;
	@Autowired
	CaseService caseService;

	private Path fileStoragePath;
	private String classeurStorageLocation;
	
	@RequestMapping(value = "/classeurs", method = RequestMethod.GET)
	public List<Classeur> getAllClasseurs() {
		return classeurService.getAllClasseurs();
	}
//	add classeur
//	{
//	    "nomClasseur": "Classeur1",
//	    "cases": {
//				"idCase" : 1
//			}
//	  }
	@RequestMapping(value = "/classeur" ,method = RequestMethod.POST)
	public Classeur addClasseur(@Valid @RequestBody Classeur classeur, BindingResult bindingResult ){
		if(bindingResult.hasErrors()) {
			//return FormService;
		}   
		Cases c=caseService.getCaseById(classeur.getCases().getIdCase());
		List<Classeur> liste= c.getClasseurs();
		//verifier si le nom de la case existe deja dans la base de donnée
		if(liste != null) {
			for(Classeur l : liste) {
				if(l.getNomClasseur().equals((classeur.getNomClasseur()))) {
					classeur.setNomClasseur(classeur.getNomClasseur()+"_"+liste.size());
					break;
				}
			}
		}
		Placard p=c.getPlacards();
		Services s= p.getServices(); 
		classeurStorageLocation=s.getNomService()+"/"+p.getNomPlacard()+"/"+c.getNomCase()+"/"+classeur.getNomClasseur();
		//creation du repertoire placard
		fileStoragePath = Paths.get(classeurStorageLocation).toAbsolutePath().normalize();
        try {
            Files.createDirectories(fileStoragePath);
          //ajouter le classeur dans la base de donné
    		classeurService.addClasseur(classeur);
        } catch (IOException e) {
            throw new RuntimeException("Issue in creating the directory");
        }
        return classeur;
	}
	//edit classeur 
//	{
//	    "nomClasseur": "classeur_new",
//	    
//				"idClasseur" : 1
//			
//	  }
	@RequestMapping(value = "/classeur", method = RequestMethod.PUT)
	public Classeur editClasseur(@RequestBody Classeur c) {
		Services s;
		Placard p;
		Cases cases;
		
		File newdir = null;
		Classeur c1=classeurService.getClasseurById(c.getIdClasseur()); //obtenir les anciennes infos sur la case depuis la db
		if(c.getCases()==null) {
			c.setCases(c1.getCases());
			cases=caseService.getCaseById(c1.getCases().getIdCase()); //obtenir les infors sur la case
		} else {
			c.setCases(c.getCases());
			cases=caseService.getCaseById(c.getCases().getIdCase()); //obtenir les infors sur la case
		}
		p=cases.getPlacards();
		s=p.getServices();
		if(c.getNomClasseur()==null) 
			c.setNomClasseur(c1.getNomClasseur());
		else 
			c.setNomClasseur(c.getNomClasseur());
		List<Classeur> liste= cases.getClasseurs();
		//verifier si le nom duclasseur existe deja dans la case donné
		if(liste!=null) {
			for(Classeur l : liste) {
				if(l.getNomClasseur().equals((c.getNomClasseur())) && l.getIdClasseur()!=c.getIdClasseur()) { // "possibilié de rester dans le mm repertoire et donc on compare le classeur avec d'autres classeurs de la case"
				  throw new RuntimeException("Le nom du Classeur donné, existe déjà");
				}
		    }
		}
		File source = new File(c1.getCases().getPlacards().getServices().getNomService(),c1.getCases().getPlacards().getNomPlacard());
		source= new File(source,c1.getCases().getNomCase());
		source= new File(source,c1.getNomClasseur());

		//renommer la case
		newdir = new File(c1.getCases().getPlacards().getServices().getNomService(),c1.getCases().getPlacards().getNomPlacard());
		newdir = new File(newdir,c1.getCases().getNomCase());
		newdir = new File(newdir,c.getNomClasseur());
		
		if (source.renameTo(newdir)) {
		    System.out.println("Directory renamed successfully");
		 } else {
			 System.out.println("Failed to rename directory");
		 }
		classeurService.updateClasseur(c);
		//le repertoire de a case
		File sourceFolder = newdir;
		File destinationFolder = new File(s.getNomService(),p.getNomPlacard());
		destinationFolder = new File(destinationFolder,cases.getNomCase());
		destinationFolder = new File(destinationFolder,c.getNomClasseur());
		try {
			Files.move(sourceFolder.toPath(),destinationFolder.toPath(),StandardCopyOption.REPLACE_EXISTING);
			System.out.println("Directory moved successfully");
		}
		catch (IOException e) {
			 throw new RuntimeException("Issue in moving the directory");
        }
		return c;
	}
	@RequestMapping(value = "/classeur/{id}", method = RequestMethod.DELETE)
	public void deleteClasseur(@PathVariable Long id) {
		
		Classeur classeur=classeurService.getClasseurById(id);
		Cases c=classeur.getCases();
		Placard p=c.getPlacards();
		Services s= p.getServices(); 
		classeurStorageLocation=s.getNomService()+"/"+p.getNomPlacard()+"/"+c.getNomCase()+"/"+classeur.getNomClasseur();
		//le repertoire du classeur
		fileStoragePath = Paths.get(classeurStorageLocation).toAbsolutePath().normalize();
	
		try {
		   //supprimer les fichier qui appartiennent au classeur
            List<Fichier> fichiers= classeur.getFichiers();
            if(fichiers != null) {
            	for(Fichier fichier: fichiers){
            		String fichierStorageLocation=s.getNomService()+"/"+p.getNomPlacard()+"/"+c.getNomCase()+"/"+classeur.getNomClasseur()+"/"+fichier.getNomFichier();
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
            //supprimer le classeur depuis son emplacement dans le dossier de stockage
            Files.delete(fileStoragePath);
            //supprimer le classeur de la base de données
            classeurService.deleteClasseur(id);
        } catch (IOException e) {
            throw new RuntimeException("Issue in deleting the directory");
        }
	}
}
