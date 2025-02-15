package com.gdu.wacdo.model;

import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Data;
import org.springframework.format.annotation.DateTimeFormat;

import java.time.LocalDate;

@Entity
@Data
public class Affectation extends AbstractPersistentEntity<Integer> {


    @DateTimeFormat(pattern="yyyy-MM-dd")
    private LocalDate dateDebut;
    @DateTimeFormat(pattern="yyyy-MM-dd")
    private LocalDate dateFin;

    @ManyToOne
    private Employe employe;
    @ManyToOne
    private Restaurant restaurant;
    @ManyToOne
    private Fonction fonction;

    public Affectation(int id, LocalDate dateDebut, LocalDate dateFin, Employe employe, Restaurant restaurant, Fonction fonction) {
        this.setId(id);
        this.dateDebut = dateDebut;
        this.dateFin = dateFin;
        this.employe = employe;
        this.restaurant = restaurant;
        this.fonction = fonction;
    }

    public Affectation() {

    }
}
