package com.gdu.wacdo.controllers;

import com.gdu.wacdo.dto.form.RechercheRestaurant;
import com.gdu.wacdo.dto.model.RestaurantDTO;
import com.gdu.wacdo.dto.response.ReponseService;
import com.gdu.wacdo.model.Restaurant;
import com.gdu.wacdo.services.RestaurantService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;

import static java.util.Collections.emptyList;

@Controller
@Slf4j
public class RestaurantController {

    private final RestaurantService restaurantService;
    private final ModelMapper modelMapper;

    public RestaurantController(RestaurantService restaurantService, ModelMapper modelMapper) {
        this.restaurantService = restaurantService;
        this.modelMapper = modelMapper;
    }

    @GetMapping("/listeRestaurants")
    public String restaurants(Model model) throws Exception {
        ReponseService reponse = restaurantService.findAll();
        if (reponse.isOk()) {
            List<RestaurantDTO> restaurantDTOS = ((List<Restaurant>) reponse.getData()).stream()
                    .map(r -> modelMapper.map(r, RestaurantDTO.class))
                    .toList();
            model.addAttribute("restaurants", restaurantDTOS);
            return "restaurants";
        }
        throw reponse.getException();
    }

    @GetMapping("/detailRestaurant/{id}")
    public String restaurant(Model model, @PathVariable int id) {
        ReponseService reponse = restaurantService.findById(id);
        if (reponse.isOk()) {
            model.addAttribute("restaurant", modelMapper.map(reponse.getData(), RestaurantDTO.class));
            return "restaurant";
        } else {
            return "index";
        }
    }


    @PostMapping("/rechercheRestaurants")
    public String rechercheRestaurants(@Valid @ModelAttribute("rechercheRestaurants") RechercheRestaurant rechercheRestaurants, BindingResult result, Model model) throws Exception {
        ReponseService reponseService = restaurantService.findByRechercheRestaurant(rechercheRestaurants);
        if (result.hasErrors()) {
            return "restaurants";
        }
        return switch (reponseService.getStatus()) {
            case OK -> {
                mappingListeRestaurantQuandRechercheOK(rechercheRestaurants, model, reponseService);
                yield "restaurants";
            }
            case EMPTY -> {
                mappingQuandRechecheEmpty(rechercheRestaurants, model);
                yield "restaurants";
            }
            case ERROR -> throw reponseService.getException();
        };
    }

    /**
     * Réattribut l'objet de recherche de restaurant, fournit la liste de restaurant trouvé par la recherche.
     */
    private void mappingListeRestaurantQuandRechercheOK(RechercheRestaurant rechercheRestaurants, Model model, ReponseService reponseService) {
        List<RestaurantDTO> restaurantDTOS = ((List<Restaurant>) reponseService.getData()).stream()
                .map(restaurant -> modelMapper.map(restaurant, RestaurantDTO.class))
                .toList();
        model.addAttribute("rechercheRestaurants", rechercheRestaurants);
        model.addAttribute("restaurants", restaurantDTOS);
    }

    /**
     * Réattribut l'objet de recherche de restaurant, fournit une liste vide de restaurant et passe le message d'erreur.
     */
    private void mappingQuandRechecheEmpty(RechercheRestaurant rechercheRestaurants, Model model) throws Exception {
        model.addAttribute("rechercheRestaurants", rechercheRestaurants);
        model.addAttribute("restaurants", emptyList());
        model.addAttribute("recherchevide", "Aucun restaurant trouvé");
    }

    @ModelAttribute(value = "rechercheRestaurants")
    private RechercheRestaurant getrechercheRestaurant() {
        return new RechercheRestaurant();
    }
}
