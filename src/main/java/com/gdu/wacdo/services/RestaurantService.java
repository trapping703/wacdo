package com.gdu.wacdo.services;

import com.gdu.wacdo.model.Restaurant;
import com.gdu.wacdo.repositories.RestaurantRepository;
import com.gdu.wacdo.dto.response.ReponseService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

import static com.gdu.wacdo.status.CodeReponse.*;
import static com.gdu.wacdo.dto.response.ReponseService.reponse;

@Service
@Slf4j
public class RestaurantService {

    private final RestaurantRepository restaurantRepository;

    public RestaurantService(RestaurantRepository restaurantRepository) {
        this.restaurantRepository = restaurantRepository;
    }

    public ReponseService<Restaurant> save(Restaurant restaurant) {
        try {
            return reponse(OK, restaurantRepository.save(restaurant));
        } catch (Exception e) {
            return reponse(ERROR, restaurant, e);
        }
    }

    public ReponseService<List<Restaurant>> findAll() {
        return reponse(OK, restaurantRepository.findAll());
    }

    public ReponseService findById(int id) {
        try {
            Optional<Restaurant> restaurant = restaurantRepository.findById(id);
            if(restaurant.isPresent()) {
                return reponse(OK, restaurant.get());
            }else{
                return reponse(IDK, id);
            }
        } catch (Exception e) {
            return reponse(ERROR, id, e);
        }
    }

    public ReponseService<Integer> delete(int id) {
        try {
            restaurantRepository.deleteById(id);
            return reponse(OK, id);
        } catch (Exception e) {
            return reponse(ERROR, id, e);
        }
    }

    public ReponseService findByNom(String name) {
        try {
            return reponse(OK, restaurantRepository.findByNom(name));
        } catch (Exception e) {
            return reponse(ERROR, name, e);
        }
    }

    public ReponseService findByVille(String ville) {
        try {
            return reponse(OK, restaurantRepository.findByVille(ville));
        } catch (Exception e) {
            return reponse(ERROR, ville, e);
        }
    }

    public ReponseService findByCodePostal(int codePostal) {
        try {
            return reponse(OK, restaurantRepository.findByCodePostal(codePostal));
        } catch (Exception e) {
            return reponse(ERROR, codePostal, e);
        }
    }
}
