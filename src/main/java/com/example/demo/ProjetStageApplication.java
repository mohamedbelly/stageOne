package com.example.demo;

import org.springframework.boot.CommandLineRunner;
import org.springframework.boot.SpringApplication;
import org.springframework.boot.autoconfigure.SpringBootApplication;


import springfox.documentation.swagger2.annotations.EnableSwagger2;


@SpringBootApplication
@EnableSwagger2
public class ProjetStageApplication implements CommandLineRunner{
	
	//appel aux interface
//	@Autowired 
//	CaseRepository caseRepository ;
//	@Autowired 
//	CategorieRepository categorieRepository;
//	@Autowired 
//	ClasseurRepository classeurRepository;
//	@Autowired 
//	FichierRepository fichierRepository;
//	@Autowired 
//	ServicesRepository serviceRepository;
//	@Autowired 
//	PlacardRepository placardRepository;
//	
//	//appel au services
//	@Autowired
//	CaseService caseService; 
//	@Autowired
//	CategorieService categorieService; 
//	@Autowired
//	ClasseurService classeurService; 
//	@Autowired
//	FichierService fichierService; 
//	@Autowired
//	ServicesService sevicesService; 
//	@Autowired
//	PlacardService placardService;
	
	public static void main(String[] args) {
		SpringApplication.run(ProjetStageApplication.class, args);
	}
	@Override
	public void run(String... args) throws Exception{
		//creation des categories
//		Categorie categorie1= new Categorie("cat1"); 
//		Categorie catrecived1 = categorieRepository.save(categorie1);
//		Categorie categorie2= new Categorie("cat2");
//		Categorie catrecived2 = categorieRepository.save(categorie2);
//		Categorie categorie3= new Categorie("cat3");
//		Categorie catrecived3 = categorieRepository.save(categorie3);
		
		//creation des services
//		Services services1= new Services("Service1"); 
//		Services servicesRecived1 = serviceRepository.save(services1);
//		Services services2= new Services("Service2");
//		Services servicesRecived2 = serviceRepository.save(services2);
//		Services services3= new Services("Service3");
//		Services servicesRecived3 = serviceRepository.save(services3);
		
		//creation des placards
//		Placard placard1= new Placard("Placard1",(long) 20); 
//		placard1.setServices(servicesRecived1);
//		Placard placardRecived1 = placardRepository.save(placard1);
//		Placard placard2= new Placard("Placard2",(long) 30);
//		placard2.setServices(servicesRecived2);
//		Placard placardRecived2 = placardRepository.save(placard2);
//		Placard placard3= new Placard("Placard3",(long) 10);
//		placard3.setServices(servicesRecived3);
//		Placard placardRecived3 = placardRepository.save(placard3);
//		
		//creation des cases
//		Cases case1= new Cases("Case1"); 
//		case1.setPlacards(placardRecived1);
//		Cases caseRecived1 = caseRepository.save(case1);
//		Cases case2= new Cases("Case2");
//		case2.setPlacards(placardRecived2);
//		Cases caseRecived2 = caseRepository.save(case2);
//		Cases case3= new Cases("Case3");
//		case3.setPlacards(placardRecived3);
//		Cases caseRecived3 = caseRepository.save(case3);
				
		//creation des classeurs
//		Classeur classeur1= new Classeur("Classeur1"); 
//		classeur1.setCases(caseRecived1);
//		Classeur classeurRecived1 = classeurRepository.save(classeur1);
//
//		Classeur classeur2= new Classeur("Classeur2"); 
//		classeur2.setCases(caseRecived2);
//		Classeur classeurRecived2 = classeurRepository.save(classeur2);
//				
//		Classeur classeur3= new Classeur("Classeur3"); 
//		classeur3.setCases(caseRecived3);
//		Classeur classeurRecived3 = classeurRepository.save(classeur3);
		
		//creation des Fichiers
//		Fichier fichier1= new Fichier();
//		fichier1.setCategorie(catrecived1);
//		fichier1.setClasseur(classeurRecived1);
//		fichierService.addFichier("nom1","type1","src/file1");
//		
//		Fichier fichier2= new Fichier("nom2","type2","src/file2");
//		fichier2.setCategorie(catrecived2);
//		fichier2.setClasseur(classeurRecived2);
//		fichierService.addFichier(fichier2);
//		
//		Fichier fichier3= new Fichier("nom3","type3","src/file3");
//		fichier3.setCategorie(catrecived3);
//		fichier3.setClasseur(classeurRecived3);
//		fichierService.addFichier(fichier3);

		
	}

}
