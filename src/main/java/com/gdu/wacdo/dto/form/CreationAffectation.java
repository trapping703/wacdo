package com.gdu.wacdo.dto.form;

import com.gdu.wacdo.model.Affectation;
import com.gdu.wacdo.model.Employe;
import com.gdu.wacdo.model.Fonction;
import com.gdu.wacdo.model.Restaurant;
import jakarta.validation.constraints.NotNull;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

@Data
public class CreationAffectation {

    private Integer id;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    @NotNull(message = "dateDebut incompatible")
    private LocalDate dateDebut;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate dateFin;
    private boolean depuisEmploye;
    private boolean depuisRestaurant;

    @NotNull(message = "employe incompatible")
    private Integer employe;
    @NotNull(message = "restaurant incompatible")
    private Integer restaurant;
    @NotNull(message = "fonction incompatible")
    private Integer fonction;

    public CreationAffectation(Integer id, LocalDate dateDebut, LocalDate dateFin, Integer employe, Integer restaurant, Integer fonction) {
        this.id = id;
        this.dateDebut = dateDebut;
        this.dateFin = dateFin;
        this.employe = employe;
        this.restaurant = restaurant;
        this.fonction = fonction;
    }

    public CreationAffectation(Affectation affectation) {
        this.id = affectation.getId();
        this.dateDebut = affectation.getDateDebut();
        this.dateFin = affectation.getDateFin();
        this.employe = affectation.getEmploye().getId();
        this.restaurant = affectation.getRestaurant().getId();
        this.fonction = affectation.getFonction().getId();
    }

    public CreationAffectation() {

    }

    public CreationAffectation creationDepuisEmploye(Integer employee) {
        this.depuisEmploye = true;
        this.employe = employee;
        return this;
    }

    public CreationAffectation creationDepuisRestaurant(Integer restaurant) {
        this.depuisRestaurant = true;
        this.restaurant = restaurant;
        return this;
    }

    public Affectation toAffectation(Employe employe, Restaurant restaurant, Fonction fonction) {
        return new Affectation(dateDebut, dateFin, employe, restaurant, fonction);
    }

    public Affectation pourEdition(Affectation affectation, Employe employe, Restaurant restaurant, Fonction fonction) {
        affectation.setEmploye(employe);
        affectation.setRestaurant(restaurant);
        affectation.setFonction(fonction);
        affectation.setDateDebut(dateDebut);
        affectation.setDateFin(dateFin);
        return affectation;
    }
}
