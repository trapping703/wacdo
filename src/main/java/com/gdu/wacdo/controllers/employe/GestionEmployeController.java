package com.gdu.wacdo.controllers.employe;

import com.gdu.wacdo.dto.form.RechercheAffectationDetailsEmployeDTO;
import com.gdu.wacdo.dto.form.RechercheEmployeDTO;
import com.gdu.wacdo.dto.model.EmployeDTO;
import com.gdu.wacdo.dto.response.ReponseService;
import com.gdu.wacdo.model.Employe;
import com.gdu.wacdo.services.EmployeService;
import jakarta.validation.Valid;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
public class GestionEmployeController {

    private final EmployeService employeService;
    private final ModelMapper modelMapper;

    public GestionEmployeController(EmployeService employeService, ModelMapper modelMapper) {
        this.employeService = employeService;
        this.modelMapper = modelMapper;
    }

    /**
     * Page de création d'employé
     *
     * @return
     */
    @GetMapping("/creerEmploye")
    public String getCreerEmploye() {
        return "employe/creationEmploye";
    }

    /**
     * Gestion de la création d'employé
     */
    @PostMapping("/creerEmploye")
    public String enregistrementEmploye(@Valid @ModelAttribute("employeDTO") EmployeDTO employeDTO, BindingResult result, Model model) throws Exception {
        if (result.hasErrors()) {
            return "employe/creationEmploye";
        }
        ReponseService<Employe> reponseService = employeService.save(modelMapper.map(employeDTO, Employe.class));
        return switch (reponseService.getStatus()) {
            case OK -> {
                mappingEmployeEnregistree(reponseService.getObjetRetour(), model);
                yield "employe/employe";
            }
            case EMPTY -> {
                mappingEmployeNonEnregistree(employeDTO, model);
                yield "employe/creationEmploye";
            }
            case ERROR -> throw reponseService.getException();
        };
    }

    /**
     * Page d'édition d'employé
     */
    @GetMapping("/editerEmploye/{id}")
    public String getEmployePourEdition(Model model, @PathVariable int id) {
        model.addAttribute("employe", modelMapper.map(employeService.findById(id).getData(), EmployeDTO.class));
        return "employe/editionEmploye";
    }

    /**
     * Gestion de l'édition d'employé
     */
    @PostMapping("/editerEmploye")
    public String editionEmploye(@Valid @ModelAttribute("employe") EmployeDTO employeDTO, BindingResult result, Model model) throws Exception {
        if (result.hasErrors()) {
            return "employe/editionEmploye";
        }
        ReponseService<Employe> reponseService = employeService.save(modelMapper.map(employeDTO.pourEdition(employeService.findById(employeDTO.getId()).getObjetRetour()), Employe.class));
        return switch (reponseService.getStatus()) {
            case OK -> {
                mappingEmployeEnregistree((Employe) reponseService.getData(), model);
                yield "employe/employe";
            }
            case EMPTY -> {
                mappingEmployeNonEnregistree(employeDTO, model);
                yield "employe/editionEmploye";
            }
            case ERROR -> throw reponseService.getException();
        };
    }

    private void mappingEmployeEnregistree(Employe employe, Model model) {
        model.addAttribute("employe", modelMapper.map(employe, EmployeDTO.class));
        model.addAttribute("messageEnregistrement", "Employe Enregistrée");
    }

    private void mappingEmployeNonEnregistree(EmployeDTO employeDTO, Model model) throws Exception {
        model.addAttribute("employeDTO", employeDTO);
        model.addAttribute("messageNonEnregistrement", "Employe non enregistrée");
    }

    @ModelAttribute(value = "employeDTO")
    private EmployeDTO getEmployeDTO() {
        return new EmployeDTO();
    }

    @ModelAttribute(value = "rechercheAffectation")
    private RechercheAffectationDetailsEmployeDTO getRechercheAffectation() {
        return new RechercheAffectationDetailsEmployeDTO();
    }

}
