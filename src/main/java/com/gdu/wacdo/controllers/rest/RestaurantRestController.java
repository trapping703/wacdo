package com.gdu.wacdo.controllers.rest;

import com.gdu.wacdo.model.Restaurant;
import com.gdu.wacdo.services.RestaurantService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.beans.factory.annotation.Autowired;
import org.springframework.http.HttpStatus;
import org.springframework.http.ResponseEntity;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@RestController
@Slf4j
public class RestaurantRestController {

    @Autowired
    private RestaurantService restaurantService;

    @GetMapping("/restaurants")
    public List<Restaurant> getAllRestaurants() {
        return (List<Restaurant>) restaurantService.findAll().getData();
    }

    @GetMapping("/restaurant/{id}")
    public Restaurant getRestaurant(@PathVariable int id) {
        return (Restaurant) restaurantService.findById(id).getData();
    }

    @PostMapping("/restaurant")
    public ResponseEntity<Restaurant> createRestaurant(@RequestBody Restaurant restaurant) {
        return new ResponseEntity<>((Restaurant) restaurantService.save(restaurant).getData(), HttpStatus.CREATED);
    }

    @PatchMapping("/restaurant")
    public ResponseEntity<Restaurant> updateRestaurant(@RequestBody Restaurant restaurant) {
        return new ResponseEntity<>((Restaurant) restaurantService.save(restaurant).getData(), HttpStatus.OK);
    }
}
