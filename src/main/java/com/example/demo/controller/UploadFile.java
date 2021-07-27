package com.example.demo.controller;
import java.io.File;
import java.io.IOException;
import java.net.MalformedURLException;
import java.nio.file.Files;
import java.nio.file.Path;
import java.nio.file.Paths;
import java.nio.file.StandardCopyOption;
import java.util.ArrayList;
import java.util.Arrays;
import java.util.List;

import org.springframework.web.multipart.MultipartFile;

import com.example.demo.entities.Cases;
import com.example.demo.entities.Classeur;
import com.example.demo.entities.Fichier;
import com.example.demo.entities.Placard;
import com.example.demo.entities.Services;
import com.example.demo.services.CategorieService;
import com.example.demo.services.ClasseurService;
import com.example.demo.services.FichierService;


import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.core.io.Resource;
import org.springframework.core.io.UrlResource;
import org.springframework.http.HttpHeaders;
import org.springframework.http.MediaType;
import org.springframework.http.ResponseEntity;
import org.springframework.util.StringUtils;

import org.springframework.web.bind.annotation.*;
import org.springframework.web.servlet.support.ServletUriComponentsBuilder;
import javax.servlet.http.HttpServletRequest;
import javax.validation.Valid;

@RestController
public class UploadFile {
	@Autowired
    private FichierService repo;
	@Autowired
    private CategorieService categorieService;
	@Autowired
    private ClasseurService classeurService;
    private Long dbNombreFichiers;
    private String fileStorageLocation;
    
    //upload single file or multiple files in the same moment
//    public UploadFile(FileStorageService fileStorageService) {
//        this.fileStorageService = fileStorageService;
//    }
    @RequestMapping(value = "/fichier/{idClasseur}&{idCategorie}" ,method = RequestMethod.POST)
    List<Fichier> addfichier(@Valid @RequestParam("fichier") MultipartFile[] fichier,
    						 @PathVariable Long idClasseur,
    						 @PathVariable Long idCategorie ){
		 
        if (fichier.length > 10) {
            throw new RuntimeException("Vous avez dépassé le nombre total des fichiers à télécharger !");
        }
        
        List<Fichier> uploadResponseList = new ArrayList<>();
        Arrays.asList(fichier)
                .stream()
                .forEach(file -> {
                	dbNombreFichiers=(long)repo.getAllFichiers().size();
                	//la location ou nous devrons stocker le fichier
                    Classeur classeur=classeurService.getClasseurById(idClasseur);
                    Cases c=classeur.getCases();
            		Placard p=c.getPlacards();
            		Services s=p.getServices(); 
            		fileStorageLocation=s.getNomService()+"/"+p.getNomPlacard()+"/"+c.getNomCase()+"/"+classeur.getNomClasseur();
            		
            		Path fileStoragePath = Paths.get(fileStorageLocation).toAbsolutePath().normalize();
                    String fileName = StringUtils.cleanPath(file.getOriginalFilename());
                    String fileStorage=fileName.substring(0,fileName.lastIndexOf(".")) +"_"+dbNombreFichiers+fileName.substring(fileName.lastIndexOf("."));
                    Path filePath = Paths.get(fileStoragePath + "\\" +  fileStorage);
                    try {
                        Files.copy(file.getInputStream(), filePath, StandardCopyOption.REPLACE_EXISTING);
                    } catch (IOException e) {
                        throw new RuntimeException("Issue in storing the file", e);
                    }
                    //obtenir le nombre de fichiers existe dans la base de données
                	dbNombreFichiers=(long)repo.getAllFichiers().size();
                   
                    //Ecrire avant l'extention l'id du fichier pour eviter la redondance
                    fileName=fileName.substring(0,fileName.lastIndexOf(".")) +"_"+dbNombreFichiers+fileName.substring(fileName.lastIndexOf("."));
                    //preciser l'url
                    String url = ServletUriComponentsBuilder.fromCurrentContextPath()
                            .path("/")
                            .path(fileName)
                            .toUriString();

                    String fileType = file.getContentType();
                    //creer un fichier
                    Fichier response = new Fichier(fileName,fileType, url);
                    response.setCategorie(categorieService.getCategorieById(idCategorie));
                    response.setClasseur(classeurService.getClasseurById(idClasseur));
                    //deplacer le fichier creer à notre dossier
                    File sourceFile = new File(fileName);
                    File destFile = new File(url);
                     
                    if (sourceFile.renameTo(destFile)) {
                        System.out.println("File moved successfully");
                    } else {
                        System.out.println("Failed to move file");
                    }
                   
                    uploadResponseList.add(response);
                    repo.addFichier(response);
                });
        return uploadResponseList;
    }
    @GetMapping("/download")
    ResponseEntity<Resource> telechargerFichier(@RequestBody Fichier fichier, HttpServletRequest request) {
    	Fichier f=repo.getFichierById(fichier.getIdFichier());

		//la location ou nous devrons stocker le fichier
        Classeur classeur=f.getClasseur();
        Cases c=classeur.getCases();
 		Placard p=c.getPlacards();
 		Services s=p.getServices(); 
 		fileStorageLocation=s.getNomService()+"/"+p.getNomPlacard()+"/"+c.getNomCase()+"/"+classeur.getNomClasseur();
		Path path = Paths.get(fileStorageLocation).toAbsolutePath().resolve(f.getNomFichier());
		
	        Resource resource;
	        try {
	            resource = new UrlResource(path.toUri());

	        } catch (MalformedURLException e) {
	           throw new RuntimeException("Issue in reading the file", e);
	        }
	        
	        if(resource.exists() && resource.isReadable()){
	        	String mimeType;
	            try {
	                mimeType = request.getServletContext().getMimeType(resource.getFile().getAbsolutePath());
	            } catch (IOException e) {
	                mimeType = MediaType.APPLICATION_OCTET_STREAM_VALUE; //afficher le fichier peut import son extention
	            }
	            mimeType = mimeType == null ? MediaType.APPLICATION_OCTET_STREAM_VALUE : mimeType;
	            return ResponseEntity.ok()
	                    .contentType(MediaType.parseMediaType(mimeType))
//	                   attachment permet de telecharger lorsque on copie le lien de dowload
	                    .header(HttpHeaders.CONTENT_DISPOSITION, "attachment;fileName=" + resource.getFilename())
	                    .body(resource);
	        }else{
	            throw new RuntimeException("the file doesn't exist or not readable");
	        }
        
    }
    
