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

    public ReponseService save(Restaurant restaurant) {
        try {
            return reponse(OK, restaurantRepository.save(restaurant));
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

    public ReponseService delete(int id) {
        try {
            restaurantRepository.deleteById(id);
            return reponse(OK, id);
        } catch (Exception e) {
            return reponse(ERROR, id, e);
        }
    }
}
