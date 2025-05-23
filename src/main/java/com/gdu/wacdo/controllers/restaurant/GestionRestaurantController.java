package com.gdu.wacdo.controllers.restaurant;

import com.gdu.wacdo.dto.form.RechercheAffectationDetailsRestaurantDTO;
import com.gdu.wacdo.dto.model.AffectationDTO;
import com.gdu.wacdo.dto.model.EmployeDTO;
import com.gdu.wacdo.dto.model.FonctionDTO;
import com.gdu.wacdo.dto.model.RestaurantDTO;
import com.gdu.wacdo.dto.response.ReponseService;
import com.gdu.wacdo.model.Affectation;
import com.gdu.wacdo.model.Restaurant;
import com.gdu.wacdo.services.AffectationService;
import com.gdu.wacdo.services.EmployeService;
import com.gdu.wacdo.services.FonctionService;
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
public class GestionRestaurantController {

    private final RestaurantService restaurantService;
    private final AffectationService affectationService;
    private final FonctionService fonctionService;
    private final EmployeService employeService;
    private final ModelMapper modelMapper;

    public GestionRestaurantController(RestaurantService restaurantService, AffectationService affectationService, FonctionService fonctionService, EmployeService employeService, ModelMapper modelMapper) {
        this.restaurantService = restaurantService;
        this.affectationService = affectationService;
        this.fonctionService = fonctionService;
        this.employeService = employeService;
        this.modelMapper = modelMapper;
    }

    /**
     * Page de création de restaurant
     *
     * @return
     */
    @GetMapping("/creerRestaurant")
    public String getCreerRestaurant() {
        return "restaurant/creationRestaurant";
    }

    /**
     * Gestion de la création de restaurant
     */
    @PostMapping("/creerRestaurant")
    public String enregistrementRestaurant(@Valid @ModelAttribute("restaurantDTO") RestaurantDTO restaurantDTO, BindingResult result, Model model) throws Exception {
        if (result.hasErrors()) {
            return "restaurant/creationRestaurant";
        }
        ReponseService<Restaurant> reponseService = restaurantService.save(modelMapper.map(restaurantDTO, Restaurant.class));
        return switch (reponseService.getStatus()) {
            case OK -> {
                mappingRestaurantCreer(reponseService.getObjetRetour(), model);
                yield "restaurant/restaurant";
            }
            case EMPTY -> {
                mappingRestaurantNonEnregistree(restaurantDTO, model);
                yield "restaurant/creationRestaurant";
            }
            case ERROR -> throw reponseService.getException();
        };
    }

    /**
     * Page d'édition du restaurant
     */
    @GetMapping("/editerRestaurant/{id}")
    public String getEditerRestaurant(Model model, @PathVariable int id) {
        Restaurant restaurant = restaurantService.findById(id).getObjetRetour();
        model.addAttribute("restaurantDTO", modelMapper.map(restaurant, RestaurantDTO.class));
        mappingAffectation(affectationService.findByRestaurantIs(restaurant), model);
        return "restaurant/editionRestaurant";
    }

    /**
     * Page d'édition du restaurant avec un la liste d'affectation filtré
     */
    @PostMapping("/editerRestaurantAllAffectation/{id}")
    public String getEditerRestaurant(RechercheAffectationDetailsRestaurantDTO rechercheAffectation,Model model, @PathVariable int id) {
        Restaurant restaurant = restaurantService.findById(id).getObjetRetour();
        model.addAttribute("restaurantDTO", modelMapper.map(restaurant, RestaurantDTO.class));
        ReponseService<List<Affectation>> reponseService = affectationService.findAffectationsPourRechercheEditionRestaurant(rechercheAffectation, id);
        if (!reponseService.isEmpty()) {
            model.addAttribute("affectationsDuRestaurant", reponseService.getObjetRetour().stream()
                    .map(affectation -> modelMapper.map(affectation, AffectationDTO.class))
                    .toList());
        } else {
            model.addAttribute("affectationsDuRestaurant", emptyList());
        }
        model.addAttribute("rechercheAffectationDuRestaurant", rechercheAffectation);
        return "restaurant/editionRestaurant";
    }

