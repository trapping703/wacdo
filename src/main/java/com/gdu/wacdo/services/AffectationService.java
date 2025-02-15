package com.gdu.wacdo.services;

import com.gdu.wacdo.dto.form.RechercheAffectation;
import com.gdu.wacdo.dto.response.ReponseService;
import com.gdu.wacdo.model.Affectation;
import com.gdu.wacdo.repositories.AffectationRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

import static com.gdu.wacdo.dto.response.ReponseService.reponse;
import static com.gdu.wacdo.status.CodeReponse.*;

@Service
@Slf4j
public class AffectationService {

    @PersistenceContext
    private EntityManager em;

    private final AffectationRepository affectationRepository;

    public AffectationService(AffectationRepository affectationRepository) {
        this.affectationRepository = affectationRepository;
    }

    public ReponseService<Affectation> save(Affectation affectation) {
        try {
            return reponse(OK, affectationRepository.save(affectation));
        } catch (Exception e) {
            return reponse(ERROR, affectation, e);
        }
    }

    public ReponseService<List<Affectation>> findAll() {
        return reponse(OK, affectationRepository.findAll());
    }

    public ReponseService findById(int id) {
        try {
            Optional<Affectation> affectation = affectationRepository.findById(id);
            if (affectation.isPresent()) {
                return reponse(OK, affectation.get());
            } else {
                return reponse(IDK, id);
            }
        } catch (Exception e) {
            return reponse(ERROR, id, e);
        }
    }

    public ReponseService<Integer> delete(int id) {
        try {
            affectationRepository.deleteById(id);
            return reponse(OK, id);
        } catch (Exception e) {
            return reponse(ERROR, id, e);
        }
    }

    /**
     * Utilisé pour la recherche des affectations pour la vue /affectations
     */
    public ReponseService findByRechercheAffectation(RechercheAffectation rechercheAffectation) {
        try {
            List<Affectation> affectations = affectationRepository.findByRechercheAffectation(rechercheAffectation.getVille(), rechercheAffectation.getDateDebut(), rechercheAffectation.getDateFin(), rechercheAffectation.getFonction_id());
            if (!affectations.isEmpty()) {
                return reponse(OK, affectations);
            } else {
                return reponse(IDK, rechercheAffectation);
            }
        } catch (Exception e) {
            return reponse(ERROR, rechercheAffectation, e);
        }
    }
}
