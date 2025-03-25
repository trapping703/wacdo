package com.gdu.wacdo.controllers;

import com.gdu.wacdo.dto.form.RechercheFonction;
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

import java.util.List;

import static java.util.Collections.emptyList;

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
        ReponseService<List<Fonction>> reponse = fonctionService.findAll();
        if (reponse.isOk()) {
            List<FonctionDTO> fonctionDTOS = reponse.getObjetRetour().stream()
                    .map(fonction -> modelMapper.map(fonction, FonctionDTO.class))
                    .toList();
            model.addAttribute("fonctions", fonctionDTOS);
            return "fonctions";
        }
        throw reponse.getException();
    }

    @GetMapping("/detailFonction/{id}")
    public String getFonctionById(Model model, @PathVariable int id) {
        ReponseService<Fonction> reponse = fonctionService.findById(id);
        model.addAttribute("fonction", modelMapper.map(reponse.getData(), FonctionDTO.class));
        return "fonction";
    }

    @PostMapping("/rechercheFonctions")
    public String rechercheFonctions(RechercheFonction rechercheFonctions, Model model) throws Exception {
        ReponseService<List<Fonction>> reponseService = fonctionService.findByLibelle(rechercheFonctions.getLibelle());
        return switch (reponseService.getStatus()) {
            case OK -> {
                mappingListeFonctionQuandRechercheOK(rechercheFonctions, model, reponseService);
                yield "fonctions";
            }
            case EMPTY -> {
                mappingRechecheVide(rechercheFonctions, model);
                yield "fonctions";
            }
            case ERROR -> throw reponseService.getException();
        };
    }

    @GetMapping("/creerFonction")
    public String getCreerFonction() {
        return "creationFonction";
    }

    @PostMapping("/creerFonction")
    public String enregistrementFonction(FonctionDTO fonctionDTO, Model model) throws Exception {
        ReponseService<Fonction> reponseService = fonctionService.save(modelMapper.map(fonctionDTO, Fonction.class));
        return switch (reponseService.getStatus()) {
            case OK -> {
                mappingFonctionEnregistree(reponseService.getObjetRetour(), model);
                yield "fonction";
            }
            case EMPTY -> {
                mappingFonctionNonEnregistree(fonctionDTO, model);
                yield "creationFonction";
            }
            case ERROR -> throw reponseService.getException();
        };
    }

    @GetMapping("/editerFonction/{id}")
    public String editerFonction(Model model, @PathVariable int id) {
        ReponseService<Fonction> reponse = fonctionService.findById(id);
        model.addAttribute("fonction", modelMapper.map(reponse.getData(), FonctionDTO.class));
        return "editionFonction";
    }

    @PostMapping("/editerFonction")
    public String editerFonction(@Valid @ModelAttribute("fonction") FonctionDTO fonction, BindingResult result, Model model) throws Exception {
        if (result.hasErrors()) {
            return "editionFonction";
        }
        ReponseService<Fonction> reponseService = fonctionService.save(modelMapper.map(fonction, Fonction.class));
        return switch (reponseService.getStatus()) {
            case OK -> {
                mappingFonctionEnregistree(reponseService.getObjetRetour(), model);
                yield "fonction";
            }
            case EMPTY -> {
                mappingFonctionNonEditee(fonction, model);
                yield "editionFonction";
            }
            case ERROR -> throw reponseService.getException();
        };
    }

    /**
     * Réattribut l'objet de recherche de fonction, fournit la liste de fonction trouvé par la recherche.
     */
    private void mappingListeFonctionQuandRechercheOK(RechercheFonction rechercheFonctions, Model model, ReponseService<List<Fonction>> reponseService) {
        List<FonctionDTO> fonctionDTOS = reponseService.getObjetRetour().stream()
                .map(fonction -> modelMapper.map(fonction, FonctionDTO.class))
                .toList();
        model.addAttribute("rechercheFonctions", rechercheFonctions);
        model.addAttribute("fonctions", fonctionDTOS);
    }

    /**
     * Réattribut l'objet de recherche de fonction, fournit une liste vide de fonction et passe le message d'erreur.
     */
    private void mappingRechecheVide(RechercheFonction rechercheFonctions, Model model) throws Exception {
        model.addAttribute("rechercheFonctions", rechercheFonctions);
        model.addAttribute("fonctions", emptyList());
        model.addAttribute("recherchevide", "Aucune fonction trouvée");
    }

    /**
     * Réattribut l'objet fonction enregistrée et un message de validation.
     */
    private void mappingFonctionEnregistree(Fonction fonction, Model model) {
        model.addAttribute("fonction", modelMapper.map(fonction, FonctionDTO.class));
        model.addAttribute("messageEnregistrement", "Fonction Enregistrée");
    }

    /**
     * Réattribut l'objet fonction non enregistrée et d'erreur
     */
    private void mappingFonctionNonEnregistree(FonctionDTO fonctionDTO, Model model) throws Exception {
        model.addAttribute("fonctionDTO", fonctionDTO);
        model.addAttribute("messageNonEnregistrement", "Fonction non enregistrée");
    }

    /**
     * Réattribut l'objet fonction non éditer et d'erreur
     */
    private void mappingFonctionNonEditee(FonctionDTO fonctionDTO, Model model) throws Exception {
        model.addAttribute("fonction", fonctionDTO);
        model.addAttribute("messageNonEnregistrement", "Fonction non enregistrée");
    }

    @ModelAttribute(value = "rechercheFonctions")
    private RechercheFonction getrechercheFonction() {
        return new RechercheFonction();
    }

    @ModelAttribute(value = "fonctionDTO")
    private FonctionDTO getFonctionDTO() {
        return new FonctionDTO();
    }
}
