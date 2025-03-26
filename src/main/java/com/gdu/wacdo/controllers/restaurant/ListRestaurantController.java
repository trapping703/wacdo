package com.gdu.wacdo.controllers.restaurant;

import com.gdu.wacdo.dto.form.RechercheRestaurantDTO;
import com.gdu.wacdo.dto.model.RestaurantDTO;
import com.gdu.wacdo.dto.response.ReponseService;
import com.gdu.wacdo.model.Restaurant;
import com.gdu.wacdo.services.RestaurantService;
import jakarta.validation.Valid;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;

import static java.util.Collections.emptyList;

@Controller
public class ListRestaurantController {

    private final RestaurantService restaurantService;
    private final ModelMapper modelMapper;

    public ListRestaurantController(RestaurantService restaurantService, ModelMapper modelMapper) {
        this.restaurantService = restaurantService;
        this.modelMapper = modelMapper;
    }

    /**
     * Page de la liste d'affectation
     */
    @GetMapping("/listeRestaurants")
    public String restaurants(Model model) throws Exception {
        ReponseService<List<Restaurant>> reponse = restaurantService.findAll();
        if (!reponse.isError()) {
            List<RestaurantDTO> restaurantDTOS = reponse.getObjetRetour().stream()
                    .map(r -> modelMapper.map(r, RestaurantDTO.class))
                    .toList();
            model.addAttribute("restaurants", restaurantDTOS);
            return "restaurant/restaurants";
        }
        throw reponse.getException();
    }

    /**
     * Gestion de la recherche de restaurant
     */
    @PostMapping("/rechercheRestaurants")
    public String rechercheRestaurants(@Valid @ModelAttribute("rechercheRestaurants") RechercheRestaurantDTO rechercheRestaurants, BindingResult result, Model model) throws Exception {
        if (result.hasErrors()) {
            return "restaurant/restaurants";
        }
        ReponseService<List<Restaurant>> reponseService = restaurantService.findByRechercheRestaurant(rechercheRestaurants);
        return switch (reponseService.getStatus()) {
            case OK -> {
                mappingListeRestaurantQuandRechercheOK(rechercheRestaurants, model, reponseService);
                yield "restaurant/restaurants";
            }
            case EMPTY -> {
                mappingQuandRechecheEmpty(rechercheRestaurants, model);
                yield "restaurant/restaurants";
            }
            case ERROR -> throw reponseService.getException();
        };
    }

    private void mappingListeRestaurantQuandRechercheOK(RechercheRestaurantDTO rechercheRestaurants, Model model, ReponseService<List<Restaurant>> reponseService) {
        List<RestaurantDTO> restaurantDTOS = reponseService.getObjetRetour().stream()
                .map(restaurant -> modelMapper.map(restaurant, RestaurantDTO.class))
                .toList();
        model.addAttribute("rechercheRestaurants", rechercheRestaurants);
        model.addAttribute("restaurants", restaurantDTOS);
    }

    private void mappingQuandRechecheEmpty(RechercheRestaurantDTO rechercheRestaurants, Model model) throws Exception {
        model.addAttribute("rechercheRestaurants", rechercheRestaurants);
        model.addAttribute("restaurants", emptyList());
        model.addAttribute("recherchevide", "Aucun restaurant trouvé");
    }

    @ModelAttribute(value = "rechercheRestaurants")
    private RechercheRestaurantDTO getrechercheRestaurant() {
        return new RechercheRestaurantDTO();
    }
}
