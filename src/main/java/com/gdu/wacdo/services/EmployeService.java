package com.gdu.wacdo.services;

import com.gdu.wacdo.dto.form.RechercheEmploye;
import com.gdu.wacdo.dto.response.ReponseService;
import com.gdu.wacdo.model.Employe;
import com.gdu.wacdo.repositories.EmployeRepository;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

import static com.gdu.wacdo.dto.response.ReponseService.reponse;
import static com.gdu.wacdo.status.CodeReponse.*;

@Service
@Slf4j
public class EmployeService {

    private final EmployeRepository employeRepository;
    private final PasswordEncodeur passwordEncodeur;

    public EmployeService(EmployeRepository employeRepository, PasswordEncodeur passwordEncodeur) {
        this.employeRepository = employeRepository;
        this.passwordEncodeur = passwordEncodeur;
    }

    public ReponseService save(Employe employe) {
        try {
            passwordEncodeur.encrypte(employe);
            employeRepository.save(employe);
            if (employe.getId() != null) {
                return reponse(OK, employe);
            } else {
                return reponse(ERROR, employe);
            }
        } catch (Exception e) {
            return reponse(ERROR, employe, e);
        }
    }

    public ReponseService findAll() {
        return reponse(OK, employeRepository.findAll());
    }

    public ReponseService findById(int id) {
        try {
            Optional<Employe> employe = employeRepository.findById(id);
            return employe.map(value -> reponse(OK, value)).orElseGet(() -> reponse(EMPTY, id));
        } catch (Exception e) {
            return reponse(ERROR, id, e);
        }
    }

    /**
     * Utilisé pour la recherche des Employées pour la vue /employes
     */
    public ReponseService findByRechercheEmploye(RechercheEmploye rechercheEmploye) {
        try {
            List<Employe> employes = employeRepository.getEmployesPourRecherche(rechercheEmploye.getNom(), rechercheEmploye.getPrenom(), rechercheEmploye.getEmail());
            if (!employes.isEmpty()) {
                return reponse(OK, employes);
            } else {
                return reponse(EMPTY, rechercheEmploye);
            }
        } catch (Exception e) {
            return reponse(ERROR, rechercheEmploye, e);
        }
    }
}
