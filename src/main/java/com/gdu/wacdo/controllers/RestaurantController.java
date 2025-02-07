package com.gdu.wacdo.controllers;

import com.gdu.wacdo.model.Restaurant;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;
import java.util.stream.Stream;

@Controller
public class RestaurantController {

    private final List<Restaurant> restaurants;

    public RestaurantController() {
        restaurants = Stream.iterate(0, n -> n + 1)
                .limit(10)
                .map(i -> new Restaurant(i,"nom" + i.toString(), "adresse" + i.toString(),10 + i,  "ville" + i.toString()))
                .toList();
    }


    @GetMapping("/restaurants")
    public String restaurants(Model model) {


        model.addAttribute("restaurants", restaurants);

        return "restaurants";
    }

    @GetMapping("/restaurant/{id}")
    public String restaurant(Model model, @PathVariable int id) {
        Restaurant resto = restaurants.stream().filter(r -> r.getId() == id).findAny().get();

        model.addAttribute("restaurant", resto);
        return "restaurant";
    }
}
