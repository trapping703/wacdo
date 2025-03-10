package com.gdu.wacdo.repositories;

import com.gdu.wacdo.model.Employe;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;
import java.util.Optional;

public interface EmployeRepository extends JpaRepository<Employe, Integer> {

    Optional<Employe> findByEmail(String email);

    @Query("select a from Employe a " +
            "where (:nom is null or a.nom like %:nom%) " +
            "and (:prenom is null or a.prenom like %:prenom%) " +
            "and (:email is null or a.email like %:email%)")
    List<Employe> getEmployesPourRecherche(String nom, String prenom, String email);

}
