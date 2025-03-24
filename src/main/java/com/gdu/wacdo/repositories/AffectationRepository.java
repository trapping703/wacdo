package com.gdu.wacdo.repositories;

import com.gdu.wacdo.model.Affectation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;
import java.util.List;

public interface AffectationRepository extends JpaRepository<Affectation, Integer> {

    @Query("select a from Affectation a inner join Restaurant b on a.restaurant.id=b.id " +
            "where (:ville is null or b.ville like %:ville%) " +
            "and (cast(:dateDebut as date) is null or a.dateDebut=:dateDebut) " +
            "and (cast(:dateFin as date) is null or a.dateFin=:dateFin) " +
            "and (:fonction_id is null or :fonction_id = 0 or a.fonction.id=:fonction_id)")
    List<Affectation> findAffectationsPourRechercheVueListeAffection(String ville, LocalDate dateDebut, LocalDate dateFin, int fonction_id);

    @Query("select a from Affectation a inner join Restaurant b on a.restaurant.id=b.id " +
            "where (cast(:dateDebut as date) is null or a.dateDebut=:dateDebut) " +
            "and (:fonction_id is null or :fonction_id = 0 or a.fonction.id=:fonction_id)"+
            "and (:employe_id is null  or a.employe.id=:employe_id)")
    List<Affectation> findAffectationsPourRechercheVueDetailsEmploye(LocalDate dateDebut, int fonction_id, int employe_id);

}
