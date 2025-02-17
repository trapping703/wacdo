package com.gdu.wacdo.services;

import com.gdu.wacdo.dto.form.RechercheAffectation;
import com.gdu.wacdo.dto.response.ReponseService;
import com.gdu.wacdo.model.Affectation;
import com.gdu.wacdo.repositories.AffectationRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

import static com.gdu.wacdo.dto.response.ReponseService.reponse;
import static com.gdu.wacdo.status.CodeReponse.*;

@Service
@Slf4j
public class AffectationService {

    private final AffectationRepository affectationRepository;

    public AffectationService(AffectationRepository affectationRepository) {
        this.affectationRepository = affectationRepository;
    }

    public ReponseService save(Affectation affectation) {
        try {
            affectationRepository.save(affectation);
            if(affectation.getId() != null) {
                return reponse(OK, affectation);
            }else{
                return reponse(ERROR, affectation);
            }
        } catch (Exception e) {
            return reponse(ERROR, affectation, e);
        }
    }

    public ReponseService findAll() {
        return reponse(OK, affectationRepository.findAll());
    }

    public ReponseService findById(int id) {
        try {
            Optional<Affectation> affectation = affectationRepository.findById(id);
            return affectation.map(a -> reponse(OK, a)).orElseGet(() -> reponse(EMPTY, id));
        } catch (Exception e) {
            return reponse(ERROR, id, e);
        }
    }

    /**
     * Utilisé pour la recherche des affectations pour la vue /affectations
     */
    public ReponseService findByRechercheAffectation(RechercheAffectation rechercheAffectation) {
        try {
            List<Affectation> affectations = affectationRepository.findAffectationsPourRecherche(rechercheAffectation.getVille(), rechercheAffectation.getDateDebut(), rechercheAffectation.getDateFin(), rechercheAffectation.getFonction_id());
            if (!affectations.isEmpty()) {
                return reponse(OK, affectations);
            } else {
                return reponse(EMPTY, rechercheAffectation);
            }
        } catch (Exception e) {
            return reponse(ERROR, rechercheAffectation, e);
        }
    }
}
