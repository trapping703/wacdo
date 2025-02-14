package com.gdu.wacdo.dto.model;

import lombok.Data;

import java.time.LocalDate;

@Data
public class AffectationDTO {

    private int id;
    private LocalDate dateDebut;
    private LocalDate dateFin;

    private EmployeDTO employe;
    private RestaurantDTO restaurant;
    private FonctionDTO fonction;

    public AffectationDTO(int id, LocalDate dateDebut, LocalDate dateFin, EmployeDTO employe, RestaurantDTO restaurant, FonctionDTO fonction) {
        this.setId(id);
        this.dateDebut = dateDebut;
        this.dateFin = dateFin;
        this.employe = employe;
        this.restaurant = restaurant;
        this.fonction = fonction;
    }

    public AffectationDTO() {

    }
}
