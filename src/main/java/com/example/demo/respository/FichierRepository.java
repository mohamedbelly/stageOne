package com.example.demo.respository;
import com.example.demo.entities.Fichier;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.JpaRepository;

@Repository
public interface FichierRepository extends JpaRepository <Fichier, Long>{
 
}
