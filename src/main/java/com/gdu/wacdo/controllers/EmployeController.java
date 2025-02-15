package com.gdu.wacdo.controllers;

import com.gdu.wacdo.dto.model.EmployeDTO;
import com.gdu.wacdo.dto.response.ReponseService;
import com.gdu.wacdo.model.Employe;
import com.gdu.wacdo.services.EmployeService;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@Controller
public class EmployeController {

    private final EmployeService employeService;
    private final ModelMapper modelMapper;

    public EmployeController(EmployeService employeService, ModelMapper modelMapper) {
        this.employeService = employeService;
        this.modelMapper = modelMapper;
    }

    @GetMapping("/employes")
    public String employes(Model model) throws Exception {
        ReponseService reponse = employeService.findAll();
        if (reponse.isOk()) {
            List<EmployeDTO> employeDTOs = ((List<Employe>)reponse.getData()).stream()
                    .map(employe -> modelMapper.map(employe, EmployeDTO.class))
                    .toList();
            model.addAttribute("employes", employeDTOs);
        } else {
            throw reponse.getException();
        }
        return "employes";
    }

    @GetMapping("/employe/{id}")
    public String employes(Model model, @PathVariable int id) {
        ReponseService reponse = employeService.findById(id);
        if (reponse.isOk()) {
            model.addAttribute("employe", modelMapper.map(reponse.getData(), EmployeDTO.class));
        }
        return "employe";
    }
}