    /**
     * Gestion de l'édition du restaurant
     */
    @PostMapping("/editerRestaurant")
    public String editerRestaurant(@Valid @ModelAttribute("restaurantDTO") RestaurantDTO restaurantDTO, BindingResult result, Model model) throws Exception {
        if (result.hasErrors()) {
            return "restaurant/editionRestaurant";
        }
        ReponseService<Restaurant> reponseService = restaurantService.save(modelMapper.map(restaurantDTO.pourEdition(restaurantService.findById(restaurantDTO.getId()).getObjetRetour()), Restaurant.class));
        return switch (reponseService.getStatus()) {
            case OK -> {
                mappingRestaurantEnregistree(reponseService.getObjetRetour(), model);
                yield "restaurant/restaurant";
            }
            case EMPTY -> {
                mappingRestaurantNonEnregistree(restaurantDTO, model);
                mappingAffectation(affectationService.findByRestaurantIs(reponseService.getObjetRetour()), model);
                yield "restaurant/editionRestaurant";
            }
            case ERROR -> throw reponseService.getException();
        };
    }

    private void mappingRestaurantEnregistree(Restaurant restaurant, Model model) {
        model.addAttribute("restaurant", modelMapper.map(restaurant, RestaurantDTO.class));
        ReponseService<List<Affectation>> affectationReponse = affectationService.findByDateFinIsNullAndRestaurantIs(restaurant);
        if(affectationReponse.isOk()){
            model.addAttribute("affectationsDuRestaurant", affectationService.findByDateFinIsNullAndRestaurantIs(restaurant).getObjetRetour()
                    .stream()
                    .map(affectation -> modelMapper.map(affectation, AffectationDTO.class))
                    .toList());
        } else {
            model.addAttribute("affectationsDuRestaurant", emptyList());
        }
        model.addAttribute("messageEnregistrement", "Restaurant Enregistrée");
    }

    private void mappingRestaurantCreer(Restaurant restaurant, Model model) {
        model.addAttribute("restaurant", modelMapper.map(restaurant, RestaurantDTO.class));
        model.addAttribute("affectationsDuRestaurant", emptyList());
        model.addAttribute("messageEnregistrement", "Restaurant Enregistrée");
    }

    private void mappingRestaurantNonEnregistree(RestaurantDTO restaurantDTO, Model model) throws Exception {
        model.addAttribute("restaurantDTO", restaurantDTO);
        model.addAttribute("messageNonEnregistrement", "Restaurant non enregistrée");
    }

    private void mappingAffectation(ReponseService<List<Affectation>> affectationsReponse, Model model) {
        if (affectationsReponse.isOk()) {
            model.addAttribute("affectationsDuRestaurant", affectationsReponse.getObjetRetour().stream()
                    .map(affectation -> modelMapper.map(affectation, AffectationDTO.class))
                    .toList());
        }
    }

    @ModelAttribute(value = "restaurantDTO")
    private RestaurantDTO getRestaurantDTO() {
        return new RestaurantDTO();
    }

    @ModelAttribute(value = "fonctions")
    private List<FonctionDTO> getAllFonctions() {
        return fonctionService.findAll().getObjetRetour().stream()
                .map(fonction -> modelMapper.map(fonction, FonctionDTO.class))
                .toList();
    }

    @ModelAttribute(value = "employes")
    private List<EmployeDTO> getAllEmployes() {
        return employeService.findAll().getObjetRetour().stream()
                .map(fonction -> modelMapper.map(fonction, EmployeDTO.class))
                .toList();
    }

    @ModelAttribute(value = "rechercheAffectationDuRestaurant")
    private RechercheAffectationDetailsRestaurantDTO getRechercheAffectationDuRestaurant() {
        return new RechercheAffectationDetailsRestaurantDTO();
    }
}
