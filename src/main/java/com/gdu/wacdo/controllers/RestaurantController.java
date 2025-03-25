package com.gdu.wacdo.controllers;

import com.gdu.wacdo.dto.form.RechercheAffectationDetailsRestaurantDTO;
import com.gdu.wacdo.dto.form.RechercheRestaurantDTO;
import com.gdu.wacdo.dto.model.AffectationDTO;
import com.gdu.wacdo.dto.model.EmployeDTO;
import com.gdu.wacdo.dto.model.FonctionDTO;
import com.gdu.wacdo.dto.model.RestaurantDTO;
import com.gdu.wacdo.dto.response.ReponseService;
import com.gdu.wacdo.model.Affectation;
import com.gdu.wacdo.model.Employe;
import com.gdu.wacdo.model.Fonction;
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
public class RestaurantController {

    private final RestaurantService restaurantService;
    private final AffectationService affectationService;
    private final FonctionService fonctionService;
    private final EmployeService employeService;
    private final ModelMapper modelMapper;

    public RestaurantController(RestaurantService restaurantService, AffectationService affectationService, FonctionService fonctionService, EmployeService employeService, ModelMapper modelMapper) {
        this.restaurantService = restaurantService;
        this.affectationService = affectationService;
        this.fonctionService = fonctionService;
        this.employeService = employeService;
        this.modelMapper = modelMapper;
    }

    @GetMapping("/listeRestaurants")
    public String restaurants(Model model) throws Exception {
        ReponseService<List<Restaurant>> reponse = restaurantService.findAll();
        if (!reponse.isError()) {
            List<RestaurantDTO> restaurantDTOS = reponse.getObjetRetour().stream()
                    .map(r -> modelMapper.map(r, RestaurantDTO.class))
                    .toList();
            model.addAttribute("restaurants", restaurantDTOS);
            return "restaurants";
        }
        throw reponse.getException();
    }

