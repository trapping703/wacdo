package com.gdu.wacdo.controllers.fonction;

import com.gdu.wacdo.dto.model.FonctionDTO;
import com.gdu.wacdo.dto.response.ReponseService;
import com.gdu.wacdo.model.Fonction;
import com.gdu.wacdo.services.FonctionService;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
public class DetailsFonctionController {

    private final FonctionService fonctionService;
    private final ModelMapper modelMapper;

    public DetailsFonctionController(FonctionService fonctionService, ModelMapper modelMapper) {
        this.fonctionService = fonctionService;
        this.modelMapper = modelMapper;
    }

    @GetMapping("/detailFonction/{id}")
    public String getFonctionById(Model model, @PathVariable int id) {
        ReponseService<Fonction> reponse = fonctionService.findById(id);
        model.addAttribute("fonction", modelMapper.map(reponse.getData(), FonctionDTO.class));
        return "fonction/fonction";
    }
}
