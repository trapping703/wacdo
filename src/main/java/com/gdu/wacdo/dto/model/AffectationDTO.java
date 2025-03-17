package com.gdu.wacdo.dto.model;

import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

@Data
public class AffectationDTO {

    private Integer id;

    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate dateDebut;
    @DateTimeFormat(pattern = "yyyy-MM-dd")
    private LocalDate dateFin;
    private boolean depuisEmploye;
    private boolean depuisRestaurant;

    private EmployeDTO employe;
    private RestaurantDTO restaurant;
    private FonctionDTO fonction;

    public AffectationDTO(Integer id, LocalDate dateDebut, LocalDate dateFin, EmployeDTO employe, RestaurantDTO restaurant, FonctionDTO fonction) {
        this.id = id;
        this.dateDebut = dateDebut;
        this.dateFin = dateFin;
        this.employe = employe;
        this.restaurant = restaurant;
        this.fonction = fonction;
    }

    public AffectationDTO() {

    }
}
