package com.gdu.wacdo.services;

import com.gdu.wacdo.model.Employe;
import org.springframework.stereotype.Service;

import java.util.List;

@Service
public class EmployeNonAffecteService {

    public List<Employe> filtrerEmployesNonAffecte(List<Employe> employes) {
        return employes.stream().filter(this::isNonAffecte).toList();
    }

    private boolean isNonAffecte(Employe employe) {
        if(employe.getAffectations().isEmpty()){
            return true;
        }
        return employe.getAffectations().stream().noneMatch(affectation -> affectation.getDateFin() == null);
    }
}
