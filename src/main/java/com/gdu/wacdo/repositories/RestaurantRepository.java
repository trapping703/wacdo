package com.gdu.wacdo.repositories;

import com.gdu.wacdo.model.Restaurant;
import org.springframework.data.jpa.repository.JpaRepository;

import java.util.List;

public interface RestaurantRepository extends JpaRepository<Restaurant, Integer> {

    List<Restaurant> findByNom(String nom);

    List<Restaurant> findByVille(String Ville);

    List<Restaurant> findByCodePostal(int codePostal);
}
