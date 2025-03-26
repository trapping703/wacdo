package com.gdu.wacdo.controllers.employe;

import com.gdu.wacdo.dto.form.RechercheAffectationDetailsEmployeDTO;
import com.gdu.wacdo.dto.model.AffectationDTO;
import com.gdu.wacdo.dto.model.EmployeDTO;
import com.gdu.wacdo.dto.model.FonctionDTO;
import com.gdu.wacdo.dto.response.ReponseService;
import com.gdu.wacdo.model.Affectation;
import com.gdu.wacdo.services.AffectationService;
import com.gdu.wacdo.services.EmployeService;
import com.gdu.wacdo.services.FonctionService;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.ArrayList;
import java.util.List;

@Controller
public class DetailEmployeController {

    private final EmployeService employeService;
    private final AffectationService affectationService;
    private final FonctionService fonctionService;
    private final ModelMapper modelMapper;

    public DetailEmployeController(EmployeService employeService, AffectationService affectationService, FonctionService fonctionService, ModelMapper modelMapper) {
        this.employeService = employeService;
        this.affectationService = affectationService;
        this.fonctionService = fonctionService;
        this.modelMapper = modelMapper;
    }

    /**
     * Page employé
     */
    @GetMapping("/detailEmploye/{id}")
    public String getEmploye(Model model, @PathVariable int id) {
        model.addAttribute("employe", modelMapper.map(employeService.findById(id).getData(), EmployeDTO.class));
        return "employe/employe";
    }

    /**
     * Gestion de la recherche d'affectation de la page détail employé
     */
    @PostMapping("/detailEmploye/{id}")
    public String detailEmployeAvecFiltreAffectation(RechercheAffectationDetailsEmployeDTO rechercheAffectation, Model model, @PathVariable int id) throws Exception {
        EmployeDTO employeDTO = modelMapper.map(employeService.findById(id).getData(), EmployeDTO.class);
        ReponseService<List<Affectation>> reponseService = affectationService.findAffectationsPourRechercheDetailsEmploye(rechercheAffectation, id);
        employeDTO.setAffectations(mappingListeAffectation(reponseService));
        model.addAttribute("employe", employeDTO);
        model.addAttribute("rechercheAffectation", rechercheAffectation);
        return "employe/employe";
    }


    private List<AffectationDTO> mappingListeAffectation(ReponseService<List<Affectation>> reponseService) throws Exception {
        return switch (reponseService.getStatus()) {
            case OK -> reponseService.getObjetRetour().stream()
                    .map(affectation -> modelMapper.map(affectation, AffectationDTO.class))
                    .toList();
            case EMPTY -> new ArrayList<>();

            case ERROR -> throw reponseService.getException();
        };
    }

    @ModelAttribute(value = "fonctions")
    private List<FonctionDTO> getAllFonctions() {
        return fonctionService.findAll().getObjetRetour().stream()
                .map(fonction -> modelMapper.map(fonction, FonctionDTO.class))
                .toList();
    }

    @ModelAttribute(value = "rechercheAffectation")
    private RechercheAffectationDetailsEmployeDTO getRechercheAffectation() {
        return new RechercheAffectationDetailsEmployeDTO();
    }
}
