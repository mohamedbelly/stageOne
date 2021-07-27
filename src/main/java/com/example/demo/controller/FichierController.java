package com.example.demo.controller;
import java.io.File;
import java.io.IOException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.List;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.util.StringUtils;
import org.springframework.web.bind.annotation.CrossOrigin;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.RequestBody;
import org.springframework.web.bind.annotation.RequestMapping;
import org.springframework.web.bind.annotation.RequestMethod;
import org.springframework.web.bind.annotation.RestController;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;

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
@CrossOrigin("*") //acceptation de tous les serveurs
public class FichierController {
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
	
	private String fichierStorageLocation;
	private Path fileStoragePath;
	
	@RequestMapping(value = "/fichiers", method = RequestMethod.GET)
	public List<Fichier> getAllFichiers() {
		return fichierService.getAllFichiers();
	}
	
	@RequestMapping(value = "/fichier", method = RequestMethod.PUT)
	public Fichier editFichier(@RequestBody Fichier f) {
		Services s;
		Placard p;
		Cases cases;
		Classeur c;
		File newdir = null;
		
		Fichier f1=fichierService.getFichierById(f.getIdFichier()); //obtenir les anciennes infos sur le fichier depuis la db
		//modification du classeur
		if(f.getClasseur()==null) {
			f.setClasseur(f1.getClasseur());
			c= classeurService.getClasseurById(f1.getClasseur().getIdClasseur()); //obtenir les infors sur la case
		} else {
			f.setClasseur(f.getClasseur());
			c= classeurService.getClasseurById(f.getClasseur().getIdClasseur()); //obtenir les infors sur le classeur
		}
		cases=c.getCases();
		p=cases.getPlacards();
		s=p.getServices();
		//modification de la categorie du fichier
		if(f.getCategorie()==null)
			f.setCategorie(f1.getCategorie());
		else
			f.setCategorie(f.getCategorie());
		//modification du nom fichier
		if(f.getNomFichier()==null) 
			f.setNomFichier(f1.getNomFichier());
		else 
			f.setNomFichier(f.getNomFichier());
		List<Fichier> liste= c.getFichiers();
		//verifier si le nom duclasseur existe deja dans le classeur donné
		if(liste!=null) {
			for(Fichier l : liste) {
				if(l.getNomFichier().equals((f.getNomFichier())) && l.getIdFichier()!=f.getIdFichier()) { // "possibilié de rester dans le mm repertoire et donc on compare le fichier avec d'autres fichiers du classeur"
				  throw new RuntimeException("Le nom du Fichier donné, existe déjà");
				}
		    }	
		}
		File source = new File(f1.getClasseur().getCases().getPlacards().getServices().getNomService(),f1.getClasseur().getCases().getPlacards().getNomPlacard());
		source= new File(source,f1.getClasseur().getCases().getNomCase());
		source= new File(source,f1.getClasseur().getNomClasseur());
		source= new File(source,f1.getNomFichier());

		//renommer le fichier
		newdir = new File(f1.getClasseur().getCases().getPlacards().getServices().getNomService(),f1.getClasseur().getCases().getPlacards().getNomPlacard());
		newdir = new File(newdir,f1.getClasseur().getCases().getNomCase());
		newdir = new File(newdir,f1.getClasseur().getNomClasseur());
		newdir = new File(newdir,f.getNomFichier());
		
		if (source.renameTo(newdir)) {		
            String fileName = StringUtils.cleanPath(f.getNomFichier());
           //preciser l'url
            String url = ServletUriComponentsBuilder.fromCurrentContextPath()
                    .path("/")
                    .path(fileName)
                    .toUriString();

            f.setPath(url);
			
		    System.out.println("Directory renamed successfully");
		 } else {
			 System.out.println("Failed to rename directory");
		 }
		if(f.getPath()==null)
			f.setPath(f1.getPath());
		f.setDateCreation(f1.getDateCreation());
		fichierService.updateFichier(f);
		//le repertoire du fichier
		File sourceFolder = newdir;
		File destinationFolder = new File(s.getNomService(),p.getNomPlacard());
		destinationFolder = new File(destinationFolder,cases.getNomCase());
		destinationFolder = new File(destinationFolder,c.getNomClasseur());
		destinationFolder = new File(destinationFolder,f.getNomFichier());
		try {
			Files.move(sourceFolder.toPath(),destinationFolder.toPath(),StandardCopyOption.REPLACE_EXISTING);
			System.out.println("Directory moved successfully");
		}
		catch (IOException e) {
			 throw new RuntimeException("Issue in moving the directory");
        }
		return f;
	}
	
	@RequestMapping(value = "/fichier/{id}", method = RequestMethod.DELETE)
	public void deleteFichier(@PathVariable Long id) {
		Fichier f=fichierService.getFichierById(id);
		Classeur classeur=f.getClasseur();
		Cases c=classeur.getCases();
		Placard p=c.getPlacards();
		Services s=p.getServices(); 
		fichierStorageLocation=s.getNomService()+"/"+p.getNomPlacard()+"/"+c.getNomCase()+"/"+classeur.getNomClasseur()+"/"+f.getNomFichier();
		fileStoragePath = Paths.get(fichierStorageLocation).toAbsolutePath().normalize();
		try {
			//supprimer le fichier depuis son emplacement dans le dossier de stockage
            Files.delete(fileStoragePath);
          //supprimer le fichier de la base de donné
            fichierService.deleteFichier(id);
        } catch (IOException e) {
            throw new RuntimeException("Issue in deleting the directory");
        }
	}
	
	@GetMapping("fichier/{nom}")
    public String searchFichier(@PathVariable("nom") String nom){
		return fichierService.searchFichier(nom);
    }
	@GetMapping("fichiers/{id}")
    public Fichier getFichierById(@PathVariable("id") Long id){
		return fichierService.getFichierById(id);
    }
}
