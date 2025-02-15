package com.gdu.wacdo.repositories;

import com.gdu.wacdo.dto.model.FonctionDTO;
import com.gdu.wacdo.model.Affectation;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.time.LocalDate;
import java.util.List;

public interface AffectationRepository extends JpaRepository<Affectation, Integer> {

    //            "and (:dateDebut is null or function('date_format', a.dateDebut, '%d, %Y, %m')=:dateDebut) " +
//                    "and (:dateFin is null or function('date_format', a.dateFin, '%d, %Y, %m')=:dateFin) " +
    @Query("select a from Affectation a inner join Restaurant b on a.restaurant.id=b.id " +
            "where (:ville is null or b.ville like %:ville%) " +
            "and (cast(:dateDebut as date) is null or a.dateDebut=:dateDebut) " +
            "and (cast(:dateFin as date) is null or a.dateFin=:dateFin) " +
            "and (:fonction_id is null or :fonction_id = 0 or a.fonction.id=:fonction_id)")
    List<Affectation> findByRechercheAffectation(String ville, LocalDate dateDebut, LocalDate dateFin, int fonction_id);
}
