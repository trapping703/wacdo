package com.gdu.wacdo.services;

import com.gdu.wacdo.dto.form.RechercheRestaurant;
import com.gdu.wacdo.dto.response.ReponseService;
import com.gdu.wacdo.model.Restaurant;
import com.gdu.wacdo.repositories.RestaurantRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

import static com.gdu.wacdo.dto.response.ReponseService.reponse;
import static com.gdu.wacdo.status.CodeReponse.*;

@Service
@Slf4j
public class RestaurantService {

    private final RestaurantRepository restaurantRepository;

    public RestaurantService(RestaurantRepository restaurantRepository) {
        this.restaurantRepository = restaurantRepository;
    }

    public ReponseService save(Restaurant restaurant) {
        try {
            restaurantRepository.save(restaurant);
            if (restaurant.getId() != null) {
                return reponse(OK, restaurant);
            } else {
                return reponse(ERROR, restaurant);
            }
        } catch (Exception e) {
            return reponse(ERROR, restaurant, e);
        }
    }

    public ReponseService findAll() {
        return reponse(OK, restaurantRepository.findAll());
    }

    public ReponseService findById(int id) {
        try {
            Optional<Restaurant> restaurant = restaurantRepository.findById(id);
            return restaurant.map(value -> reponse(OK, value)).orElseGet(() -> reponse(EMPTY, id));
        } catch (Exception e) {
            return reponse(ERROR, id, e);
        }
    }

    /**
     * Utilisé pour la recherche des restaurants pour la vue /restaurants
     */
    public ReponseService findByRechercheRestaurant(RechercheRestaurant rechercheRestaurant) {
        try {
            List<Restaurant> restaurants = getRestaurantsPourRecherche(rechercheRestaurant);
            if (!restaurants.isEmpty()) {
                return reponse(OK, restaurants);
            } else {
                return reponse(EMPTY, rechercheRestaurant);
            }
        } catch (Exception e) {
            return reponse(ERROR, rechercheRestaurant, e);
        }
    }

    /**
     * Appelle le repository avec null pour le code postal si le string est vide, sinon il est convertit.
     */
    private List<Restaurant> getRestaurantsPourRecherche(RechercheRestaurant rechercheRestaurant) {
        if (rechercheRestaurant.getCodePostal().isEmpty()) {
            return restaurantRepository.getRestaurantsPourRecherche(rechercheRestaurant.getNom(), rechercheRestaurant.getVille(), null);
        } else {
            return restaurantRepository.getRestaurantsPourRecherche(rechercheRestaurant.getNom(), rechercheRestaurant.getVille(), Integer.valueOf(rechercheRestaurant.getCodePostal()));
        }

    }
}
