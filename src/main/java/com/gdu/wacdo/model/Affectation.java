package com.gdu.wacdo.model;

import jakarta.persistence.Entity;
import jakarta.persistence.JoinColumn;
import jakarta.persistence.ManyToOne;
import lombok.Data;

import java.time.LocalDate;

@Entity
@Data
public class Affectation extends  AbstractPersistentEntity<Integer> {

    private LocalDate dateDebut;
    private LocalDate dateFin;

    @ManyToOne
    private Employe employe;
    @ManyToOne
    private Restaurant restaurant;
    @ManyToOne
    private Fonction fonction;

}
