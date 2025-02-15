package com.gdu.wacdo.repositories;

import com.gdu.wacdo.model.Restaurant;
import org.springframework.data.jpa.repository.JpaRepository;
import org.springframework.data.jpa.repository.Query;

import java.util.List;

public interface RestaurantRepository extends JpaRepository<Restaurant, Integer> {

    @Query("select a from Restaurant a " +
            "where (:nom is null or a.nom like %:nom%) " +
            "and (:ville is null or a.ville like %:ville%) " +
            "and (:CodePostal is null or a.codePostal = :CodePostal)")
    List<Restaurant> getRestaurantsPourRecherche(String nom, String ville, int CodePostal);
}
