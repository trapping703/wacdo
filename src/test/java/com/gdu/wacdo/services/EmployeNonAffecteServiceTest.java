package com.gdu.wacdo.services;

import com.gdu.wacdo.model.Affectation;
import com.gdu.wacdo.model.Employe;
import org.assertj.core.api.Assertions;
import org.junit.jupiter.api.Test;

import java.time.LocalDate;
import java.util.List;

class EmployeNonAffecteServiceTest {

    @Test
    public void givenListeEmployesNonAffectéEtAffecte_whenFiltrer_thenRetourneEmployesNonAffecte() {
        //given
        Employe employeAffecte = creerEmployeAffecte();
        Employe employeNonAffecte = creerEmployeNonAffecte();
        Employe employeSansAffectation = new Employe();
        //when
        List<Employe> employesFiltrer = new EmployeNonAffecteService().filtrerEmployesNonAffecte(List.of(employeAffecte, employeSansAffectation, employeNonAffecte));
        //then
        Assertions.assertThat(employesFiltrer).containsOnly(employeNonAffecte, employeSansAffectation);
    }

    private static Employe creerEmployeAffecte() {
        Employe employe = new Employe();
        Affectation affectation = new Affectation();
        affectation.setDateFin(null);
        Affectation affectation2 = new Affectation();
        affectation.setDateFin(LocalDate.now());
        employe.setAffectations(List.of(affectation, affectation2));
        return employe;
    }

    private static Employe creerEmployeNonAffecte() {
        Employe employe = new Employe();
        Affectation affectation = new Affectation();
        affectation.setDateFin(LocalDate.now());
        employe.setAffectations(List.of(affectation));
        return employe;
    }

}