    @GetMapping("/detailRestaurant/{id}")
    public String getRestaurant(Model model, @PathVariable int id) throws Exception {
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
            return "restaurant";
        }
        throw reponse.getException();
    }


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
        return "restaurant";
    }

    @PostMapping("/rechercheRestaurants")
    public String rechercheRestaurants(@Valid @ModelAttribute("rechercheRestaurants") RechercheRestaurantDTO rechercheRestaurants, BindingResult result, Model model) throws Exception {
        if (result.hasErrors()) {
            return "restaurants";
        }
        ReponseService<List<Restaurant>> reponseService = restaurantService.findByRechercheRestaurant(rechercheRestaurants);
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

    @GetMapping("/creerRestaurant")
    public String getCreerRestaurant() {
        return "creationRestaurant";
    }

    @PostMapping("/creerRestaurant")
    public String enregistrementRestaurant(@Valid @ModelAttribute("restaurantDTO") RestaurantDTO restaurantDTO, BindingResult result, Model model) throws Exception {
        if (result.hasErrors()) {
            return "creationRestaurant";
        }
        ReponseService<Restaurant> reponseService = restaurantService.save(modelMapper.map(restaurantDTO, Restaurant.class));
        return switch (reponseService.getStatus()) {
            case OK -> {
                mappingRestaurantEnregistree(reponseService.getObjetRetour(), model);
                yield "restaurant";
            }
            case EMPTY -> {
                mappingRestaurantNonEnregistree(restaurantDTO, model);
                yield "creationRestaurant";
            }
            case ERROR -> throw reponseService.getException();
        };
    }

    @GetMapping("/editerRestaurant/{id}")
    public String getEditerRestaurant(Model model, @PathVariable int id) {
        model.addAttribute("restaurantDTO", modelMapper.map(restaurantService.findById(id).getData(), RestaurantDTO.class));
        return "editionRestaurant";
    }


    @PostMapping("/editerRestaurant")
    public String editerRestaurant(@Valid @ModelAttribute("restaurantDTO") RestaurantDTO restaurantDTO, BindingResult result, Model model) throws Exception {
        if (result.hasErrors()) {
            return "editionRestaurant";
        }
        ReponseService<Restaurant> reponseService = restaurantService.save(modelMapper.map(restaurantDTO.pourEdition(restaurantService.findById(restaurantDTO.getId()).getObjetRetour()), Restaurant.class));
        return switch (reponseService.getStatus()) {
            case OK -> {
                mappingRestaurantEnregistree(reponseService.getObjetRetour(), model);
                yield "restaurant";
            }
            case EMPTY -> {
                mappingRestaurantNonEnregistree(restaurantDTO, model);
                yield "editionRestaurant";
            }
            case ERROR -> throw reponseService.getException();
        };
    }

    /**
     * Réattribut l'objet de recherche de restaurant, fournit la liste de restaurant trouvé par la recherche.
     */
    private void mappingListeRestaurantQuandRechercheOK(RechercheRestaurantDTO rechercheRestaurants, Model model, ReponseService<List<Restaurant>> reponseService) {
        List<RestaurantDTO> restaurantDTOS = reponseService.getObjetRetour().stream()
                .map(restaurant -> modelMapper.map(restaurant, RestaurantDTO.class))
                .toList();
        model.addAttribute("rechercheRestaurants", rechercheRestaurants);
        model.addAttribute("restaurants", restaurantDTOS);
    }

    /**
     * Réattribut l'objet de recherche de restaurant, fournit une liste vide de restaurant et passe le message d'erreur.
     */
    private void mappingQuandRechecheEmpty(RechercheRestaurantDTO rechercheRestaurants, Model model) throws Exception {
        model.addAttribute("rechercheRestaurants", rechercheRestaurants);
        model.addAttribute("restaurants", emptyList());
        model.addAttribute("recherchevide", "Aucun restaurant trouvé");
    }

    /**
     * Réattribut l'objet restaurant enregistrée et un message de validation.
     */
    private void mappingRestaurantEnregistree(Restaurant restaurant, Model model) {
        model.addAttribute("restaurant", modelMapper.map(restaurant, RestaurantDTO.class));
        model.addAttribute("affectationsDuRestaurant", affectationService.findByDateFinIsNullAndRestaurantIs(restaurant).getObjetRetour()
                .stream()
                .map(affectation -> modelMapper.map(affectation, AffectationDTO.class))
                .toList());
        model.addAttribute("messageEnregistrement", "Restaurant Enregistrée");
    }

    /**
     * Réattribut l'objet restaurantDTO avec un message d'erreur
     */
    private void mappingRestaurantNonEnregistree(RestaurantDTO restaurantDTO, Model model) throws Exception {
        model.addAttribute("restaurantDTO", restaurantDTO);
        model.addAttribute("messageNonEnregistrement", "Restaurant non enregistrée");
    }

    @ModelAttribute(value = "rechercheRestaurants")
    private RechercheRestaurantDTO getrechercheRestaurant() {
        return new RechercheRestaurantDTO();
    }

    @ModelAttribute(value = "restaurantDTO")
    private RestaurantDTO getRestaurantDTO() {
        return new RestaurantDTO();
    }

    @ModelAttribute(value = "fonctions")
    private List<FonctionDTO> getAllFonctions() {
        return ((List<Fonction>) fonctionService.findAll().getData()).stream()
                .map(fonction -> modelMapper.map(fonction, FonctionDTO.class))
                .toList();
    }

    @ModelAttribute(value = "employes")
    private List<EmployeDTO> getAllEmployes() {
        return ((List<Employe>) employeService.findAll().getData()).stream()
                .map(fonction -> modelMapper.map(fonction, EmployeDTO.class))
                .toList();
    }

    @ModelAttribute(value = "rechercheAffectationDuRestaurant")
    private RechercheAffectationDetailsRestaurantDTO getRechercheAffectationDuRestaurant() {
        return new RechercheAffectationDetailsRestaurantDTO();
    }
}
