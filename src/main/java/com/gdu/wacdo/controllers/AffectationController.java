package com.gdu.wacdo.controllers;

import com.gdu.wacdo.dto.form.RechercheAffectation;
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
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;

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


    @GetMapping("/affectations")
    public String getAllAffectations(Model model) {
        List<AffectationDTO> affectationDTOS = affectationService.findAll().getData().stream()
                .map(affectation -> modelMapper.map(affectation, AffectationDTO.class))
                .toList();
        model.addAttribute("affectations", affectationDTOS);
        return "affectations";
    }

    @GetMapping("/affectation/{id}")
    public String getFonctionById(Model model, @PathVariable int id) {
        ReponseService reponse = affectationService.findById(id);
        if (reponse.isOk()) {
            model.addAttribute("affectation", modelMapper.map(reponse.getData(), AffectationDTO.class));
            return "affectation";
        } else {
            return "index";
        }
    }

    @PostMapping("/rechercheAffectations")
    public String rechercheAffectations(RechercheAffectation rechercheAffectation, Model model) {
        ReponseService reponseService = affectationService.findByRechercheAffectation(rechercheAffectation);
        List<AffectationDTO> affectationDTOS = ((List<Affectation>)reponseService.getData()).stream()
                .map(affectation -> modelMapper.map(affectation, AffectationDTO.class))
                .toList();
        model.addAttribute("rechercheAffectations", rechercheAffectation);
        model.addAttribute("affectations", affectationDTOS);
        return "affectations";
    }

    @ModelAttribute(value = "restaurants")
    private List<RestaurantDTO> getAllRestaurants() {
        return restaurantService.findAll().getData().stream()
                .map(fonction -> modelMapper.map(fonction, RestaurantDTO.class))
                .toList();
    }

    @ModelAttribute(value = "fonctions")
    private List<FonctionDTO> getAllFonctions() {
        return fonctionService.findAll().getData().stream()
                .map(fonction -> modelMapper.map(fonction, FonctionDTO.class))
                .toList();
    }

    @ModelAttribute(value = "employes")
    private List<EmployeDTO> getAllEmployes() {
        return employeService.findAll().getData().stream()
                .map(fonction -> modelMapper.map(fonction, EmployeDTO.class))
                .toList();
    }

    @ModelAttribute(value = "rechercheAffectation")
    private RechercheAffectation getRechercheAffectation() {
        return new RechercheAffectation();
    }
}
