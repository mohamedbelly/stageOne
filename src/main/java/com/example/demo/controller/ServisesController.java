package com.example.demo.controller;
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

public class ServisesController {
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
	private String placardStorageLocation;
	private String fichierStorageLocation;
	private String caseStorageLocation;
	private String classeurStorageLocation;
	private Path fileStoragePath;
	private Path caseStoragePath;
	private Path fichierStoragePath;
	private Path classeurStoragePath;
	private Path placardStoragePath;
	private String serviceStorageLocation;
	@RequestMapping(value = "/services", method = RequestMethod.GET)
	public List<Services> getAllServices() {
		return serviceService.getAllServices();
	}
	//ajouter service
//   {
//	    "nomService": "service",
//			 "nbrPlacards":2,
//			 "nbrCases":2
//	  }
	@RequestMapping(value = "/service" ,method = RequestMethod.POST)
	public Services addService(@Valid @RequestBody Services s, BindingResult bindingResult ){
		if(bindingResult.hasErrors()) {
			//return FormService;
		}   
		Placard placard;
		Cases cases;
		List<Services> liste= serviceService.getAllServices();
		//verifier si le nom du service existe deja dans la base de donnée
		if(liste != null) {
			for(Services l : liste) {
				if(l.getNomService().equals((s.getNomService()))) {
					s.setNomService(s.getNomService()+"_"+liste.size());
					break;
				}
			}
		}
			//creation du repertoire qui port le nom du service
			s.setNbrPlacards(s.getNbrPlacards());
			Long nbrCase=s.getNbrCases();
			s.setNbrCases(s.getNbrCases()*s.getNbrPlacards()); // le nombre totale des cases qui vont etre creer dans le service
			serviceService.addService(s);
			serviceStorageLocation = s.getNomService();
	        fileStoragePath = Paths.get(serviceStorageLocation).toAbsolutePath().normalize();
	        try {
	            Files.createDirectories(fileStoragePath);
	        } catch (IOException e) {
	            throw new RuntimeException("Issue in creating the directory");
	        }
		
			for(int j=1;j<=s.getNbrPlacards();j++) {
				placard= new Placard();
				placard.setNombreCases(nbrCase);
				placard.setServices(s);
				placard.setNomPlacard("Placard"+j);
				placardService.addPlacard(placard);
				
				//creation du repertoire qui port le nom du placard
				placardStorageLocation = s.getNomService()+"/"+placard.getNomPlacard();
		        fileStoragePath = Paths.get(placardStorageLocation).toAbsolutePath().normalize();
		        try {
		            Files.createDirectories(fileStoragePath);
		        } catch (IOException e) {
		            throw new RuntimeException("Issue in creating file directory");
		        }
				
				for(int k=1;k<=nbrCase;k++) {
					cases=new Cases();
					cases.setPlacards(placard);
					cases.setNom("Case"+k);
					caseStorageLocation = placardStorageLocation+"/"+cases.getNomCase();
					caseService.addCase(cases);
					
					//creation du repertoire qui port le nom de la case
			        fileStoragePath = Paths.get(caseStorageLocation).toAbsolutePath().normalize();
			        try {
			            Files.createDirectories(fileStoragePath);
			        } catch (IOException e) {
			            throw new RuntimeException("Issue in creating the directory");
			        }
				}	
		}
			return s;
	}
	@RequestMapping(value = "/service", method = RequestMethod.PUT)
	public Services editService(@RequestBody Services s) {
		Services s1=serviceService.getServiceById(s.getIdService());
		Path source = Paths.get(s1.getNomService());
		List<Services> liste= serviceService.getAllServices(); //recuperer les services 
		//verifier si le nom du service existe deja dans la base de donnée
		if(liste!=null) {
			for(Services l : liste) {
				if(l.getNomService().equals((s.getNomService()))&& l.getIdService()!=s.getIdService()) {
					 throw new RuntimeException("Le nom du service existe déjà");
				}
			}
		}
		s.setNomService(s.getNomService());
		s.setNbrPlacards(s1.getNbrPlacards());
		s.setNbrCases(s1.getNbrCases());
		serviceService.updateService(s);
		Path newdir = Paths.get(s.getNomService());
        try {
        	Files.move(source, source.resolveSibling(newdir),
                    StandardCopyOption.REPLACE_EXISTING);
        } catch (IOException e) {
            throw new RuntimeException("Issue in updating the directory");
        }
        return s;
	}
	@RequestMapping(value = "/service/{id}", method = RequestMethod.DELETE)
	public void deleteService(@PathVariable Long id) {
		Services s= serviceService.getServiceById(id); 
		serviceStorageLocation=s.getNomService();
		//le repertoire du service
		fileStoragePath = Paths.get(serviceStorageLocation).toAbsolutePath().normalize();
			try {
		   //supprimer les fichier, les classeurs, des cases et des placards qui appartiennent au service
			List<Placard> placards = s.getPlacards();
			if(placards != null) {
				for(Placard p : placards) {
					List<Cases> cases = p.getCases(); //recupere la liste des case du placard
					if(cases != null) { //si la liste des case n'est pas null
						for(Cases c: cases){ 
							List<Classeur> classeurs=c.getClasseurs(); //pour chaque case on recupere la liste des classeurs
							if(classeurs != null) { // si la liste des classeur n'est pas null on verifie si elle contient des fichiers
								for(Classeur classeur: classeurs){
									List<Fichier> fichiers=classeur.getFichiers(); //recuperer la liste des fichiers
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
				                        throw new RuntimeException("Issue in deleting the directory");
				                    	}
								}
							}
							caseStorageLocation=s.getNomService()+"/"+p.getNomPlacard()+"/"+c.getNomCase();
							caseStoragePath=Paths.get(caseStorageLocation).toAbsolutePath().normalize();
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
					placardStorageLocation=s.getNomService()+"/"+p.getNomPlacard();
					placardStoragePath=Paths.get(placardStorageLocation).toAbsolutePath().normalize();
		            try {
		            	//supprimer le placard depuis son emplacement dans le dossier de stockage
		                 Files.delete(placardStoragePath );
		                 //supprimer le placard depuis la base de donné
		                  placardService.deletePlacard(p.getIdPlacard());
		                 }
		            catch (IOException e) {
		                  throw new RuntimeException("Issue in deleting the directory");
		                 }
		        
				}
			}
	//supprimer le service depuis son emplacement dans le dossier de stockage
    Files.delete(fileStoragePath);
     //supprimer le service de la base de données
     serviceService.deleteService(id);
			} 
		catch (IOException e) {
            throw new RuntimeException("Issue in deleting the directory");
        	}
	}
}
