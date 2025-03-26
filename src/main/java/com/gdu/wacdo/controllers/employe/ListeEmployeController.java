package com.gdu.wacdo.controllers.employe;

import com.gdu.wacdo.dto.form.RechercheEmployeDTO;
import com.gdu.wacdo.dto.model.EmployeDTO;
import com.gdu.wacdo.dto.response.ReponseService;
import com.gdu.wacdo.model.Employe;
import com.gdu.wacdo.services.EmployeNonAffecteService;
import com.gdu.wacdo.services.EmployeService;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;

import static java.util.Collections.emptyList;

@Controller
public class ListeEmployeController {

    private final EmployeService employeService;
    private final EmployeNonAffecteService employeNonAffecteService;
    private final ModelMapper modelMapper;

    public ListeEmployeController(EmployeService employeService, EmployeNonAffecteService employeNonAffecteService, ModelMapper modelMapper) {
        this.employeService = employeService;
        this.employeNonAffecteService = employeNonAffecteService;
        this.modelMapper = modelMapper;
    }

    /**
     * Page de la liste des employés
     */
    @GetMapping("/listeEmployes")
    public String employes(Model model) throws Exception {
        List<EmployeDTO> employeDTOs = employeService.findAll().getObjetRetour().stream()
                .map(employe -> modelMapper.map(employe, EmployeDTO.class))
                .toList();
        model.addAttribute("employes", employeDTOs);
        return "employe/employes";
    }

    /**
     * Gestion de la recherche des employés non affectés
     */
    @GetMapping("/listeEmployesNA")
    public String getListeEmployesNonAffectes(Model model) throws Exception {
        List<EmployeDTO> employeDTOs = employeNonAffecteService.filtrerEmployesNonAffecte(employeService.findAll().getObjetRetour()).stream()
                .map(employe -> modelMapper.map(employe, EmployeDTO.class))
                .toList();
        model.addAttribute("employes", employeDTOs);
        return "employe/employes";
    }

    /**
     * Gestion de la recherche d'employe
     */
    @PostMapping("/rechercheEmployes")
    public String rechercheEmployes(RechercheEmployeDTO rechercheEmployes, Model model) throws Exception {
        ReponseService<List<Employe>> reponseService = employeService.findByRechercheEmploye(rechercheEmployes);
        return switch (reponseService.getStatus()) {
            case OK -> {
                mappingListeEmployeQuandRechercheOK(rechercheEmployes, model, reponseService);
                yield "employe/employes";
            }
            case EMPTY -> {
                mappingQuandRechecheEmpty(rechercheEmployes, model);
                yield "employe/employes";
            }
            case ERROR -> throw reponseService.getException();
        };
    }

    private void mappingListeEmployeQuandRechercheOK(RechercheEmployeDTO rechercheEmployes, Model model, ReponseService<List<Employe>> reponseService) {
        List<EmployeDTO> employeDTOS = reponseService.getObjetRetour().stream()
                .map(employe -> modelMapper.map(employe, EmployeDTO.class))
                .toList();
        model.addAttribute("rechercheEmployes", rechercheEmployes);
        model.addAttribute("employes", employeDTOS);
    }

    private void mappingQuandRechecheEmpty(RechercheEmployeDTO rechercheEmployes, Model model) {
        model.addAttribute("rechercheEmployes", rechercheEmployes);
        model.addAttribute("employes", emptyList());
        model.addAttribute("recherchevide", "Aucun employé trouvé");
    }

    @ModelAttribute(value = "rechercheEmployes")
    private RechercheEmployeDTO getRechercheEmploye() {
        return new RechercheEmployeDTO();
    }
}
