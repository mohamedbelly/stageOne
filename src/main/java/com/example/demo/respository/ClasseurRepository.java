package com.example.demo.respository;
import com.example.demo.entities.Classeur;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.JpaRepository;

@Repository
public interface ClasseurRepository extends JpaRepository <Classeur, Long>{
 
}
