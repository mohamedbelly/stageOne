package com.example.demo.respository;
import com.example.demo.entities.Cases;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.JpaRepository;

@Repository
public interface CaseRepository extends JpaRepository <Cases, Long>{

}
