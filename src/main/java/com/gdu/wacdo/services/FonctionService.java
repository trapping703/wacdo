package com.gdu.wacdo.services;

import com.gdu.wacdo.dto.response.ReponseService;
import com.gdu.wacdo.model.Fonction;
import com.gdu.wacdo.repositories.FonctionRepository;
import jakarta.transaction.Transactional;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

import static com.gdu.wacdo.dto.response.ReponseService.reponse;
import static com.gdu.wacdo.status.CodeReponse.*;

@Service
@Slf4j
public class FonctionService {

    private final FonctionRepository fonctionRepository;

    public FonctionService(FonctionRepository fonctionRepository) {
        this.fonctionRepository = fonctionRepository;
    }

    public ReponseService save(Fonction fonction) {
        try {
            fonctionRepository.save(fonction);
            if (fonction.getId() != null) {
                return reponse(OK, fonction);
            } else {
                return reponse(EMPTY, fonction);
            }
        } catch (Exception e) {
            return reponse(ERROR, fonction, e);
        }
    }

    public ReponseService findAll() {
        return reponse(OK, fonctionRepository.findAll());
    }

    public ReponseService findById(int id) {
        try {
            Optional<Fonction> fonction = fonctionRepository.findById(id);
            return fonction.map(value -> reponse(OK, value)).orElseGet(() -> reponse(EMPTY, id));
        } catch (Exception e) {
            return reponse(ERROR, id, e);
        }
    }

    public ReponseService delete(int id) {
        try {
            fonctionRepository.deleteById(id);
            return reponse(OK, id);
        } catch (Exception e) {
            return reponse(ERROR, id, e);
        }
    }

    /**
     * Utilisé pour la recherche des fonctions pour la vue /fonctions
     */
    public ReponseService findByLibelle(String libelle) {
        try {
            List<Fonction> fonctions = fonctionRepository.findByLibelleContaining(libelle);
            if (fonctions.isEmpty()) {
                return reponse(EMPTY, libelle);
            } else {
                return reponse(OK, fonctions);
            }
        } catch (Exception e) {
            return reponse(ERROR, libelle, e);
        }
    }
}
