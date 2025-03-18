package com.gdu.wacdo.controllers;

import com.gdu.wacdo.dto.form.CreationAffectation;
import com.gdu.wacdo.dto.form.RechercheAffectation;
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
import org.springframework.web.bind.annotation.*;

import java.util.List;

import static java.util.Collections.emptyList;

@Controller
@Slf4j
public class AffectationController {

    private final AffectationService affectationService;
    private final RestaurantService restaurantService;
    private final FonctionService fonctionService;
    private final EmployeService employeService;
    private final ModelMapper modelMapper;

    public AffectationController(AffectationService affectationService, RestaurantService restaurantService, FonctionService fonctionService, EmployeService employeService, ModelMapper modelMapper) {
        this.affectationService = affectationService;
        this.restaurantService = restaurantService;
        this.fonctionService = fonctionService;
        this.employeService = employeService;
        this.modelMapper = modelMapper;
    }


    @GetMapping("/listeAffectations")
    public String getAllAffectations(Model model) throws Exception {
        ReponseService reponseService = affectationService.findAll();
        if (reponseService.isOk()) {
            List<AffectationDTO> affectationDTOS = ((List<Affectation>) reponseService.getData()).stream()
                    .map(affectation -> modelMapper.map(affectation, AffectationDTO.class))
                    .toList();
            model.addAttribute("affectations", affectationDTOS);
            return "affectations";
        } else {
            throw reponseService.getException();
        }

    }

    @GetMapping("/detailAffectation/{id}")
    public String getFonctionById(Model model, @PathVariable int id) throws Exception {
        ReponseService reponse = affectationService.findById(id);
        if (reponse.isOk()) {
            model.addAttribute("affectation", modelMapper.map(reponse.getData(), AffectationDTO.class));
            return "affectation";
        } else {
            throw reponse.getException();
        }
    }

    @PostMapping("/rechercheAffectations")
    public String rechercheAffectations(RechercheAffectation rechercheAffectation, Model model) throws Exception {
        ReponseService reponseService = affectationService.findByRechercheAffectation(rechercheAffectation);
        return switch (reponseService.getStatus()) {
            case OK -> {
                mappingListeAffectationsQuandRechercheOK(rechercheAffectation, model, reponseService);
                yield "restaurants";
            }
            case EMPTY -> {
                mappingQuandRechecheEmpty(rechercheAffectation, model);
                yield "restaurants";
            }
            case ERROR -> throw reponseService.getException();
        };
    }

    @GetMapping("/creerAffectation")
    public String getCreerAffectation(@RequestParam(value = "restaurant", required = false) Integer idRestaurant, @RequestParam(value = "employe", required = false) Integer idEmploye, Model model) throws Exception {
        if (idRestaurant != null) {
            model.addAttribute("affectationDTO", new CreationAffectation().creationDepuisRestaurant(idRestaurant));
        }
        if (idEmploye != null) {
            model.addAttribute("affectationDTO", new CreationAffectation().creationDepuisEmploye(idEmploye));
        }
        return "creationAffectation";
    }


    @PostMapping("/creerAffectation")
    public String enregistrementAffectation(@Valid @ModelAttribute("affectationDTO") CreationAffectation creationAffectation, BindingResult result, Model model) throws Exception {
        if (result.hasErrors()) {
            return "creationAffectation";
        }

        ReponseService reponseService = affectationService.save(creationAffectation.toAffectation((Employe) employeService.findById(creationAffectation.getEmploye()).getData(), (Restaurant) restaurantService.findById(creationAffectation.getRestaurant()).getData(), (Fonction) fonctionService.findById(creationAffectation.getFonction()).getData()));
        return switch (reponseService.getStatus()) {
            case OK -> {
                mappingAffectationEnregistree((Affectation) reponseService.getData(), model);
                yield "affectation";
            }
            case EMPTY -> {
                mappingAffectationNonEnregistree(creationAffectation, model);
                yield "creationAffectation";
            }
            case ERROR -> throw reponseService.getException();
        };
    }

    @GetMapping("/editerAffectation/{id}")
    public String getAffectationPourEdition(Model model, @PathVariable int id) throws Exception {
        model.addAttribute("affectationDTO", new CreationAffectation((Affectation) affectationService.findById(id).getData()));
        return "editionAffectation";
    }

    @PostMapping("/editerAffectation")
    public String editionAffectation(@Valid @ModelAttribute("affectationDTO") CreationAffectation creationAffectation, BindingResult result, Model model) throws Exception {
        if (result.hasErrors()) {
            return "editionAffectation";
        }
        ReponseService reponseService = affectationService.save(creationAffectation.pourEdition((Affectation) affectationService.findById(creationAffectation.getId()).getData(), (Employe) employeService.findById(creationAffectation.getEmploye()).getData(), (Restaurant) restaurantService.findById(creationAffectation.getRestaurant()).getData(), (Fonction) fonctionService.findById(creationAffectation.getFonction()).getData()));
        return switch (reponseService.getStatus()) {
            case OK -> {
                mappingAffectationEnregistree((Affectation) reponseService.getData(), model);
                yield "affectation";
            }
            case EMPTY -> {
                mappingAffectationNonEnregistree(creationAffectation, model);
                yield "editionAffectation";
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
    private void mappingAffectationNonEnregistree(CreationAffectation affectationDTO, Model model) throws Exception {
        model.addAttribute("affectationDTO", affectationDTO);
        model.addAttribute("messageNonEnregistrement", "Affectation non enregistrée");
    }

    /**
     * Réattribut l'objet de recherche d'affectation , fournit la liste d'affectaion trouvé par la recherche.
     */
    private void mappingListeAffectationsQuandRechercheOK(RechercheAffectation rechercheAffectation, Model model, ReponseService reponseService) {
        List<AffectationDTO> affectationDTOS = ((List<Affectation>) reponseService.getData()).stream()
                .map(affectation -> modelMapper.map(affectation, AffectationDTO.class))
                .toList();
        model.addAttribute("rechercheAffectations", rechercheAffectation);
        model.addAttribute("affectations", affectationDTOS);
    }

    /**
     * Réattribut l'objet de recherche de affectation, fournit une liste vide d'affectation et passe le message d'erreur.
     */
    private void mappingQuandRechecheEmpty(RechercheAffectation rechercheAffectations, Model model) throws Exception {
        model.addAttribute("rechercheAffectations", rechercheAffectations);
        model.addAttribute("affectations", emptyList());
        model.addAttribute("recherchevide", "Aucune affectation trouvée");
    }

    @ModelAttribute(value = "restaurants")
    private List<RestaurantDTO> getAllRestaurants() {
        return ((List<Restaurant>) restaurantService.findAll().getData()).stream()
                .map(fonction -> modelMapper.map(fonction, RestaurantDTO.class))
                .toList();
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

    @ModelAttribute(value = "rechercheAffectation")
    private RechercheAffectation getRechercheAffectation() {
        return new RechercheAffectation();
    }
}
