package com.example.demo.respository;
import com.example.demo.entities.Placard;
import org.springframework.stereotype.Repository;
import org.springframework.data.jpa.repository.JpaRepository;

@Repository
public interface PlacardRepository extends JpaRepository <Placard, Long>{

}
