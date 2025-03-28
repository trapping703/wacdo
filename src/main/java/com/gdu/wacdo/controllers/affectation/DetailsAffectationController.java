package com.gdu.wacdo.controllers.affectation;

import com.gdu.wacdo.dto.model.AffectationDTO;
import com.gdu.wacdo.dto.response.ReponseService;
import com.gdu.wacdo.model.Affectation;
import com.gdu.wacdo.services.AffectationService;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.PathVariable;

@Controller
public class DetailsAffectationController {

    private final AffectationService affectationService;
    private final ModelMapper modelMapper;

    public DetailsAffectationController(AffectationService affectationService, ModelMapper modelMapper) {
        this.affectationService = affectationService;
        this.modelMapper = modelMapper;
    }

    /**
     * Page d'index du détail d'affectation
     */
    @GetMapping("/detailAffectation/{id}")
    public String getAffectationById(Model model, @PathVariable int id) throws Exception {
        ReponseService<Affectation> reponse = affectationService.findById(id);
        model.addAttribute("affectation", modelMapper.map(reponse.getData(), AffectationDTO.class));
        return "affectation/affectation";
    }
}
