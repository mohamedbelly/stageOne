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

public class PlacardController {
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
	private String placardStorageLocation;
	private String fichierStorageLocation;
	private Path fichierStoragePath;
	private String classeurStorageLocation;
	private String caseStorageLocation;
	private Path classeurStoragePath;
	private Path caseStoragePath;

	
	@RequestMapping(value = "/placards", method = RequestMethod.GET)
	public List<Placard> getAllPlacards() {
		return placardService.getAllPlacards();
	}
//ajouter placard
//	{
//    "nomPlacard": "Placard2",
//    "nombreCases": 2,
//    "services": {
//      "idService": 1
//    }
//}
	@RequestMapping(value = "/placard", method = RequestMethod.POST)
	 public Placard addPlacard(@Valid @RequestBody Placard p,BindingResult bindingResult ){
			if(bindingResult.hasErrors()) {
				//return FormPlacard;
			}   
		Services s=serviceService.getServiceById(p.getServices().getIdService()); 
		List<Placard> liste= s.getPlacards();
	
		//verifier si le nom ddu placard existe deja dans la base de donnée
		if(liste !=null) {
			for(Placard l : liste) {
				if(l.getNomPlacard().equals((p.getNomPlacard()))) {
					p.setNomPlacard(p.getNomPlacard()+"_"+liste.size());
					break;
				}
			}
		}
		String placardStorageLocation=s.getNomService()+"/"+p.getNomPlacard();
		//creation du repertoire placard
		fileStoragePath = Paths.get(placardStorageLocation).toAbsolutePath().normalize();
        try {
            Files.createDirectories(fileStoragePath);
            placardService.addPlacard(p);
            //modifier le nombre des placards dans la base de données
            s.setNbrPlacards(s.getNbrPlacards()+1);
            serviceService.updateService(s);
        } catch (IOException e) {
            throw new RuntimeException("Issue in creating the directory");
        }
		//creation des cases du placard
			for(int i=1;i<=p.getNombreCases();i++) {
				Cases cases=new Cases();
				cases.setPlacards(p);
				cases.setNom("Case_"+i);
				String caseStorageLocation = placardStorageLocation+"/"+cases.getNomCase();
				caseService.addCase(cases);
				//modifier le nombre des cases dans la base de données
	            s.setNbrCases(s.getNbrCases()+1);
	            serviceService.updateService(s);
				//creation du repertoire qui port le nom de la case
		        fileStoragePath = Paths.get(caseStorageLocation).toAbsolutePath().normalize();
		        try {
		            Files.createDirectories(fileStoragePath);
		        } catch (IOException e) {
		            throw new RuntimeException("Issue in creating the directory");
		        }
			}
		return p;
	}
//	{
//		   "idPlacard": 3,
//		    "nomPlacard": "Placard2"
//	}
	@RequestMapping(value = "/placard", method = RequestMethod.PUT)
	public Placard editPlacard(@RequestBody Placard p) {
		Services s = null;
		File newdir = null;
		Placard p1=placardService.getPlacardById(p.getIdPlacard()); //obtenir les anciennes infos sur le placard de la db
		if(p.getServices()==null) {
			p.setServices(p1.getServices());
			s=serviceService.getServiceById(p1.getServices().getIdService()); //obtenir les infors sur le service du placard
		} else {
				p.setServices(p.getServices());
				s=serviceService.getServiceById(p.getServices().getIdService());
			
		}
		if(p.getNomPlacard()==null) 
			p.setNomPlacard(p1.getNomPlacard());
		else 
			p.setNomPlacard(p.getNomPlacard());
		   
					List<Placard> liste= s.getPlacards();
					//verifier si le nom du placard existe deja dans le service courant
					if(liste!=null) {
						for(Placard l : liste) {
							if(l.getNomPlacard().equals((p.getNomPlacard()))  && l.getIdPlacard()!=p.getIdPlacard() ) { // "possibilié de rester dans le mm repertoire et donc on compare le placard avec d'autres placards"
								 throw new RuntimeException("Le nom du placard existe déjà");
							}
						}
					}
			File source = new File(p1.getServices().getNomService(),p1.getNomPlacard());

			//renommer le placard
			newdir = new File(p1.getServices().getNomService(),p.getNomPlacard());

			if (source.renameTo(newdir)) {
			    System.out.println("Directory renamed successfully");
			} else {
			    System.out.println("Failed to rename directory");
			}
		p.setNombreCases(p1.getNombreCases());
		//le repertoire du placard
		File sourceFolder = newdir;
		File destinationFolder = new File(s.getNomService(),p.getNomPlacard());
		try {
			Files.move(sourceFolder.toPath(),destinationFolder.toPath(),StandardCopyOption.REPLACE_EXISTING);
			System.out.println("Directory moved successfully");
		}
		catch (IOException e) {
			 throw new RuntimeException("Issue in moving the directory");
        }
		if(p.getServices().getIdService()!=p1.getServices().getIdService()) {
			s=serviceService.getServiceById(p1.getServices().getIdService());
			s.setNbrPlacards(s.getNbrPlacards()-1);
			s.setNbrCases(s.getNbrCases()-p1.getNombreCases());
			serviceService.updateService(s);
			//le nouveau service
			p.setServices(p.getServices());
			s=serviceService.getServiceById(p.getServices().getIdService());
			s.setNbrPlacards(s.getNbrPlacards()+1);
			s.setNbrCases(s.getNbrCases()+p1.getNombreCases());
			serviceService.updateService(s);
		}
		placardService.updatePlacard(p);
		return p;
	}
	@RequestMapping(value = "/placard/{id}", method = RequestMethod.DELETE)
	public void deletePlacard(@PathVariable Long id) {
		Placard p=placardService.getPlacardById(id);
		Services s= p.getServices(); 
		placardStorageLocation=s.getNomService()+"/"+p.getNomPlacard();
		
		//le repertoire du placard
		fileStoragePath = Paths.get(placardStorageLocation).toAbsolutePath().normalize();
			try {
		   //supprimer les fichier, les classeurs et les cases qui appartiennent au placard
			List<Cases> cases = p.getCases();
			if(cases != null) { //si le placard contient des cases
				for(Cases c: cases){ 
					List<Classeur> classeurs=c.getClasseurs(); //pour chaque case on recupere la liste des classeurs
					if(classeurs != null) { // si la liste des classeur n'est pas null on verifie si elle contient des fichiers
		            	for(Classeur classeur: classeurs){
		            		List<Fichier> fichiers=classeur.getFichiers();
		            		if(fichiers !=null) { //si la liste des fichiers n'est pas null
		            			for(Fichier fichier : fichiers) { //supprimer chaque fichier de la liste
			            			fichierStorageLocation=s.getNomService()+"/"+p.getNomPlacard()+"/"+c.getNomCase()+"/"+classeur.getNomClasseur()+"/"+fichier.getNomFichier();
			            			fichierStoragePath = Paths.get(fichierStorageLocation).toAbsolutePath().normalize();
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
		            		classeurStorageLocation=s.getNomService()+"/"+p.getNomPlacard()+"/"+c.getNomCase()+"/"+classeur.getNomClasseur();
		            		classeurStoragePath = Paths.get(classeurStorageLocation).toAbsolutePath().normalize();
		            		try {
		            			//supprimer le classeur depuis son emplacement dans le dossier de stockage
		                        Files.delete(classeurStoragePath);
		                      //supprimer le classeur de la base de donné
		                        classeurService.deleteClasseur(classeur.getIdClasseur());
		                    	}
		            		catch (IOException e) {
		                        throw new RuntimeException("oooh Issue in deleting the directory");
		                    	}
		            	}
			       }
					caseStorageLocation=s.getNomService()+"/"+p.getNomPlacard()+"/"+c.getNomCase();
					caseStoragePath = Paths.get(caseStorageLocation).toAbsolutePath().normalize();
            		try {
            			//supprimer la case depuis son emplacement dans le dossier de stockage
                        Files.delete(caseStoragePath );
                      //supprimer la case depuis la base de donné
                        caseService.deleteCase(c.getIdCase());
                    	}
            		catch (IOException e) {
                        throw new RuntimeException("Issue in deleting the directory");
                    	}
				}
			}
            //supprimer le placard depuis son emplacement dans le dossier de stockage
            Files.delete(fileStoragePath);
            //supprimer le placard depuis la base de données
            placardService.deletePlacard(id);
            //modifier le nombre des placards dans la base de données
            s.setNbrPlacards(s.getNbrPlacards()-1);
            s.setNbrCases(s.getNbrCases()-p.getNombreCases());
            serviceService.updateService(s);
			} catch (IOException e) {
            throw new RuntimeException("Issue in deleting the directory");
        }
	}
}
