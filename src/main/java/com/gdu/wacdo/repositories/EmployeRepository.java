package com.gdu.wacdo.repositories;

import com.gdu.wacdo.model.Affectation;
import com.gdu.wacdo.model.Employe;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface EmployeRepository extends JpaRepository<Employe, Integer> {

    List<Employe> findByNom(String nom);
    List<Employe> findByPrenom(String prenom);
    List<Employe> findByEmail(String email);

}
