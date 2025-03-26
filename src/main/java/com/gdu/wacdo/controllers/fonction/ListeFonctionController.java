package com.gdu.wacdo.controllers.fonction;

import com.gdu.wacdo.dto.form.RechercheFonctionDTO;
import com.gdu.wacdo.dto.model.FonctionDTO;
import com.gdu.wacdo.dto.response.ReponseService;
import com.gdu.wacdo.model.Fonction;
import com.gdu.wacdo.services.FonctionService;
import org.modelmapper.ModelMapper;
import org.springframework.stereotype.Controller;
import org.springframework.ui.Model;
import org.springframework.web.bind.annotation.GetMapping;
import org.springframework.web.bind.annotation.ModelAttribute;
import org.springframework.web.bind.annotation.PostMapping;

import java.util.List;

import static java.util.Collections.emptyList;

@Controller
public class ListeFonctionController {

    private final FonctionService fonctionService;
    private final ModelMapper modelMapper;

    public ListeFonctionController(FonctionService fonctionService, ModelMapper modelMapper) {
        this.fonctionService = fonctionService;
        this.modelMapper = modelMapper;
    }

    /**
     * Page d'index de la liste de fonction
     */
    @GetMapping("/listeFonctions")
    public String getAllFonctions(Model model) throws Exception {
        ReponseService<List<Fonction>> reponse = fonctionService.findAll();
        if (reponse.isOk()) {
            List<FonctionDTO> fonctionDTOS = reponse.getObjetRetour().stream()
                    .map(fonction -> modelMapper.map(fonction, FonctionDTO.class))
                    .toList();
            model.addAttribute("fonctions", fonctionDTOS);
            return "fonction/fonctions";
        }
        throw reponse.getException();
    }

    /**
     * Gestion de la recherche des fonctions
     */
    @PostMapping("/rechercheFonctions")
    public String rechercheFonctions(RechercheFonctionDTO rechercheFonctions, Model model) throws Exception {
        ReponseService<List<Fonction>> reponseService = fonctionService.findByLibelle(rechercheFonctions.getLibelle());
        return switch (reponseService.getStatus()) {
            case OK -> {
                mappingListeFonctionQuandRechercheOK(rechercheFonctions, model, reponseService);
                yield "fonction/fonctions";
            }
            case EMPTY -> {
                mappingRechecheVide(rechercheFonctions, model);
                yield "fonction/fonctions";
            }
            case ERROR -> throw reponseService.getException();
        };
    }

    private void mappingListeFonctionQuandRechercheOK(RechercheFonctionDTO rechercheFonctions, Model model, ReponseService<List<Fonction>> reponseService) {
        List<FonctionDTO> fonctionDTOS = reponseService.getObjetRetour().stream()
                .map(fonction -> modelMapper.map(fonction, FonctionDTO.class))
                .toList();
        model.addAttribute("rechercheFonctions", rechercheFonctions);
        model.addAttribute("fonctions", fonctionDTOS);
    }

    private void mappingRechecheVide(RechercheFonctionDTO rechercheFonctions, Model model) throws Exception {
        model.addAttribute("rechercheFonctions", rechercheFonctions);
        model.addAttribute("fonctions", emptyList());
        model.addAttribute("recherchevide", "Aucune fonction trouvée");
    }

    @ModelAttribute(value = "rechercheFonctions")
    private RechercheFonctionDTO getrechercheFonction() {
        return new RechercheFonctionDTO();
    }
}
