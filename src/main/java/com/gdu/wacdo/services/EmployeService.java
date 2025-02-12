package com.gdu.wacdo.services;

import com.gdu.wacdo.model.Employe;
import com.gdu.wacdo.repositories.EmployeRepository;
import com.gdu.wacdo.dto.response.ReponseService;
import lombok.extern.slf4j.Slf4j;
import org.springframework.stereotype.Service;

import java.util.List;
import java.util.Optional;

import static com.gdu.wacdo.status.CodeReponse.*;
import static com.gdu.wacdo.dto.response.ReponseService.reponse;

@Service
@Slf4j
public class EmployeService {

    private final EmployeRepository employeRepository;

    public EmployeService(EmployeRepository employeRepository) {
        this.employeRepository = employeRepository;
    }

    public ReponseService<Employe> save(Employe employe) {
        try {
            return reponse(OK, employeRepository.save(employe));
        } catch (Exception e) {
            return reponse(ERROR, employe, e);
        }
    }

    public ReponseService<List<Employe>> findAll() {
        return reponse(OK, employeRepository.findAll());
    }

    public ReponseService findById(int id) {
        try {
            Optional<Employe> employe = employeRepository.findById(id);
            if(employe.isPresent()) {
                return reponse(OK, employe.get());
            }else{
                return reponse(IDK, id);
            }
        } catch (Exception e) {
            return reponse(ERROR, id, e);
        }
    }

    public ReponseService<Integer> delete(int id) {
        try {
            employeRepository.deleteById(id);
            return reponse(OK, id);
        } catch (Exception e) {
            return reponse(ERROR, id, e);
        }
    }

    public ReponseService findByNom(String nom) {
        try {
            return reponse(OK, employeRepository.findByNom(nom));
        } catch (Exception e) {
            return reponse(ERROR, nom, e);
        }
    }

    public ReponseService findByPrenom(String prenom) {
        try {
            return reponse(OK, employeRepository.findByPrenom(prenom));
        } catch (Exception e) {
            return reponse(ERROR, prenom, e);
        }
    }

    public ReponseService findByEmail(String email) {
        try {
            return reponse(OK, employeRepository.findByEmail(email));
        } catch (Exception e) {
            return reponse(ERROR, email, e);
        }
    }
}
