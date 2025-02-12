package com.gdu.wacdo.repositories;

import com.gdu.wacdo.model.Affectation;
import com.gdu.wacdo.model.Fonction;
import org.springframework.data.jpa.repository.JpaRepository;

public interface FonctionRepository extends JpaRepository<Fonction, Integer> {

    Fonction findByLibelle(String libelle);
}
