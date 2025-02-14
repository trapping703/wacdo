package com.gdu.wacdo.controllers;

import com.gdu.wacdo.dto.model.AffectationDTO;
import com.gdu.wacdo.dto.model.EmployeDTO;
import com.gdu.wacdo.dto.model.FonctionDTO;
import com.gdu.wacdo.dto.model.RestaurantDTO;
import com.gdu.wacdo.dto.response.ReponseService;
import com.gdu.wacdo.services.AffectationService;
import com.gdu.wacdo.services.EmployeService;
import com.gdu.wacdo.services.FonctionService;
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
                .map(fonction -> modelMapper.map(fonction, AffectationDTO.class))
                .toList();
        model.addAttribute("affectations", affectationDTOS);
        return "affectations";
    }

    @GetMapping("/affectation/{id}")
    public String getFonctionById(Model model, @PathVariable int id) {
        ReponseService reponse = affectationService.findById(id);
        if (reponse.isOk()) {
            model.addAttribute("affectation", modelMapper.map(reponse.getData(), AffectationDTO.class));
            model.addAttribute("restaurants", getAllRestaurants());
            model.addAttribute("fonctions", getAllFonctions());
            model.addAttribute("employes", getAllEmployes());
            return "affectation";
        } else {
            return "index";
        }
    }

    private Object[] getAllRestaurants() {
        return restaurantService.findAll().getData().stream()
                .map(fonction -> modelMapper.map(fonction, RestaurantDTO.class))
                .toArray();
    }

    private List<FonctionDTO> getAllFonctions() {
        return fonctionService.findAll().getData().stream()
                .map(fonction -> modelMapper.map(fonction, FonctionDTO.class))
                .toList();
    }

    private List<EmployeDTO> getAllEmployes() {
        return employeService.findAll().getData().stream()
                .map(fonction -> modelMapper.map(fonction, EmployeDTO.class))
                .toList();
    }
}
