package com.gdu.wacdo.services;

import com.gdu.wacdo.dto.form.RechercheAffectation;
import com.gdu.wacdo.dto.form.RechercheAffectationDetailsEmploye;
import com.gdu.wacdo.dto.form.RechercheAffectationDetailsRestaurant;
import com.gdu.wacdo.dto.response.ReponseService;
import com.gdu.wacdo.model.Affectation;
import com.gdu.wacdo.model.Restaurant;
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

    public ReponseService<Affectation> save(Affectation affectation) {
        try {
            affectationRepository.save(affectation);
            if (affectation.getId() != null) {
                return reponse(OK, affectation);
            } else {
                return reponse(ERROR, affectation);
            }
        } catch (Exception e) {
            return reponse(ERROR, affectation, e);
        }
    }

    public ReponseService<List<Affectation>> findAll() {
        return reponse(OK, affectationRepository.findAll());
    }

    public ReponseService<Affectation> findById(int id) {
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
    public ReponseService<List<Affectation>> findAffectationsPourRechercheListeAffection(RechercheAffectation rechercheAffectation) {
        try {
            List<Affectation> affectations = affectationRepository.findAffectationsPourRechercheListeAffection(rechercheAffectation.getVille(), rechercheAffectation.getDateDebut(), rechercheAffectation.getDateFin(), rechercheAffectation.getFonction_id());
            if (!affectations.isEmpty()) {
                return reponse(OK, affectations);
            } else {
                return reponse(EMPTY, rechercheAffectation);
            }
        } catch (Exception e) {
            return reponse(ERROR, rechercheAffectation, e);
        }
    }

    /**
     * Utilisé pour la recherche des affectations pour la vue /affectations
     */
    public ReponseService<List<Affectation>> findAffectationsPourRechercheDetailsEmploye(RechercheAffectationDetailsEmploye rechercheAffectation, Integer idEmploye) {
        try {
            List<Affectation> affectations = affectationRepository.findAffectationsPourRechercheDetailsEmploye(rechercheAffectation.getDateDebut(), rechercheAffectation.getFonction_id(), idEmploye);
            if (!affectations.isEmpty()) {
                return reponse(OK, affectations);
            } else {
                return reponse(EMPTY, rechercheAffectation);
            }
        } catch (Exception e) {
            return reponse(ERROR, rechercheAffectation, e);
        }
    }


    /**
     * Retourne la liste d'affectation en cours filtré par le forumulaire {@link RechercheAffectationDetailsRestaurant}
     */
    public ReponseService<List<Affectation>> findAffectationsPourRechercheDetailsRestaurant(RechercheAffectationDetailsRestaurant rechercheAffectation, Integer idRestaurant) {
        try {
            List<Affectation> affectations = affectationRepository.findAffectationsPourRechercheDetailsRestaurant(rechercheAffectation.getDateDebut(), rechercheAffectation.getFonction_id(), rechercheAffectation.getEmploye_id(), idRestaurant);
            if (!affectations.isEmpty()) {
                return reponse(OK, affectations);
            } else {
                return reponse(EMPTY, rechercheAffectation);
            }
        } catch (Exception e) {
            return reponse(ERROR, rechercheAffectation, e);
        }
    }

    /**
     * Retourne la liste d'affectation en cours d'un restaurant.
     */
    public ReponseService<List<Affectation>> findByDateFinIsNullAndRestaurantIs(Restaurant restaurant) {
        try {
            List<Affectation> affectations = affectationRepository.findByDateFinIsNullAndRestaurantIs(restaurant);
            if (!affectations.isEmpty()) {
                return reponse(OK, affectations);
            } else {
                return reponse(EMPTY, restaurant);
            }
        } catch (Exception e) {
            return reponse(ERROR, restaurant, e);
        }
    }
}
