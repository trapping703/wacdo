package com.gdu.wacdo.services;

import com.gdu.wacdo.dto.response.ReponseService;
import com.gdu.wacdo.model.Affectation;
import com.gdu.wacdo.model.Fonction;
import com.gdu.wacdo.repositories.AffectationRepository;
import jakarta.persistence.EntityManager;
import jakarta.persistence.PersistenceContext;
import jakarta.persistence.TypedQuery;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.time.LocalDate;
import java.util.List;
import java.util.Optional;

import static com.gdu.wacdo.dto.response.ReponseService.reponse;
import static com.gdu.wacdo.status.CodeReponse.*;

@Service
@Slf4j
@Transactional
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

    public ReponseService findByFonction(Fonction fonction) {
        try {
            return reponse(OK, affectationRepository.findByFonction(fonction));
        } catch (Exception e) {
            return reponse(ERROR, fonction, e);
        }
    }

    public ReponseService findByDateDebut(LocalDate dateDebut) {
        try {
            return reponse(OK, affectationRepository.findByDateDebut(dateDebut));
        } catch (Exception e) {
            return reponse(ERROR, dateDebut, e);
        }
    }

    public ReponseService findByDateFin(LocalDate dateFin) {
        try {
            return reponse(OK, affectationRepository.findByDateFin(dateFin));
        } catch (Exception e) {
            return reponse(ERROR, dateFin, e);
        }
    }

    public ReponseService findByVille(String ville) {
        TypedQuery<Affectation> query = em.createQuery("select a from AffectationDTO a inner join Restaurant b where b.ville like :ville", Affectation.class);
        query.setParameter("ville", "%" + ville + "%");
        try {
            List<Affectation> affectations = query.getResultList();
            if (!affectations.isEmpty()) {
                return reponse(OK, affectations);
            } else {
                return reponse(IDK, ville);
            }
        } catch (Exception e) {
            return reponse(ERROR, ville, e);
        }
    }
}
