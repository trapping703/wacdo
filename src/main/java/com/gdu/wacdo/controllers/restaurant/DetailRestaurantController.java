package com.gdu.wacdo.controllers.restaurant;

import com.gdu.wacdo.dto.form.RechercheAffectationDetailsRestaurantDTO;
import com.gdu.wacdo.dto.model.AffectationDTO;
import com.gdu.wacdo.dto.model.RestaurantDTO;
import com.gdu.wacdo.dto.response.ReponseService;
import com.gdu.wacdo.model.Affectation;
import com.gdu.wacdo.model.Restaurant;
import com.gdu.wacdo.services.AffectationService;
import com.gdu.wacdo.services.RestaurantService;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;

import static java.util.Collections.emptyList;

@Controller
public class DetailRestaurantController {

    private final RestaurantService restaurantService;
    private final AffectationService affectationService;
    private final ModelMapper modelMapper;

    public DetailRestaurantController(RestaurantService restaurantService, AffectationService affectationService, ModelMapper modelMapper) {
        this.restaurantService = restaurantService;
        this.affectationService = affectationService;
        this.modelMapper = modelMapper;
    }

    /**
     * Page de détails du restaurant
     */
    @GetMapping("/detailRestaurant/{id}")
    public String detailRestaurant(Model model, @PathVariable int id) throws Exception {
        ReponseService<Restaurant> reponse = restaurantService.findById(id);
        if (!reponse.isError()) {
            model.addAttribute("restaurant", modelMapper.map(reponse.getData(), RestaurantDTO.class));
            ReponseService<List<Affectation>> reponseService = affectationService.findByDateFinIsNullAndRestaurantIs(reponse.getObjetRetour());
            if (!reponseService.isEmpty()) {
                model.addAttribute("affectationsDuRestaurant", reponseService.getObjetRetour().stream()
                        .map(affectation -> modelMapper.map(affectation, AffectationDTO.class))
                        .toList());
            } else {
                model.addAttribute("affectationsDuRestaurant", emptyList());
            }
            return "restaurant/restaurant";
        }
        throw reponse.getException();
    }


    /**
     * Gestion de la recherche d'affectation sur la page détail du restaurant
     */
    @PostMapping("/detailRestaurant/{id}")
    public String detailRestaurantAvecFiltreAffectation(RechercheAffectationDetailsRestaurantDTO rechercheAffectation, Model model, @PathVariable int id) throws Exception {
        RestaurantDTO RestaurantDTO = modelMapper.map(restaurantService.findById(id).getData(), RestaurantDTO.class);
        ReponseService<List<Affectation>> reponseService = affectationService.findAffectationsPourRechercheDetailsRestaurant(rechercheAffectation, id);
        if (!reponseService.isEmpty()) {
            model.addAttribute("affectationsDuRestaurant", reponseService.getObjetRetour().stream()
                    .map(affectation -> modelMapper.map(affectation, AffectationDTO.class))
                    .toList());
        } else {
            model.addAttribute("affectationsDuRestaurant", emptyList());
        }
        model.addAttribute("restaurant", RestaurantDTO);
        model.addAttribute("rechercheAffectationDuRestaurant", rechercheAffectation);
        return "restaurant/restaurant";
    }

    @ModelAttribute(value = "rechercheAffectationDuRestaurant")
    private RechercheAffectationDetailsRestaurantDTO getRechercheAffectationDuRestaurant() {
        return new RechercheAffectationDetailsRestaurantDTO();
    }
}