	 @GetMapping("/fichier")
	    ResponseEntity<Resource> consulterFichier(@RequestBody Fichier fichier, HttpServletRequest request) {
		 Fichier f=repo.getFichierById(fichier.getIdFichier());

		//la location ou nous devrons stocker le fichier
        Classeur classeur= f.getClasseur();
        Cases c= classeur.getCases();
 		Placard p= c.getPlacards();
 		Services s= p.getServices(); 
 		fileStorageLocation=s.getNomService()+"/"+p.getNomPlacard()+"/"+c.getNomCase()+"/"+classeur.getNomClasseur();
		Path path = Paths.get(fileStorageLocation).toAbsolutePath().resolve(f.getNomFichier());
		
		
	        Resource resource;
	        try {
	            resource = new UrlResource(path.toUri());

	        } catch (MalformedURLException e) {
	           throw new RuntimeException("Issue in reading the file", e);
	        }
	        
	        if(resource.exists() && resource.isReadable()){
	        	String mimeType;
		        try {
		            mimeType = request.getServletContext().getMimeType(resource.getFile().getAbsolutePath());
		        } catch (IOException e) {
		            mimeType = MediaType.APPLICATION_OCTET_STREAM_VALUE; //afficher le fichier peut import son extention
		        }
		        mimeType = mimeType == null ? MediaType.APPLICATION_OCTET_STREAM_VALUE : mimeType;
		        return ResponseEntity.ok()
		                .contentType(MediaType.parseMediaType(mimeType))
//		                attachment permet de telecharger lorsque on copie le lien de dowload
//		                .header(HttpHeaders.CONTENT_DISPOSITION, "attachment;fileName=" + resource.getFilename())
		                .header(HttpHeaders.CONTENT_DISPOSITION, "inline;fileName=" + resource.getFilename())
		                .body(resource);
	        }else{
	            throw new RuntimeException("the file doesn't exist or not readable");
	        }
	
	    }
}
