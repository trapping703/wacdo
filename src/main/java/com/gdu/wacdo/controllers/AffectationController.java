package com.gdu.wacdo.controllers;

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
        ReponseService<List<Affectation>> reponseService = affectationService.findAll();
        if (reponseService.isOk()) {
            List<AffectationDTO> affectationDTOS = reponseService.getObjetRetour().stream()
                    .map(affectation -> modelMapper.map(affectation, AffectationDTO.class))
                    .toList();
            model.addAttribute("affectations", affectationDTOS);
            return "affectations";
        } else {
            throw reponseService.getException();
        }

    }

    @GetMapping("/detailAffectation/{id}")
    public String getAffectationById(Model model, @PathVariable int id) throws Exception {
        ReponseService<Affectation> reponse = affectationService.findById(id);
        if (reponse.isOk()) {
            model.addAttribute("affectation", modelMapper.map(reponse.getData(), AffectationDTO.class));
            return "affectation";
        } else {
            throw reponse.getException();
        }
    }

    @PostMapping("/rechercheAffectations")
    public String rechercheAffectations(RechercheAffectationDTO rechercheAffectation, Model model) throws Exception {
        ReponseService<List<Affectation>> reponseService = affectationService.findAffectationsPourRechercheListeAffection(rechercheAffectation);
        return switch (reponseService.getStatus()) {
            case OK -> {
                mappingListeAffectationsQuandRechercheOK(rechercheAffectation, model, reponseService);
                yield "affectations";
            }
            case EMPTY -> {
                mappingQuandRechecheEmpty(rechercheAffectation, model);
                yield "affectations";
            }
            case ERROR -> throw reponseService.getException();
        };
    }

    @GetMapping("/creerAffectation")
    public String getCreerAffectation(@RequestParam(value = "restaurant", required = false) Integer idRestaurant, @RequestParam(value = "employe", required = false) Integer idEmploye, Model model) throws Exception {
        if (idRestaurant != null) {
            model.addAttribute("affectationDTO", new CreationAffectationDTO().creationDepuisRestaurant(idRestaurant));
        }
        if (idEmploye != null) {
            model.addAttribute("affectationDTO", new CreationAffectationDTO().creationDepuisEmploye(idEmploye));
        }
        return "creationAffectation";
    }


    @PostMapping("/creerAffectation")
    public String enregistrementAffectation(@Valid @ModelAttribute("affectationDTO") CreationAffectationDTO creationAffectation, BindingResult result, Model model) throws Exception {
        if (result.hasErrors()) {
            return "creationAffectation";
        }

        ReponseService<Affectation> reponseService = affectationService.save(creationAffectation.toAffectation(employeService.findById(creationAffectation.getEmploye()).getObjetRetour(), restaurantService.findById(creationAffectation.getRestaurant()).getObjetRetour(), fonctionService.findById(creationAffectation.getFonction()).getObjetRetour()));
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
        model.addAttribute("affectationDTO", new CreationAffectationDTO(affectationService.findById(id).getObjetRetour()));
        return "editionAffectation";
    }

    @PostMapping("/editerAffectation")
    public String editionAffectation(@Valid @ModelAttribute("affectationDTO") CreationAffectationDTO creationAffectation, BindingResult result, Model model) throws Exception {
        if (result.hasErrors()) {
            return "editionAffectation";
        }
        ReponseService<Affectation> reponseService = affectationService.save(creationAffectation.pourEdition(affectationService.findById(creationAffectation.getId()).getObjetRetour(), employeService.findById(creationAffectation.getEmploye()).getObjetRetour(), restaurantService.findById(creationAffectation.getRestaurant()).getObjetRetour(), fonctionService.findById(creationAffectation.getFonction()).getObjetRetour()));
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
    private void mappingAffectationNonEnregistree(CreationAffectationDTO affectationDTO, Model model) throws Exception {
        model.addAttribute("affectationDTO", affectationDTO);
        model.addAttribute("messageNonEnregistrement", "Affectation non enregistrée");
    }

    /**
     * Réattribut l'objet de recherche d'affectation , fournit la liste d'affectaion trouvé par la recherche.
     */
    private void mappingListeAffectationsQuandRechercheOK(RechercheAffectationDTO rechercheAffectation, Model model, ReponseService<List<Affectation>> reponseService) {
        List<AffectationDTO> affectationDTOS = reponseService.getObjetRetour().stream()
                .map(affectation -> modelMapper.map(affectation, AffectationDTO.class))
                .toList();
        model.addAttribute("rechercheAffectations", rechercheAffectation);
        model.addAttribute("affectations", affectationDTOS);
    }

    /**
     * Réattribut l'objet de recherche de affectation, fournit une liste vide d'affectation et passe le message d'erreur.
     */
    private void mappingQuandRechecheEmpty(RechercheAffectationDTO rechercheAffectations, Model model) throws Exception {
        model.addAttribute("rechercheAffectations", rechercheAffectations);
        model.addAttribute("affectations", emptyList());
        model.addAttribute("recherchevide", "Aucune affectation trouvée");
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

    @ModelAttribute(value = "rechercheAffectations")
    private RechercheAffectationDTO getRechercheAffectation() {
        return new RechercheAffectationDTO();
    }
}
