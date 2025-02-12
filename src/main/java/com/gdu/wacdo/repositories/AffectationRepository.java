package com.gdu.wacdo.repositories;

import com.gdu.wacdo.model.Affectation;
import com.gdu.wacdo.model.Fonction;
import org.springframework.data.jpa.repository.JpaRepository;

import java.time.LocalDate;
import java.util.List;

public interface AffectationRepository extends JpaRepository<Affectation, Integer> {

    List<Affectation> findByFonction(Fonction fonction);

    List<Affectation> findByDateDebut(LocalDate dateDebut);

    List<Affectation> findByDateFin(LocalDate dateFin);
}
