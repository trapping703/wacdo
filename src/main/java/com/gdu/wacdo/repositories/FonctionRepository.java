package com.gdu.wacdo.repositories;

import com.gdu.wacdo.model.Fonction;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface FonctionRepository extends JpaRepository<Fonction, Integer> {

    List<Fonction> findByLibelleContaining(String libelle);
}
