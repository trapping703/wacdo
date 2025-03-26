package com.gdu.wacdo.controllers.affectation;

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
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;

import static java.util.Collections.emptyList;

@Controller
public class ListeAffectationController {


    private final AffectationService affectationService;
    private final ModelMapper modelMapper;
    private final EmployeService employeService;
    private final RestaurantService restaurantService;
    private final FonctionService fonctionService;

    public ListeAffectationController(AffectationService affectationService, ModelMapper modelMapper, EmployeService employeService, RestaurantService restaurantService, FonctionService fonctionService) {
        this.affectationService = affectationService;
        this.modelMapper = modelMapper;
        this.employeService = employeService;
        this.restaurantService = restaurantService;
        this.fonctionService = fonctionService;
    }

    /**
     * Page d'index de la liste d'affectation
     */
    @GetMapping("/listeAffectations")
    public String getAllAffectations(Model model) throws Exception {
        ReponseService<List<Affectation>> reponseService = affectationService.findAll();
        if (reponseService.isOk()) {
            List<AffectationDTO> affectationDTOS = reponseService.getObjetRetour().stream()
                    .map(affectation -> modelMapper.map(affectation, AffectationDTO.class))
                    .toList();
            model.addAttribute("affectations", affectationDTOS);
            return "affectation/affectations";
        } else {
            throw reponseService.getException();
        }

    }

    /**
     * Gère la recherche des affectations de la vue des listes d'affectations
     */
    @PostMapping("/rechercheAffectations")
    public String rechercheAffectations(RechercheAffectationDTO rechercheAffectation, Model model) throws Exception {
        ReponseService<List<Affectation>> reponseService = affectationService.findAffectationsPourRechercheListeAffection(rechercheAffectation);
        return switch (reponseService.getStatus()) {
            case OK -> {
                mappingListeAffectationsQuandRechercheOK(rechercheAffectation, model, reponseService);
                yield "affectation/affectations";
            }
            case EMPTY -> {
                mappingQuandRechecheEmpty(rechercheAffectation, model);
                yield "affectation/affectations";
            }
            case ERROR -> throw reponseService.getException();
        };
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
