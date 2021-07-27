package com.example.demo.respository;
import com.example.demo.entities.Categorie;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.JpaRepository;

@Repository
public interface CategorieRepository extends JpaRepository <Categorie, Long>{

}
