package com.gdu.wacdo.controllers;

import com.gdu.wacdo.dto.model.FonctionDTO;
import com.gdu.wacdo.dto.response.ReponseService;
import com.gdu.wacdo.model.Fonction;
import com.gdu.wacdo.services.FonctionService;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

import java.util.List;

@Controller
@Slf4j
public class FonctionController {

    private final FonctionService fonctionService;
    private final ModelMapper modelMapper;

    public FonctionController(FonctionService fonctionService, ModelMapper modelMapper) {
        this.fonctionService = fonctionService;
        this.modelMapper = modelMapper;
    }

    @GetMapping("/listeFonctions")
    public String getAllFonctions(Model model) throws Exception {
        ReponseService reponse = fonctionService.findAll();
        if (reponse.isOk()) {
            List<FonctionDTO> fonctionDTOS = ((List<Fonction>) reponse.getData()).stream()
                    .map(fonction -> modelMapper.map(fonction, FonctionDTO.class))
                    .toList();
            model.addAttribute("fonctions", fonctionDTOS);
            return "fonctions";
        }
        throw reponse.getException();
    }

    @GetMapping("/detailFonction/{id}")
    public String getFonctionById(Model model, @PathVariable int id) {
        ReponseService reponse = fonctionService.findById(id);
        if (reponse.isOk()) {
            model.addAttribute("fonction", modelMapper.map(reponse.getData(), FonctionDTO.class));
            return "fonction";
        } else {
            return "index";
        }
    }
}
