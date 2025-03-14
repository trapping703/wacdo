package com.gdu.wacdo.controllers;

import com.gdu.wacdo.dto.form.RechercheEmploye;
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

import java.util.List;

import static java.util.Collections.emptyList;

@Controller
public class EmployeController {

    private final EmployeService employeService;
    private final ModelMapper modelMapper;

    public EmployeController(EmployeService employeService, ModelMapper modelMapper) {
        this.employeService = employeService;
        this.modelMapper = modelMapper;
    }

    @GetMapping("/listeEmployes")
    public String employes(Model model) throws Exception {
        ReponseService reponse = employeService.findAll();
        if (reponse.isOk()) {
            List<EmployeDTO> employeDTOs = ((List<Employe>) reponse.getData()).stream()
                    .map(employe -> modelMapper.map(employe, EmployeDTO.class))
                    .toList();
            model.addAttribute("employes", employeDTOs);
        } else {
            throw reponse.getException();
        }
        return "employes";
    }

    @GetMapping("/detailEmploye/{id}")
    public String employes(Model model, @PathVariable int id) {
        ReponseService reponse = employeService.findById(id);
        if (reponse.isOk()) {
            model.addAttribute("employe", modelMapper.map(reponse.getData(), EmployeDTO.class));
        }
        return "employe";
    }

    @PostMapping("/rechercheEmployes")
    public String rechercheEmployes(RechercheEmploye rechercheEmployes, Model model) throws Exception {
        ReponseService reponseService = employeService.findByRechercheEmploye(rechercheEmployes);
        return switch (reponseService.getStatus()) {
            case OK -> {
                mappingListeEmployeQuandRechercheOK(rechercheEmployes, model, reponseService);
                yield "employes";
            }
            case EMPTY -> {
                mappingQuandRechecheEmpty(rechercheEmployes, model);
                yield "employes";
            }
            case ERROR -> throw reponseService.getException();
        };
    }

    @GetMapping("/creerEmploye")
    public String getCreerEmploye() {
        return "creationEmploye";
    }

    @PostMapping("/creerEmploye")
    public String enregistrementEmploye(@Valid @ModelAttribute("employeDTO") EmployeDTO employeDTO, BindingResult result, Model model) throws Exception {
        if (result.hasErrors()) {
            return "creationEmploye";
        }
        ReponseService reponseService = employeService.save(modelMapper.map(employeDTO, Employe.class));
        return switch (reponseService.getStatus()) {
            case OK -> {
                mappingEmployeEnregistree((Employe) reponseService.getData(), model);
                yield "employe";
            }
            case EMPTY -> {
                mappingEmployeNonEnregistree(employeDTO, model);
                yield "creationEmploye";
            }
            case ERROR -> throw reponseService.getException();
        };
    }

    /**
     * Réattribut l'objet de recherche d'employé, fournit la liste d'employé trouvé par la recherche.
     */
    private void mappingListeEmployeQuandRechercheOK(RechercheEmploye rechercheEmployes, Model model, ReponseService reponseService) {
        List<EmployeDTO> employeDTOS = ((List<Employe>) reponseService.getData()).stream()
                .map(employe -> modelMapper.map(employe, EmployeDTO.class))
                .toList();
        model.addAttribute("rechercheEmployes", rechercheEmployes);
        model.addAttribute("employes", employeDTOS);
    }

    /**
     * Réattribut l'objet de recherche d'employé, fournit une liste vide d'employé et passe le message d'erreur.
     */
    private void mappingQuandRechecheEmpty(RechercheEmploye rechercheEmployes, Model model) {
        model.addAttribute("rechercheEmployes", rechercheEmployes);
        model.addAttribute("employes", emptyList());
        model.addAttribute("recherchevide", "Aucun employé trouvé");
    }

    /**
     * Réattribut l'objet employe enregistrée et un message de validation.
     */
    private void mappingEmployeEnregistree(Employe employe, Model model) {
        model.addAttribute("employe", modelMapper.map(employe, EmployeDTO.class));
        model.addAttribute("messageEnregistrement", "Employe Enregistrée");
    }

    /**
     *Réattribut l'objet employeDTO avec un message d'erreur
     */
    private void mappingEmployeNonEnregistree(EmployeDTO employeDTO, Model model) throws Exception {
        model.addAttribute("employeDTO", employeDTO);
        model.addAttribute("messageNonEnregistrement", "Employe non enregistrée");
    }

    @ModelAttribute(value = "rechercheEmployes")
    private RechercheEmploye getrechercheEmploye() {
        return new RechercheEmploye();
    }

    @ModelAttribute(value = "employeDTO")
    private EmployeDTO getEmployeDTO() {
        return new EmployeDTO();
    }

}
