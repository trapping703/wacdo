package com.gdu.wacdo.controllers;

import com.gdu.wacdo.dto.model.RestaurantDTO;
import com.gdu.wacdo.dto.response.ReponseService;
import com.gdu.wacdo.services.RestaurantService;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@Controller
@Slf4j
public class RestaurantController {

    private final RestaurantService restaurantService;
    private final ModelMapper modelMapper;

    public RestaurantController(RestaurantService restaurantService, ModelMapper modelMapper) {
        this.restaurantService = restaurantService;
        this.modelMapper = modelMapper;
    }

    @GetMapping("/restaurants")
    public String restaurants(Model model) {
        List<RestaurantDTO> restaurantDTOS = restaurantService.findAll().getData().stream()
                .map(r -> modelMapper.map(r, RestaurantDTO.class))
                .toList();
        model.addAttribute("restaurants", restaurantDTOS);
        return "restaurants";
    }

    @GetMapping("/restaurant/{id}")
    public String restaurant(Model model, @PathVariable int id) {
        ReponseService reponse = restaurantService.findById(id);
        if (reponse.isOk()) {
            model.addAttribute("restaurant", modelMapper.map(reponse.getData(), RestaurantDTO.class));
            return "restaurant";
        } else {
            return "index";
        }
    }
}
