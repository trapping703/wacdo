package com.gdu.wacdo.controllers.fonction;

import com.gdu.wacdo.dto.model.FonctionDTO;
import com.gdu.wacdo.dto.response.ReponseService;
import com.gdu.wacdo.model.Fonction;
import com.gdu.wacdo.services.FonctionService;
import jakarta.validation.Valid;
import lombok.extern.slf4j.Slf4j;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.validation.BindingResult;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PathVariable;
import org.springframework.web.bind.annotation.PostMapping;

@Controller
@Slf4j
public class GestionFonctionController {

    private final FonctionService fonctionService;
    private final ModelMapper modelMapper;

    public GestionFonctionController(FonctionService fonctionService, ModelMapper modelMapper) {
        this.fonctionService = fonctionService;
        this.modelMapper = modelMapper;
    }

    /**
     * Page vide de création de fonction
     */
    @GetMapping("/creerFonction")
    public String getCreerFonction() {
        return "fonction/creationFonction";
    }

    /**
     * Gestion de la création de fonction
     */
    @PostMapping("/creerFonction")
    public String enregistrementFonction(FonctionDTO fonctionDTO, Model model) throws Exception {
        ReponseService<Fonction> reponseService = fonctionService.save(modelMapper.map(fonctionDTO, Fonction.class));
        return switch (reponseService.getStatus()) {
            case OK -> {
                mappingFonctionEnregistree(reponseService.getObjetRetour(), model);
                yield "fonction/fonction";
            }
            case EMPTY -> {
                mappingFonctionNonEnregistree(fonctionDTO, model);
                yield "fonction/creationFonction";
            }
            case ERROR -> throw reponseService.getException();
        };
    }

    /**
     * page d'édition de la fonction
     */
    @GetMapping("/editerFonction/{id}")
    public String editerFonction(Model model, @PathVariable int id) {
        ReponseService<Fonction> reponse = fonctionService.findById(id);
        model.addAttribute("fonction", modelMapper.map(reponse.getData(), FonctionDTO.class));
        return "fonction/editionFonction";
    }

    /**
     * Gestion de l'édition de la fonction
     */
    @PostMapping("/editerFonction")
    public String editerFonction(@Valid @ModelAttribute("fonction") FonctionDTO fonction, BindingResult result, Model model) throws Exception {
        if (result.hasErrors()) {
            return "fonction/editionFonction";
        }
        ReponseService<Fonction> reponseService = fonctionService.save(modelMapper.map(fonction, Fonction.class));
        return switch (reponseService.getStatus()) {
            case OK -> {
                mappingFonctionEnregistree(reponseService.getObjetRetour(), model);
                yield "fonction/fonction";
            }
            case EMPTY -> {
                mappingFonctionNonEditee(fonction, model);
                yield "fonction/editionFonction";
            }
            case ERROR -> throw reponseService.getException();
        };
    }

    private void mappingFonctionEnregistree(Fonction fonction, Model model) {
        model.addAttribute("fonction", modelMapper.map(fonction, FonctionDTO.class));
        model.addAttribute("messageEnregistrement", "Fonction Enregistrée");
    }

    private void mappingFonctionNonEnregistree(FonctionDTO fonctionDTO, Model model) throws Exception {
        model.addAttribute("fonctionDTO", fonctionDTO);
        model.addAttribute("messageNonEnregistrement", "Fonction non enregistrée");
    }

    private void mappingFonctionNonEditee(FonctionDTO fonctionDTO, Model model) throws Exception {
        model.addAttribute("fonction", fonctionDTO);
        model.addAttribute("messageNonEnregistrement", "Fonction non enregistrée");
    }

    @ModelAttribute(value = "fonctionDTO")
    private FonctionDTO getFonctionDTO() {
        return new FonctionDTO();
    }
}
