package com.gdu.wacdo.controllers.affectation;

import com.gdu.wacdo.dto.form.CreationAffectationDTO;
import com.gdu.wacdo.dto.form.RechercheAffectationDTO;
import com.gdu.wacdo.dto.model.AffectationDTO;
import com.gdu.wacdo.dto.model.EmployeDTO;
import com.gdu.wacdo.dto.model.FonctionDTO;
import com.gdu.wacdo.dto.model.RestaurantDTO;
import com.gdu.wacdo.dto.response.ReponseService;
import com.gdu.wacdo.model.Affectation;
import com.gdu.wacdo.services.AffectationService;
import com.gdu.wacdo.services.EmployeService;
import com.gdu.wacdo.services.FonctionService;
import com.gdu.wacdo.services.RestaurantService;
import jakarta.validation.Valid;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.*;

import java.util.List;

@Controller
public class GestionAffectation {

    private final AffectationService affectationService;
    private final RestaurantService restaurantService;
    private final FonctionService fonctionService;
    private final EmployeService employeService;
    private final ModelMapper modelMapper;

    public GestionAffectation(AffectationService affectationService, RestaurantService restaurantService, FonctionService fonctionService, EmployeService employeService, ModelMapper modelMapper) {
        this.affectationService = affectationService;
        this.restaurantService = restaurantService;
        this.fonctionService = fonctionService;
        this.employeService = employeService;
        this.modelMapper = modelMapper;
    }


    @GetMapping("/creerAffectation")
    public String getCreerAffectation(@RequestParam(value = "restaurant", required = false) Integer idRestaurant, @RequestParam(value = "employe", required = false) Integer idEmploye, Model model) throws Exception {
        if (idRestaurant != null) {
            model.addAttribute("affectationDTO", new CreationAffectationDTO().creationDepuisRestaurant(idRestaurant));
        }
        if (idEmploye != null) {
            model.addAttribute("affectationDTO", new CreationAffectationDTO().creationDepuisEmploye(idEmploye));
        }
        return "affectation/creationAffectation";
    }


    @PostMapping("/creerAffectation")
    public String enregistrementAffectation(@Valid @ModelAttribute("affectationDTO") CreationAffectationDTO creationAffectation, BindingResult result, Model model) throws Exception {
        if (result.hasErrors()) {
            return "affectation/creationAffectation";
        }

        ReponseService<Affectation> reponseService = affectationService.save(creationAffectation.toAffectation(employeService.findById(creationAffectation.getEmploye()).getObjetRetour(), restaurantService.findById(creationAffectation.getRestaurant()).getObjetRetour(), fonctionService.findById(creationAffectation.getFonction()).getObjetRetour()));
        return switch (reponseService.getStatus()) {
            case OK -> {
                mappingAffectationEnregistree((Affectation) reponseService.getData(), model);
                yield "affectation/affectation";
            }
            case EMPTY -> {
                mappingAffectationNonEnregistree(creationAffectation, model);
                yield "affectation/creationAffectation";
            }
            case ERROR -> throw reponseService.getException();
        };
    }

    @GetMapping("/editerAffectation/{id}")
    public String getAffectationPourEdition(Model model, @PathVariable int id) throws Exception {
        model.addAttribute("affectationDTO", new CreationAffectationDTO(affectationService.findById(id).getObjetRetour()));
        return "affectation/editionAffectation";
    }

    @PostMapping("/editerAffectation")
    public String editionAffectation(@Valid @ModelAttribute("affectationDTO") CreationAffectationDTO creationAffectation, BindingResult result, Model model) throws Exception {
        if (result.hasErrors()) {
            return "affectation/editionAffectation";
        }
        ReponseService<Affectation> reponseService = affectationService.save(creationAffectation.pourEdition(affectationService.findById(creationAffectation.getId()).getObjetRetour(), employeService.findById(creationAffectation.getEmploye()).getObjetRetour(), restaurantService.findById(creationAffectation.getRestaurant()).getObjetRetour(), fonctionService.findById(creationAffectation.getFonction()).getObjetRetour()));
        return switch (reponseService.getStatus()) {
            case OK -> {
                mappingAffectationEnregistree((Affectation) reponseService.getData(), model);
                yield "affectation/affectation";
            }
            case EMPTY -> {
                mappingAffectationNonEnregistree(creationAffectation, model);
                yield "affectation/editionAffectation";
            }
            case ERROR -> throw reponseService.getException();
        };
    }

    /**
     * Réattribut l'objet affectation enregistrée et un message de validation.
     */
    private void mappingAffectationEnregistree(Affectation affectation, Model model) {
        model.addAttribute("affectation", modelMapper.map(affectation, AffectationDTO.class));
        model.addAttribute("messageEnregistrement", "Affectation Enregistrée");
    }

    /**
     * Réattribut l'objet affectationDTO avec un message d'erreur
     */
    private void mappingAffectationNonEnregistree(CreationAffectationDTO affectationDTO, Model model) throws Exception {
        model.addAttribute("affectationDTO", affectationDTO);
        model.addAttribute("messageNonEnregistrement", "Affectation non enregistrée");
    }

    @ModelAttribute(value = "restaurants")
    private List<RestaurantDTO> getAllRestaurants() {
        return restaurantService.findAll().getObjetRetour().stream()
                .map(fonction -> modelMapper.map(fonction, RestaurantDTO.class))
                .toList();
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
}
