package com.gdu.wacdo.controllers;

import com.gdu.wacdo.dto.response.ReponseService;
import com.gdu.wacdo.model.Restaurant;
import com.gdu.wacdo.services.RestaurantService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
@Slf4j
public class RestaurantController {

    private final RestaurantService restaurantService;

    public RestaurantController(RestaurantService restaurantService) {
        this.restaurantService = restaurantService;
    }

    @GetMapping("/restaurants")
    public String restaurants(Model model) {
        model.addAttribute("restaurants", restaurantService.findAll().getData());
        return "restaurants";
    }

    @GetMapping("/restaurant/{id}")
    public String restaurant(Model model, @PathVariable int id) {
        ReponseService reponse = restaurantService.findById(id);
        if (reponse.isOk()) {
            model.addAttribute("restaurant", reponse.getData());
            return "restaurant";
        } else {
            return "index";
        }
    }
}
