package com.example.demo.respository;
import com.example.demo.entities.Services;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.JpaRepository;

@Repository
public interface ServicesRepository extends JpaRepository <Services, Long>{

}
